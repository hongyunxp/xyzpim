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
package cn.edu.nju.software.xyz.pim.email.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.Menu.Item;
import android.widget.EditText;
import cn.edu.nju.software.xyz.pim.R;
import cn.edu.nju.software.xyz.pim.email.Base64Coder;
import cn.edu.nju.software.xyz.pim.email.EmailAccount;
import cn.edu.nju.software.xyz.pim.email.EmailDB;
import cn.edu.nju.software.xyz.pim.email.EmailException;
import cn.edu.nju.software.xyz.pim.email.Message;
import cn.edu.nju.software.xyz.pim.email.SMTPSession;
import cn.edu.nju.software.xyz.pim.util.Log;

/**
 * @author xmx 2008-4-27 下午01:47:21
 * 
 */
public class EmailCompose extends Activity {

	private static final int SEND_M_ID = 0;
	private static final int RETURN_M_ID = 1;

	private EditText SendToText;
	private EditText SubjectText;
	private EditText ContentText;

	private long a_id;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.email_compose);
		SendToText = (EditText) findViewById(R.id.email_compose_to_text);
		SubjectText = (EditText) findViewById(R.id.email_subject_text);
		ContentText = (EditText) findViewById(R.id.email_content_text);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			a_id = extras.getLong(EmailDB.EmailAccountColumns.ID);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, SEND_M_ID, R.string.send);
		menu.add(0, RETURN_M_ID, R.string.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {

		case SEND_M_ID:
			send();
			break;
		case RETURN_M_ID:
			finish();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void send() {
		final ProgressDialog pb = ProgressDialog.show(this, "Sending", "",
				true, false);

		final Handler handler = new Handler();
		final Runnable dissMiss = new Runnable() {
			public void run() {
				pb.dismiss();
				finish();
			}
		};
		Thread sendThread = new Thread(new Runnable() {

			@Override
			public void run() {

				Message msg = new Message();

				// msg.date = "Sat, 26 Apr 2008 22:47:18 +0800";
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat(
						"EEE, dd MMM yyyy HH:mm:ss");
				String msgDate = new String(format.format(date));
				msgDate = msgDate + " +0800";
				msg.date = msgDate;

				msg.from = "<xyzpim@gmail.com>";
				msg.to = "<" + SendToText.getText().toString().trim() + ">";
				msg.subject = SubjectText.getText().toString();

				String content = ContentText.getText().toString();
				String encodedContent = Base64Coder.encodeString(content);
				// System.out.println(buf.toString());
				StringBuilder buf = new StringBuilder();
				for (int index = 0; index < encodedContent.length(); ++index) {
					// if(index)
					buf.append(encodedContent.charAt(index));
					if ((index + 1) % 80 == 0
							|| index == encodedContent.length() - 1) {
						msg.content.rawContent.add(buf.toString());
						buf.delete(0, buf.length());
					}
				}
				msg.content.contentDisposition = "inline";
				msg.content.contentType = "text/plain; charset=us-ascii";
				msg.content.contentTransferEncoding = "base64";
				EmailDB db = EmailDB.getInstance(EmailCompose.this);
				EmailAccount account = db.fetchEmailAccount(a_id);

				SMTPSession s = SMTPSession.getInstance();
				s.isShowLog = true;
				s.host = account.smtpHost;
				s.port = account.smtpPort;
				s.isShowLog = true;
				s.username = account.user;
				s.password = account.password;
				try {
					s.open(true);
					s.sendMsg(msg);
					s.close();
				} catch (EmailException e) {
					Log.e(e.toString());
				}
				db.createEmailMessage(msg, EmailDB.EmailFolder.OUTBOX, a_id);
				handler.post(dissMiss);
			}
		});
		sendThread.start();
	}
}
