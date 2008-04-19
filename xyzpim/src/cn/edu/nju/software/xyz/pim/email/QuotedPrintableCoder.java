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

import java.io.ByteArrayOutputStream;

/**
 * @author xmx 2008-4-19 下午04:55:49 注意：
 *         这个类编码的时候不会自动断行也不会自动加上行末的"=\r\n"，解码的时候也认为行末的"=\r\n"已经都被去掉
 */
public class QuotedPrintableCoder {

	public static char[] encode(byte[] in) {
		return encodeImp(in).toCharArray();
	}

	public static String encodeString(String in) {
		return encodeImp(in.getBytes());
	}

	private static String encodeImp(byte[] in) {
		if (null == in)
			return null;
		StringBuffer outBuf = new StringBuffer();
		for (int index = 0; index < in.length; ++index) {
			byte b = in[index];
			// ASCII 33-60, 62-126原样输出
			if (b >= '!' && b <= '~' && b != '=') {
				outBuf.append((char) b);
			} else {
				// 编码
				outBuf.append('=');
				String s = Integer.toHexString(b & 0xFF).toUpperCase();
				if (s.length() % 2 != 0) {
					// Pad with 0
					s = "0" + s;
				}
				outBuf.append(s);
			}
		}
		return outBuf.toString();
	}

	public static byte[] decode(char[] in) {
		if (null == in)
			return null;
		ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
		for (int index = 0; index < in.length; ++index) {
			char c = in[index];
			if (c == '=') { // 需要解码
				int u = Character.digit((char) in[++index], 16);
				int l = Character.digit((char) in[++index], 16);
				if (u == -1 || l == -1) {
					continue;
				}
				outBuf.write((char) ((u << 4) + l));
			} else {
				outBuf.write((byte) c); // 直接输出
			}
		}
		return outBuf.toByteArray();
	}

	public static String decodeString(String in) {
		return new String(decode(in.toCharArray()));
	}
}
