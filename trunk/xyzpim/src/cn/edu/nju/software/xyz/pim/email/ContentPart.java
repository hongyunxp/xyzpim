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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zxs 2008-4-19 上午01:38:43
 *         这个类代表email中一个符合mime定义的content，这个类是为了处理复杂格式的message
 */
public class ContentPart {
	public String contentType;
	public String contentTransferEncoding;
	public String contentDisposition;
	public List<String> rawContent = new ArrayList<String>(); // 从服务器传来的未经解析的本content

	// part的内容，包含子content
	// parts

	/**
	 * 当contentType中指定mime类型为text/html, text/plain该方法有效
	 * 
	 * @return 本content part中的解析后的文本，不包含子content part
	 */
	public String getContentString() {
		String mime = getMIMEType();
		String charset = getCharSetName();
		StringBuilder buf = new StringBuilder();
		String re = null;

		if ("text/html".equalsIgnoreCase(mime)
				|| "text/plain".equalsIgnoreCase(mime)) {
			if (charset == null)
				charset = "GB2312";
			if (contentTransferEncoding == null)
				contentTransferEncoding = "base64";
			for (int index = 0; index < rawContent.size(); ++index) {
				String line = rawContent.get(index);
				if ("base64".equalsIgnoreCase(contentTransferEncoding))
					buf.append(line);
				else if ("quoted-printable"
						.equalsIgnoreCase(contentTransferEncoding))
					buf.append(line.substring(0, line.length() - 1));
			}
			if ("base64".equalsIgnoreCase(contentTransferEncoding))
				re = Base64Coder.decodeString(buf.toString());
			else if ("quoted-printable"
					.equalsIgnoreCase(contentTransferEncoding))
				re = QuotedPrintableCoder.decodeString(buf.toString());
		} else {

		}
		return re;
	}

	/**
	 * 
	 * @return 本conent part的MIME类型
	 */
	public String getMIMEType() {
		String reg = "[a-z|A-Z]*/[a-z|A-Z]*";
		Matcher m = Pattern.compile(reg).matcher(contentType);
		if (m.find())
			return m.group();
		else
			return null;
	}

	/**
	 * 当contentType中指定mime类型为text/html, text/plain该方法有效
	 * 
	 * @return 本conent part中文本的字符集
	 */
	public String getCharSetName() {
		String reg = "\\scharset=([a-z|A-Z|0-9|\\-]*)";
		Matcher m = Pattern.compile(reg).matcher(contentType);
		if (m.find())
			return m.group(1);
		else
			return null;
	}

	/**
	 * 当contentType中指定mime类型为 multipart/mixed, multipart/alternative该方法有效
	 * 
	 * @return boundary
	 */
	public String getBoundaryString() {
		String reg = "\\sboundary=\"(.*)\"";
		Matcher m = Pattern.compile(reg).matcher(contentType);
		if (m.find())
			return m.group(1);
		else
			return null;
	}

	/**
	 * 判断是否有子ContentPart
	 * 
	 * @return
	 */
	public boolean hasSubContentPart() {
		String mime = getMIMEType();
		if ("multipart/mixed".equalsIgnoreCase(mime)
				|| "multipart/alternative".equalsIgnoreCase(mime))
			return true;
		else
			return false;
	}

	/**
	 * 当contentType中指定mime类型为 multipart/mixed, multipart/alternative该方法有效
	 * 这个方法是一个相当消耗资源的方法，当慎用！
	 * 
	 * @return sub content part
	 */
	public List<ContentPart> getSubContentPart() {
		List<ContentPart> subCPList = null;
		if (hasSubContentPart()) {
			subCPList = new ArrayList<ContentPart>();
			int lineCount = rawContent.size();
			String line = null;
			StringBuilder buf = new StringBuilder();
			// boolean inNewSubCP = false;
			boolean inHeader = false;
			ContentPart subCP = null;
			for (int index = 0; index < lineCount; ++index) {
				line = rawContent.get(index);
				/*System.out.println(line.trim());
				System.out.println(getBoundaryString());*/
				if (line.trim().equals("--" + getBoundaryString())) {
					if (null != subCP)
						subCPList.add(subCP);
					subCP = new ContentPart();
					inHeader = true;
					continue;
				} else if (line.trim()
						.equals("--" + getBoundaryString() + "--")) {
					subCPList.add(subCP);
					break;
				} else if (inHeader) {
					if ("".equals(line.trim()))
						inHeader = false;
					{
						Matcher m = Pattern.compile("([a-z|A-Z|-]+):\\s(.+)")
								.matcher(line);
						if (m.find() || "".equals(line.trim())) {
							{
								String entry = buf.toString();
								Matcher mEntry = Pattern.compile(
										"([a-z|A-Z|-]+):\\s(.+)")
										.matcher(entry);

								// 第一行会出现这种情况
								if (!mEntry.find()) {
									buf.append(line.trim());
									continue;
								}

								String entryName = mEntry.group(1);
								String entryValue = mEntry.group(2);

								if (entryName.equalsIgnoreCase("content-type"))
									subCP.contentType = entryValue;
								else if (entryName
										.equalsIgnoreCase("content-transfer-encoding"))
									subCP.contentTransferEncoding = entryValue;
								else if (entryName
										.equalsIgnoreCase("content-disposition"))
									subCP.contentDisposition = entryValue;
							}
							buf.delete(0, buf.length());
							buf.append(line);
						} else {
							buf.append(" " + line.trim());
						}
					}
				} else if (!inHeader) {
					subCP.rawContent.add(line);
				}
			}
		}
		return subCPList;
	}

	/**
	 * 用于附件
	 * 
	 * @return
	 */
	public byte[] getContentByteArray() {
		byte[] re = null;
		StringBuilder buf = new StringBuilder();
		if (contentDisposition.indexOf("") > 0) {
			for (int index = 0; index < rawContent.size(); ++index) {
				String line = rawContent.get(index);
				if ("base64".equalsIgnoreCase(contentTransferEncoding))
					buf.append(line);
				else if ("quoted-printable"
						.equalsIgnoreCase(contentTransferEncoding))
					buf.append(line.substring(0, line.length() - 1));
			}
			if ("base64".equalsIgnoreCase(contentTransferEncoding))
				re = Base64Coder.decode(buf.toString());
			else if ("quoted-printable"
					.equalsIgnoreCase(contentTransferEncoding))
				re = QuotedPrintableCoder.decode(buf.toString());
		}

		return re;
	}

}
