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
package cn.edu.nju.software.xyz.pim.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.edu.nju.software.xyz.pim.R;
import cn.edu.nju.software.xyz.pim.email.Message;
import cn.edu.nju.software.xyz.pim.email.POP3Session;

/**
 * @author xmx 2008-3-18 下午10:39:33
 * 
 */
public class Test extends Activity implements OnClickListener {

	private TextView subjectText;
	private TextView reciverText;
	private TextView senderText;
	private TextView dateText;
	private TextView contentText;

	@Override
	protected void onCreate(Bundle icicle) {
		setContentView(R.layout.test);
		super.onCreate(icicle);

		subjectText = (TextView) findViewById(R.id.mail_subject);
		reciverText = (TextView) findViewById(R.id.mail_reciver);
		senderText = (TextView) findViewById(R.id.mail_sender);
		dateText = (TextView) findViewById(R.id.mail_date);
		contentText = (TextView) findViewById(R.id.mail_content);

		POP3Session s = POP3Session.getInstance();
		s.host = "pop3.163.com";
		s.port = 110;
		s.username = "xmxsuperstar";
		s.password = "test123456";
		s.open();
		Message m = s.getMsg(1);
		s.close();

		subjectText.setText(m.subject);
		reciverText.setText(m.to);
		senderText.setText(m.from);
		dateText.setText(m.date);
		contentText.setText(m.content);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
