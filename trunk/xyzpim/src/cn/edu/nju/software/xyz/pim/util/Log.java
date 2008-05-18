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
package cn.edu.nju.software.xyz.pim.util;

/**
 * @author xmx 2008-4-24 上午10:22:27
 * 
 */
public class Log {
	private final static String tag = "XYZPIM";

	private Log() {

	}

	public static int i(String info) {
		return null == info ? 0 : info(tag, info);
	}

	public static int i(int info) {
		return i(String.valueOf(info));
	}

	public static int i(boolean info) {
		return i(String.valueOf(info));
	}

	public static int i(float info) {
		return i(String.valueOf(info));
	}

	public static int i(double info) {
		return i(String.valueOf(info));
	}

	public static int i(long info) {
		return i(String.valueOf(info));
	}

	public static int i(char info) {
		return i(String.valueOf(info));
	}

	public static int i(char[] info) {
		return i(String.valueOf(info));
	}

	public static int i(Object info) {
		return i(String.valueOf(info));
	}

	public static int e(String info) {
		return null == info ? 0 : err(tag, info);
	}

	public static int e(int info) {
		return i(String.valueOf(info));
	}

	public static int e(boolean info) {
		return i(String.valueOf(info));
	}

	public static int e(float info) {
		return i(String.valueOf(info));
	}

	public static int e(double info) {
		return i(String.valueOf(info));
	}

	public static int e(long info) {
		return i(String.valueOf(info));
	}

	public static int e(char info) {
		return i(String.valueOf(info));
	}

	public static int e(char[] info) {
		return i(String.valueOf(info));
	}

	public static int e(Object info) {
		return i(String.valueOf(info));
	}

	private static int info(String tag, String info) {
		int length = info.length();
		int index = 0;
		while (index < length) {
			android.util.Log.i(tag, info.substring(index));
			index += 100;
		}
		return 1;
	}

	private static int err(String tag, String info) {
		int length = info.length();
		int index = 0;
		while (index < length) {
			android.util.Log.e(tag, info.substring(index));
			index += 100;
		}
		return 1;
	}
}
