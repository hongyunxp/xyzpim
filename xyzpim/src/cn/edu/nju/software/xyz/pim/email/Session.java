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

import android.util.Log;

/**
 * @author xmx 2008-4-14 下午07:57:26
 * 
 */
public abstract class Session {
	public boolean isShowLog = false;

	protected String C = "C: ";
	protected String S = "S: ";

	public String host;
	public int port;
	public String username;
	public String password;

	protected static String CRLF = "\r\n";

	public abstract void open() throws EmailException;

	public abstract void open(boolean isSSL) throws EmailException;

	public abstract void close() throws EmailException;

	// 测试用，显示客户端和服务器的详细的会话过程
	protected void log(String CS, String info) {
		if (isShowLog)
			Log.i("XYZPIM", CS + info);
	}
}
