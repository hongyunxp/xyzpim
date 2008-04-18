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

/**
 * @author zxs 2008-4-19 上午01:38:43
 *         这个类代表email中一个符合mime定义的content，这个类是为了处理复杂格式的message
 */
public class ContentPart {
	public String contentType;
	public String contentTransferEncoding;
	public String rawContent; // 从服务器传来的未经解析的本content part的内容，包含子content parts

	/**
	 * 当contentType中指定mime类型为text/html, text/plain该方法有效
	 * 
	 * @return 本content part中的解析后的文本，不包含子content part
	 */
	public String getContentString() {
		return null;
	}

	/**
	 * 
	 * @return 本conent part的MIME类型
	 */
	public String getMIMEType() {
		return null;
	}

	/**
	 * 当contentType中指定mime类型为text/html, text/plain该方法有效
	 * 
	 * @return 本conent part中文本的字符集
	 */
	public String getCharSetName() {
		return null;
	}

	/**
	 * 当contentType中指定mime类型为 multipart/mixed, multipart/alternative该方法有效
	 * 
	 * @return boundary
	 */
	public String getBoundaryString() {
		return null;
	}

	/**
	 * 当contentType中指定mime类型为 multipart/mixed, multipart/alternative该方法有效
	 * 这个方法是一个相当消耗资源的方法，当慎用！
	 * 
	 * @return sub content part
	 */
	public ContentPart[] getSubContentPart() {
		return null;
	}

	/**
	 * 用于附件
	 * 
	 * @return
	 */
	public byte[] getContentByteArray() {
		return null;
	}

}
