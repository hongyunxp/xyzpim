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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xmx 2008-4-21 下午01:19:53
 * 
 */
public class HeaderUtil {
	private HeaderUtil() {
	}

	public static Map<String, String> decodeHeader(List<String> headerLines) {
		Map<String, String> re = new HashMap<String, String>();
		Iterator<String> i = headerLines.iterator();
		String line = null;
		String entryName = null;
		String entryValue = null;
		// StringBuilder entryValueBuf = null;
		while (i.hasNext()) {
			line = i.next();

			Matcher m = Pattern.compile("([a-z|A-Z|-]+):\\s(.+)").matcher(line);
			if (m.find()) {

				if (null != entryName) {
					re.put(entryName, entryValue);
				}

				entryName = m.group(1).toLowerCase();
				entryValue = m.group(2);
			} else {
				entryValue = entryValue + " " + line.trim();
			}
		}

		if (null != entryName) {
			re.put(entryName, entryValue);
		}

		return re;
	}

	/**
	 * 解析诸如“=?GB2312?B?xPHIyw==?=”的字符串 主要出现在To: From: Subject: 字段
	 * 
	 * @throws EmailException
	 */
	public static String cplxStringDecode(String cplxString)
			throws EmailException {
		// 找出这种类型的字符串
		String reg = "=\\?" + "([a-z|A-Z|0-9|\\-]*)" + "\\?" + "([Q|B]{1})"
				+ "\\?" + "([a-z|A-Z|0-9|=|+|/|\\-|\\?]*)" + "\\?=";
		Matcher m = Pattern.compile(reg).matcher(cplxString);
		while (m.find()) {
			if (m.groupCount() == 3) {
				String entry = m.group(0);

				String charset = m.group(1);
				String encode = m.group(2); // base64 or quated-printable
				String content = m.group(3);

				if (charset.toUpperCase().startsWith("GB"))
					charset = "GB2312"; // Android对中文简体字符集只支持GB2312

				try {
					if ("B".equalsIgnoreCase(encode))
						content = new String(Base64Coder.decode(content),
								charset);
					else if ("Q".equals(encode))
						content = new String(QuotedPrintableCoder
								.decode(content), charset);
					else
						throw new EmailException(
								"Unsupported content transfer encoding");
				} catch (UnsupportedEncodingException e) {
					throw new EmailException(e.getMessage());
				}
				// 最后替换为解析出的正常字符串
				cplxString = cplxString.replace(entry, content);
			}
		}
		return cplxString;
	}
}
