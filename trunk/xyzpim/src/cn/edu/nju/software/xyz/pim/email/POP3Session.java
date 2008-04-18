/*
 * XYZPIM, the pim on Android Platform
 *
 * Copyright (c) 2008, xyz team or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by xyz team.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package cn.edu.nju.software.xyz.pim.email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLSocketFactory;

import android.util.Log;

/**
 * @author xmx 2008-4-14 下午08:03:46
 * 
 */
public class POP3Session extends Session {
	private Socket pop3Socket;
	private BufferedReader in;
	private BufferedWriter out;

	private static POP3Session ins;

	private POP3Session() {
	}

	// Singleton!!
	public static POP3Session getInstance() {
		if (null == ins)
			ins = new POP3Session();
		return ins;
	}

	@Override
	public void open() throws UnknownHostException, IOException, EmailException {
		open(false);
	}

	public void open(boolean isSSL) throws UnknownHostException, IOException,
			EmailException {
		if (!isSSL)
			pop3Socket = new Socket(host, port);
		else {
			// 打开SSL连接，gmail必须！！！
			SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory
					.getDefault();
			pop3Socket = factory.createSocket(host, port);
		}
		in = new BufferedReader(new InputStreamReader(pop3Socket
				.getInputStream()));

		out = new BufferedWriter(new OutputStreamWriter(pop3Socket
				.getOutputStream()));

		String cmd; // 发给服务器的命令
		String line; // 从服务器收到的一行回应

		log(S, in.readLine());

		cmd = "user " + username + CRLF;
		log(C, cmd);
		out.write(cmd);
		out.flush();
		line = in.readLine();
		log(S, line);
		if (line.startsWith("-")) // "-"是服务器的出错返回前缀
			throw new EmailException(line);

		cmd = "pass " + password + CRLF;
		log(C, cmd);
		out.write(cmd);
		out.flush();
		line = in.readLine();
		log(S, line);
		if (line.startsWith("-"))// "-"是服务器的出错返回前缀
			throw new EmailException(line);
	}

	public int getMsgCount() throws IOException, EmailException {
		int result = 0;

		String cmd = "stat" + CRLF;
		log(C, cmd);
		out.write(cmd);
		out.flush();
		String line = in.readLine();
		log(S, line);
		if (line.startsWith("-"))// "-"是服务器的出错返回前缀
			throw new EmailException(line);

		Matcher m = Pattern.compile("\\b([1-9]\\d*)\\b").matcher(line);
		if (m.find())
			result = Integer.parseInt(m.group(1));

		return result;
	}

