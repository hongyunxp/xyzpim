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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.edu.nju.software.xyz.pim.R;
import cn.edu.nju.software.xyz.pim.email.EmailDB;

/**
 * @author xmx 2008-4-27 下午01:19:44
 * 
 */
public class FolderView extends Activity {

	private static final int COMPOSE_M_ID = 0;
	private static final int RETURN_M_ID = 2;

	private Button inboxButton;
	private Button outboxButton;

	private Long a_id;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.email_folder);

		inboxButton = (Button) findViewById(R.id.email_inbox_button);
		outboxButton = (Button) findViewById(R.id.email_outbox_button);

		inboxButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent inboxIntent = new Intent(
						FolderView.this,
						cn.edu.nju.software.xyz.pim.email.ui.EmailMessageList.class);
				inboxIntent.putExtra(EmailDB.EmailAccountColumns.ID, a_id);
				inboxIntent.putExtra(EmailDB.EmailMessageColumns.FOLDER,
						EmailDB.EmailFolder.INBOX);
				FolderView.this.startSubActivity(inboxIntent, 0);
			}
		});
		outboxButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent outboxIntent = new Intent(
						FolderView.this,
						cn.edu.nju.software.xyz.pim.email.ui.EmailMessageList.class);
				outboxIntent.putExtra(EmailDB.EmailAccountColumns.ID, a_id);
				outboxIntent.putExtra(EmailDB.EmailMessageColumns.FOLDER,
						EmailDB.EmailFolder.OUTBOX);
				FolderView.this.startSubActivity(outboxIntent, 0);

			}
		});

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			a_id = extras.getLong(EmailDB.EmailAccountColumns.ID);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, COMPOSE_M_ID, R.string.compose);
		menu.add(0, RETURN_M_ID, R.string.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {
		case COMPOSE_M_ID:
			Intent composeIntent = new Intent(this,
					cn.edu.nju.software.xyz.pim.email.ui.EmailCompose.class);
			composeIntent.putExtra(EmailDB.EmailAccountColumns.ID, a_id);
			startSubActivity(composeIntent, 0);
			break;
		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

}
