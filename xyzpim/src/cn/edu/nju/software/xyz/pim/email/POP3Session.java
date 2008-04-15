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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

/**
 * @author xmx 2008-4-14 下午08:03:46
 * 
 */
public class POP3Session extends Session {
	public boolean isShowLog = false;

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
		try {
			pop3Socket = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(pop3Socket
					.getInputStream()));
			// Creat input stream for pop3socket

			out = new BufferedWriter(new OutputStreamWriter(pop3Socket
					.getOutputStream()));
			// Creat output stream for po3socket
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
		} catch (UnknownHostException e) {
			Log.e("XYZPIM", e.getMessage());
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
						msg.id = line.substring(13, line.length() - 1);
					else if (line.toLowerCase().startsWith("from:")) {
						String fromText = line.substring(6);
						String fromEncode = fromText.substring(fromText
								.indexOf("=?") + 2, fromText.indexOf("?B?"));
						String fromName = fromText.substring(fromText
								.indexOf("?B?") + 3, fromText.indexOf("?="));
						fromName = new String(Base64Coder.decode(fromName),
								fromEncode);
						String fromAddr = fromText.substring(fromText
								.indexOf("?=") + 4);
						msg.from = fromName + " " + fromAddr;
					}

					else if (line.toLowerCase().startsWith("to:"))
						msg.to = line.substring(4);
					else if (line.toLowerCase().startsWith("date: "))
						msg.date = line.substring(6);
					else if (line.toLowerCase().startsWith("subject: ")) {
						String subText = line.substring(9);
						String subEncode = subText.substring(subText
								.indexOf("=?") + 2, subText.indexOf("?B?"));
						String sub = subText.substring(
								subText.indexOf("?B?") + 3, subText
										.indexOf("?="));
						msg.subject = new String(Base64Coder.decode(sub),
								subEncode);
					} else if (line.toLowerCase().startsWith("content-type: "))
						msg.contentType = line.substring(14);
					else if (line.equals(""))
						isHeader = false;
				} else {
					contentBuf.append(line + "\n");
				}
				msg.content = contentBuf.toString();
			}
		} catch (IOException e) {
			Log.e("XYZPIM", e.getMessage());
		}

		return msg;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	/**
	 * 解析诸如“=?GB2312?B?xPHIyw==?=”的字符串
	 */
	private String cplxStringDecode(String cplxString) {
		String reg = "=\\?[0-9|a-z|A-Z]*\\?=";
		String regEncode = "=??B?";
		String regContent = "?B??=";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(cplxString);
		if (!m.find()) // 不是复杂字符
			return cplxString;
		for (int index = 0; index < m.groupCount(); ++index) {
			Log.i("XYZPIM", m.group(index));
		}
		return null;
	}

	private void log(String CS, String info) {
		if (isShowLog)
			Log.i("XYZPIM", CS + info);
	}

}