	public Message getMsg(int index) throws EmailException {
		Message msg = new Message();
		try {
			String cmd;

			// 发送取回命令
			cmd = "retr " + index + CRLF;
			log(C, cmd);
			out.write(cmd);
			out.flush();

			String line;

			// 先读第一行回应，确定是否有指定的message
			line = in.readLine();
			log(S, line);
			if (line.startsWith("-"))// "-"是服务器的出错返回前缀
				throw new EmailException(line);

			StringBuffer contentBuf = new StringBuffer();
			StringBuffer headerBuf = new StringBuffer();

			boolean isHeader = true;
			while (!(line = in.readLine()).equals(".")) {
				log(S, line);
				if (isHeader) {
					if (line.equals(""))
						// 遇到邮件头与正文间的空行，设置标记变量isHeader为假，表示正文开始，头部结束
						isHeader = false;

					// 遇到新的header条目或者头部结束
					Matcher m = Pattern.compile("([a-z|A-Z|-]+):\\s(.+)")
							.matcher(line);
					if (m.find() || line.equals("")) {
						// 先将前一个条目的内容放进msg相应域变量
						{
							String entry = headerBuf.toString();
							Matcher mEntry = Pattern.compile(
									"([a-z|A-Z|-]+):\\s(.+)").matcher(entry);

							// 头部第一行会出现这种情况
							if (!mEntry.find()) {
								headerBuf.append(line.trim());
								continue;
							}

							String entryName = mEntry.group(1);
							String entryValue = mEntry.group(2);

							if (entryName.equalsIgnoreCase("message-id"))
								// 去掉message-id: 以及<>
								msg.id = entryValue.substring(1, entryValue
										.length() - 1);
							else if (entryName.equalsIgnoreCase("from")) {
								// 去掉from:
								msg.from = cplxStringDecode(entryValue);
							} else if (entryName.equalsIgnoreCase("to")) {
								// 去掉to:
								msg.to = cplxStringDecode(entryValue);
							} else if (entryName.equalsIgnoreCase("date"))
								// 去掉date:
								msg.date = entryValue;
							else if (entryName.equalsIgnoreCase("subject")) {
								// 去掉subject:
								msg.subject = cplxStringDecode(entryValue);
							} else if (entryName
									.equalsIgnoreCase("content-type"))
								// 去掉content-type:
								msg.contentType = entryValue;
							else if (entryName
									.equalsIgnoreCase("content-transfer-encoding"))
								// 去掉content-transfer-encoding:
								msg.contentTransferEncoding = entryValue;
						} // / 完成将原先条目的内容放进msg相应域变量

						// 清除headerBuf中的内容
						headerBuf.delete(0, headerBuf.length());
						// 将新header条目中的第一行加入headerBuf
						headerBuf.append(line);
					} else {
						// 加入一个具有多行的头部条目的后续行
						headerBuf.append(line.trim());
					}
				} else {
					// 正文
					contentBuf.append(line);
				}
			}
			log(S, line);

			// 正文是纯文本，目前只支持纯文本
			if ("text/plain".equalsIgnoreCase(msg.contentType.substring(0,
					msg.contentType.indexOf(";")))) {
				if ("base64".equalsIgnoreCase(msg.contentTransferEncoding)) {
					// 获得正文编码
					String cEncode = msg.contentType.substring(msg.contentType
							.indexOf("charset=") + 8);

					// Android不支持GBK，使用GB2312代替
					if ("GBK".equalsIgnoreCase(cEncode))
						cEncode = "GB2312";

					// Base64解码
					msg.content = new String(Base64Coder.decode(contentBuf
							.toString()), cEncode);
				}
			} else {
				msg.content = contentBuf.toString();
			}

		} catch (IOException e) {
			Log.e("XYZPIM", e.getMessage());
		}

		return msg;
	}

	public List<Message> getAllMsg() throws IOException, EmailException {
		List<Message> msgs = new ArrayList<Message>();
		int msgCount = getMsgCount();
		for (int index = 1; index <= msgCount; ++index) {
			msgs.add(getMsg(index));
		}
		return msgs;
	}

	@Override
	public void close() throws IOException {
		String cmd = "quit" + CRLF;
		out.write(cmd);
		log(C, cmd);

		// in.close();
		// out.close();
		pop3Socket.close();
	}

	/**
	 * 解析诸如“=?GB2312?B?xPHIyw==?=”的字符串 主要出现在To: From: Subject: 字段
	 */
	private static String cplxStringDecode(String cplxString) {
		// 找出这种类型的字符串
		Matcher m = Pattern.compile("=\\?[A-Z|a-z|0-9|=|+|/|\\-|\\?]*\\?=")
				.matcher(cplxString);
		while (m.find()) {
			String entry = m.group();

			String encode = null;
			String content = null;

			// 找出字符集编码
			Matcher mEncode = Pattern.compile(
					"=\\?([A-Z|a-z|0-9|=|+|/|\\-|\\?]*)\\?B\\?").matcher(entry);
			if (mEncode.find()) {
				encode = mEncode.group(1); // "=?"与"?="之间的字串
				// encode = encode.substring(2, encode.length() - 3);
			}

			// 找出被BASE64编码的内容
			Matcher mContent = Pattern.compile(
					"\\?B\\?([A-Z|a-z|0-9|=|+|/|\\-|\\?]*)\\?=").matcher(entry);
			if (mContent.find()) {
				content = mContent.group(1); // "?B?"与"?="之间的字串
				// content = content.substring(3, content.length() - 2);
				try {
					if (null != encode) {

						// Android不支持GBK
						if ("GBK".equalsIgnoreCase(encode))
							encode = "GB2312";

						// BASE64解码
						content = new String(Base64Coder.decode(content),
								encode);
					}

				} catch (UnsupportedEncodingException e) {
					Log.e("XYZPIM", e.getMessage());
				}
			}

			// 最后替换为解析出的正常字符串
			cplxString = cplxString.replace(entry, content);
		}
		return cplxString;
	}
}
