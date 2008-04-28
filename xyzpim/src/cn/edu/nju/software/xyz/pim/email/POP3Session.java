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
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLSocketFactory;

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
	public void open() throws EmailException {
		open(false);
	}

	@Override
	public void open(boolean isSSL) throws EmailException {
		try {
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
		} catch (UnknownHostException e) {
			throw new EmailException(e.getMessage());
		} catch (IOException e) {
			throw new EmailException(e.getMessage());
		}
	}

	public int getMsgCount() throws EmailException {
		int result = 0;

		try {
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
		} catch (IOException e) {
			throw new EmailException(e.getMessage());
		}

		return result;
	}

	public String getMsgUID(int index) throws EmailException {
		String result = null;

		try {
			String cmd = "uidl" + " " + index + CRLF;
			log(C, cmd);
			out.write(cmd);
			out.flush();
			String line = in.readLine();
			log(S, line);
			if (line.startsWith("-"))// "-"是服务器的出错返回前缀
				throw new EmailException(line);
			Matcher m = Pattern.compile("\\+[o|O][k|K]\\s\\d*\\s(.*)").matcher(
					line);
			if (m.find())
				result = m.group(1);
		} catch (IOException e) {
			throw new EmailException(e.getMessage());
		}

		return result;
	}

	/**
	 * 收取指定邮件的头部
	 * 
	 * @param index
	 *            邮件在服务器上的索引
	 * @return 只有头部没有正文的邮件
	 * @throws EmailException
	 */
	public Message getMsgHeader(int index) throws EmailException {
		Message msg = new Message();
		try {
			String cmd;

			// 发送取回命令
			cmd = "top " + index + " 0" + CRLF;
			log(C, cmd);
			out.write(cmd);
			out.flush();

			String line;

			// 先读第一行回应，确定是否有指定的message
			line = in.readLine();
			log(S, line);
			if (line.startsWith("-"))// "-"是服务器的出错返回前缀
				throw new EmailException(line);

			List<String> headerLines = new ArrayList<String>();
			while (!(line = in.readLine()).equals("")) {
				log(S, line);
				headerLines.add(line);
			}

			Map<String, String> headerMap = HeaderUtil
					.decodeHeader(headerLines);

			setMsgHeader(msg, headerMap);

		} catch (IOException e) {
			throw new EmailException(e.getMessage());
		}
		return msg;
	}

	private void setMsgHeader(Message msg, Map<String, String> headerMap)
			throws EmailException {
		String entryValue = null;
		if (null != (entryValue = headerMap.get("from"))) {
			msg.from = HeaderUtil.cplxStringDecode(entryValue);
		}
		if (null != (entryValue = headerMap.get("to"))) {
			msg.to = HeaderUtil.cplxStringDecode(entryValue);
		}
		if (null != (entryValue = headerMap.get("date"))) {
			msg.date = entryValue;
		}
		if (null != (entryValue = headerMap.get("subject"))) {
			msg.subject = HeaderUtil.cplxStringDecode(entryValue);
		}
		if (null != (entryValue = headerMap.get("content-type"))) {
			msg.content.contentType = entryValue;
		}
		if (null != (entryValue = headerMap.get("content-transfer-encoding"))) {
			msg.content.contentTransferEncoding = entryValue;
		}
		if (null != (entryValue = headerMap.get("content-dispotion"))) {
			msg.content.contentDisposition = entryValue;
		}

	}

	/**
	 * 收取制定邮件的头部和正文
	 * 
	 * @param index
	 *            邮件在服务器上的索引
	 * @return 收到的指定的邮件
	 * @throws EmailException
	 */
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

			// boolean isHeader = true;
			List<String> headerLines = new ArrayList<String>();
			while (!(line = in.readLine()).equals("")) {
				log(S, line);
				headerLines.add(line);
			}
			log(S, line);
			while (!(line = in.readLine()).equals(".")) {
				log(S, line);
				msg.content.rawContent.add(line);
			}
			log(S, line);

			Map<String, String> headerMap = HeaderUtil
					.decodeHeader(headerLines);

			setMsgHeader(msg, headerMap);
		} catch (IOException e) {
			throw new EmailException(e.getMessage());
		}

		msg.uid = getMsgUID(index);
		return msg;
	}

	public List<Message> getAllMsg() throws EmailException {
		List<Message> msgs = new ArrayList<Message>();
		int msgCount = getMsgCount();
		for (int index = 1; index <= msgCount; ++index) {
			msgs.add(getMsg(index));
		}
		return msgs;
	}

	@Override
	public void close() throws EmailException {
		try {
			String cmd = "quit" + CRLF;
			out.write(cmd);
			log(C, cmd);

			// in.close();
			// out.close();
			pop3Socket.close();
		} catch (IOException e) {
			throw new EmailException(e.getMessage());
		}

	}

}
