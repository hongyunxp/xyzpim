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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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
		List<String> messageSubjects = new ArrayList<String>(count);
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
			fillData();
			break;
		case OPEN_M_ID:
			openMessage(getSelectedItemPosition());
			break;
		case DEL_M_ID:
			break;
		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void refresh() {
		EmailDB db = EmailDB.getInstance(this);
		EmailAccount ea = db.fetchEmailAccount(a_id);
		db.deletEmailMessageByAccount(a_id);
		POP3Session ps = POP3Session.getInstance();
		ps.isShowLog = true;
		ps.host = ea.popHost;
		ps.port = ea.popPort;
		ps.username = ea.user;
		ps.password = ea.password;
		try {
			ps.open(ea.isSSL);
			messages = ps.getAllMsg();
			ps.close();
		} catch (EmailException e) {
			Log.e(e.getMessage());
		}
		int count = messages.size();
		for (int index = 0; index < count; ++index) {
			db.createEmailMessage(messages.get(index), folder, a_id);
		}
	}

	private void openMessage(int position) {
		Message msg = messages.get(position);
		Intent openIntent = new Intent(this,
				cn.edu.nju.software.xyz.pim.email.ui.EmailReadView.class);
		openIntent.putExtra(EmailDB.EmailMessageColumns.ID, msg.id);
		startSubActivity(openIntent, 0);
	}
}
