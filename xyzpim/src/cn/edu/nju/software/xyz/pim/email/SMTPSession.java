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

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @author xmx 2008-4-14 下午07:57:37
 * 
 */
public class SMTPSession extends Session {

	@Override
	public void open() throws UnknownHostException, IOException, EmailException {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(boolean isSSL) throws UnknownHostException, IOException,
			EmailException {
		// TODO Auto-generated method stub

	}

	public void sendMsg(Message msg) {

	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}
}
