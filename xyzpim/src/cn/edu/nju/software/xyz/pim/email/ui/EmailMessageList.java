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

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.edu.nju.software.xyz.pim.R;
import cn.edu.nju.software.xyz.pim.email.EmailAccount;
import cn.edu.nju.software.xyz.pim.email.EmailDB;
import cn.edu.nju.software.xyz.pim.email.EmailException;
import cn.edu.nju.software.xyz.pim.email.Message;
import cn.edu.nju.software.xyz.pim.email.POP3Session;
import cn.edu.nju.software.xyz.pim.util.Log;

/**
 * @author xmx 2008-4-27 下午04:23:28
 * 
 */
public class EmailMessageList extends ListActivity {

	private static final int REFRESH_M_ID = 0;
	private static final int OPEN_M_ID = 1;
	private static final int DEL_M_ID = 2;
	private static final int RETURN_M_ID = 3;

	private List<Message> messages;
	private List<String> messageSubjects;

	private Long a_id;
	private String folder;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.email_message_list);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			a_id = extras.getLong(EmailDB.EmailAccountColumns.ID);
			folder = extras.getString(EmailDB.EmailMessageColumns.FOLDER);
		}

		fillData();

	}

	private void fillData() {
		messages = EmailDB.getInstance(this).fetchEmailMessages(a_id, folder);
		int count = messages.size();
		messageSubjects = new ArrayList<String>(count);
		for (int index = 0; index < count; ++index) {
			messageSubjects.add(messages.get(index).subject);
		}
		ArrayAdapter<String> messageSubjectAdp = new ArrayAdapter<String>(this,
				R.layout.email_message_row, messageSubjects);
		setListAdapter(messageSubjectAdp);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		openMessage(position);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (EmailDB.EmailFolder.INBOX.equals(folder))
			menu.add(0, REFRESH_M_ID, R.string.refresh);
		menu.add(0, OPEN_M_ID, R.string.open);
		menu.add(0, DEL_M_ID, R.string.del);
		menu.add(0, RETURN_M_ID, R.string.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {

		case REFRESH_M_ID:
			refresh();
			// fillData();
			break;
		case OPEN_M_ID:
			openMessage(getSelectedItemPosition());
			break;
		case DEL_M_ID:
			delMessage(getSelectedItemPosition());
			// refresh();
			break;
		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void refresh() {
		final ProgressDialog pb = ProgressDialog.show(this, "Refreshing", "",
				true, false);

		final Handler handler = new Handler();
		final Runnable dissMiss = new Runnable() {
			public void run() {
				ArrayAdapter<String> messageSubjectAdp = new ArrayAdapter<String>(
						EmailMessageList.this, R.layout.email_message_row,
						messageSubjects);
				setListAdapter(messageSubjectAdp);
				pb.dismiss();
				fillData();
			}
		};

		Thread refreshThread = new Thread(new Runnable() {

			@Override
			public void run() {
				EmailDB db = EmailDB.getInstance(EmailMessageList.this);
				EmailAccount ea = db.fetchEmailAccount(a_id);
				// db.deletEmailMessageByAccount(a_id);
				POP3Session ps = POP3Session.getInstance();
				ps.isShowLog = true;
				ps.host = ea.popHost;
				ps.port = ea.popPort;
				ps.username = ea.user;
				ps.password = ea.password;
				try {
					ps.open(ea.isSSL);
					// messages = ps.getAllMsg();
					// 开始收新邮件
					int count = ps.getMsgCount();
					for (int index = 1; index <= count; ++index) {
						if (db.isNewMessage(ps.getMsgUID(index))) {
							Message msg = ps.getMsg(index);
							messages.add(msg);
							db.createEmailMessage(msg, folder, a_id);
						}
					}
					// 结束收新邮件
					ps.close();
				} catch (EmailException e) {
					Log.e(e.getMessage());
				}
				handler.post(dissMiss);
			}
		});
		refreshThread.start();
	}

	private void openMessage(int position) {
		Message msg = messages.get(position);
		Intent openIntent = new Intent(this,
				cn.edu.nju.software.xyz.pim.email.ui.EmailReadView.class);
		openIntent.putExtra(EmailDB.EmailMessageColumns.ID, msg.id);
		startSubActivity(openIntent, 0);
	}

	private void delMessage(int position) {

		final Message msg = messages.get(position);

		final ProgressDialog pb = ProgressDialog.show(this, "Deleting", "",
				true, false);

		final Handler handler = new Handler();
		final Runnable dissMiss = new Runnable() {
			public void run() {
				EmailDB db = EmailDB.getInstance(EmailMessageList.this);
				db.deleteEmailMessage(msg.id);
				ArrayAdapter<String> messageSubjectAdp = new ArrayAdapter<String>(
						EmailMessageList.this, R.layout.email_message_row,
						messageSubjects);
				setListAdapter(messageSubjectAdp);
				pb.dismiss();
				fillData();
			}
		};

		final Runnable dissMissFailed = new Runnable() {
			public void run() {
				ArrayAdapter<String> messageSubjectAdp = new ArrayAdapter<String>(
						EmailMessageList.this, R.layout.email_message_row,
						messageSubjects);
				setListAdapter(messageSubjectAdp);
				pb.dismiss();
				fillData();
				alert("Network Failed!");
			}
		};

		Thread delThread = new Thread(new Runnable() {

			@Override
			public void run() {
				EmailDB db = EmailDB.getInstance(EmailMessageList.this);
				EmailAccount ea = db.fetchEmailAccount(a_id);
				POP3Session ps = POP3Session.getInstance();
				ps.isShowLog = true;
				ps.host = ea.popHost;
				ps.port = ea.popPort;
				ps.username = ea.user;
				ps.password = ea.password;
				try {
					ps.open(ea.isSSL);
					ps.delMsg(msg.uid);
					ps.close();
					handler.post(dissMiss);
				} catch (EmailException e) {
					Log.e(e.getMessage());
					handler.post(dissMissFailed);
				}
			}
		});
		delThread.start();
	}

	private void alert(String info) {
		new AlertDialog.Builder(this).setTitle(info).setPositiveButton(
				R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						/* User clicked Yes so do some stuff */
					}
				}).setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						/* User clicked No so do some stuff */
					}
				}).show();
	}
}
