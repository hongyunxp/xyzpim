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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLSocketFactory;

import android.util.Log;

/**
 * @author xmx 2008-4-14 下午08:03:46
 * 
 */
public class POP3Session extends Session {
	public boolean isShowLog = true;

	private String C = "C: ";
	private String S = "S: ";

	private Socket pop3Socket;
	private BufferedReader in;
	private BufferedWriter out;

	private static POP3Session ins;

	private POP3Session() {
	}

	public static POP3Session getInstance() {
		if (null == ins)
			ins = new POP3Session();
		return ins;
	}

	@Override
	public void open() {
		open(false);
	}

	public void open(boolean isSSL) {
		if (!isSSL)
			try {
				pop3Socket = new Socket(host, port);
			} catch (UnknownHostException e) {
				Log.e("XYZPIM", e.getMessage());
			} catch (IOException e) {
				Log.e("XYZPIM", e.getMessage());
			}
		else {
			try {
				SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory
						.getDefault();
				pop3Socket = factory.createSocket(host, port);
			} catch (UnknownHostException e) {
				Log.e("XYZPIM", e.getMessage());
			} catch (IOException e) {
				Log.e("XYZPIM", e.getMessage());
			}

		}

		try {
			in = new BufferedReader(new InputStreamReader(pop3Socket
					.getInputStream()));

			out = new BufferedWriter(new OutputStreamWriter(pop3Socket
					.getOutputStream()));

			String cmd;

			log(S, in.readLine());

			cmd = "user " + username + CRLF;
			log(C, cmd);
			out.write(cmd);
			out.flush();
			log(S, in.readLine());

			cmd = "pass " + password + CRLF;
			log(C, cmd);
			out.write(cmd);
			out.flush();
			log(S, in.readLine());
		} catch (IOException e) {
			Log.e("XYZPIM", e.getMessage());
		}
	}

	public Message getMsg(int index) {
		Message msg = new Message();
		try {
			String cmd;

			// 发送取回命令
			cmd = "retr " + index + CRLF;
			log(C, cmd);
			out.write(cmd);
			out.flush();

			String line;
			StringBuffer contentBuf = new StringBuffer();

			boolean isHeader = true;
			while (!(line = in.readLine()).equals(".")) {
				log(S, line);
				if (isHeader) {
					if (line.toLowerCase().startsWith("message-id: "))
						// 去掉message-id: 以及<>
						msg.id = line.substring(13, line.length() - 1);
					else if (line.toLowerCase().startsWith("from: ")) {
						// 去掉from:
						String fromText = line.substring(6).trim();
						msg.from = cplxStringDecode(fromText);
						// 还有发件人
						while (fromText.endsWith(",")) {
							line = in.readLine();
							log(S, line);
							fromText = line.trim();
							msg.from = msg.from + cplxStringDecode(fromText);
						}
					} else if (line.toLowerCase().startsWith("to: ")) {
						// 去掉to:
						String toText = line.substring(4).trim();
						msg.to = cplxStringDecode(toText);
						// 还有发件人
						while (toText.endsWith(",")) {
							line = in.readLine();
							log(S, line);
							toText = line.trim();
							msg.to = msg.to + cplxStringDecode(toText);
						}
					} else if (line.toLowerCase().startsWith("date: "))
						// 去掉date:
						msg.date = line.substring(6);
					else if (line.toLowerCase().startsWith("subject: ")) {
						// 去掉subject:
						String subText = line.substring(9);
						msg.subject = cplxStringDecode(subText);
					} else if (line.toLowerCase().startsWith("content-type: "))
						// 去掉content-type:
						msg.contentType = line.substring(14);
					else if (line.toLowerCase().startsWith(
							"content-transfer-encoding: "))
						// 去掉content-transfer-encoding:
						msg.contentTransferEncoding = line.substring(27);
					else if (line.equals(""))
						isHeader = false;
				} else {
					contentBuf.append(line);
				}
			}
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
			}

		} catch (IOException e) {
			Log.e("XYZPIM", e.getMessage());
		}

		return msg;
	}

	@Override
	public void close() {
		try {
			String cmd = "quit" + CRLF;
			out.write(cmd);
			log(C, cmd);
			pop3Socket.close();
		} catch (IOException e) {

		}

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
				encode = mEncode.group(1);
				// encode = encode.substring(2, encode.length() - 3);
			}

			// 找出被BASE64编码的内容
			Matcher mContent = Pattern.compile(
					"\\?B\\?([A-Z|a-z|0-9|=|+|/|\\-|\\?]*)\\?=").matcher(entry);
			if (mContent.find()) {
				content = mContent.group(1);
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

	private void log(String CS, String info) {
		if (isShowLog)
			Log.i("XYZPIM", CS + info);
	}

}
