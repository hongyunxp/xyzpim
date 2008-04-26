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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLSocketFactory;

/**
 * @author xmx 2008-4-14 下午07:57:37
 * 
 */
public class SMTPSession extends Session {
	private Socket smtpSocket;
	private BufferedReader in;
	private BufferedWriter out;

	private static SMTPSession ins;

	private SMTPSession() {
	}

	// Singleton!!
	public static SMTPSession getInstance() {
		if (null == ins)
			ins = new SMTPSession();
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
				smtpSocket = new Socket(host, port);
			else {
				// 打开SSL连接，gmail必须！！！
				SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory
						.getDefault();
				smtpSocket = factory.createSocket(host, port);
			}
			in = new BufferedReader(new InputStreamReader(smtpSocket
					.getInputStream()));

			out = new BufferedWriter(new OutputStreamWriter(smtpSocket
					.getOutputStream()));

			String cmd; // 发给服务器的命令

			log(S, in.readLine());

			cmd = "ehlo " + username;
			sendCMD(cmd);

			cmd = "auth login";
			sendCMD(cmd);

			cmd = Base64Coder.encodeString(username);
			sendCMD(cmd);

			cmd = Base64Coder.encodeString(password);
			sendCMD(cmd);

		} catch (UnknownHostException e) {
			throw new EmailException(e.getMessage());
		} catch (IOException e) {
			throw new EmailException(e.getMessage());
		}
	}

	public void sendMsg(Message msg) throws EmailException {
		String cmd;
		cmd = "mail from:" + msg.from;
		sendCMD(cmd);

		List<String> rcpts = getRcptList(msg.to);
		for (int index = 0; index < rcpts.size(); ++index) {
			cmd = "rcpt to:" + rcpts.get(index);
			sendCMD(cmd);
		}

		cmd = "data";
		sendCMD(cmd);

		try {
			sendData("Date: " + msg.date);
			sendData("From: " + msg.from);
			sendData("Subject: " + msg.subject);
			sendData("To: " + msg.to);
			sendData("Mime-Version: 1.0");
			sendData("Content-Type: " + msg.content.contentType);
			sendData("Content-Transfer-Encoding: "
					+ msg.content.contentTransferEncoding);
			sendData("Content-Disposition: " + msg.content.contentDisposition);
			sendData("");
			out.flush();

			int length = msg.content.rawContent.size();
			for (int pos = 0; pos < length; ++pos) {
				sendData(msg.content.rawContent.get(pos));
			}
			out.flush();
			sendCMD(".");

		} catch (IOException e) {
			throw new EmailException(e.getMessage());
		}

	}

	@Override
	public void close() throws EmailException {
		try {
			smtpSocket.close();
		} catch (IOException e) {
			throw new EmailException(e.getMessage());
		}

	}

	private void sendCMD(String cmd) throws EmailException {
		try {
			log(C, cmd);
			out.write(cmd + CRLF);
			out.flush();
			String line;
			StringBuilder buf = new StringBuilder();
			String reg = "(\\d{3})([\\s|\\-])(.*)";
			int resCode;
			while (true) {
				line = in.readLine();
				log(S, line);
				Matcher m = Pattern.compile(reg).matcher(line);
				if (m.find()) {
					if (m.group(2).equals("-")) {// 有多行回应
						buf.append(line + CRLF);
						continue;
					} else {
						buf.append(line + CRLF);
						resCode = Integer.parseInt(m.group(1));
						break;
					}
				}
			}
			if (resCode >= 400)
				throw new EmailException(buf.toString());
		} catch (UnknownHostException e) {
			throw new EmailException(e.getMessage());
		} catch (IOException e) {
			throw new EmailException(e.getMessage());
		}
	}

	private void sendData(String cmd) throws EmailException {
		try {
			log(C, cmd);
			out.write(cmd + CRLF);
		} catch (UnknownHostException e) {
			throw new EmailException(e.getMessage());
		} catch (IOException e) {
			throw new EmailException(e.getMessage());
		}
	}

	private List<String> getRcptList(String to) {
		String reg = "<(\\w+)@(\\w+)((\\.\\w+)+)>";
		Matcher m = Pattern.compile(reg).matcher(to);
		List<String> rcpts = new ArrayList<String>();
		while (m.find()) {
			rcpts.add(m.group());
		}
		return rcpts;
	}
}
