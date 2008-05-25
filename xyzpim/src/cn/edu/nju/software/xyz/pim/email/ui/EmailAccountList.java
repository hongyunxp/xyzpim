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

/**
 * @author xmx 2008-4-27 上午02:14:36
 * 
 */
public class EmailAccountList extends ListActivity {

	private static final int NEW_M_ID = 0;
	private static final int EDIT_M_ID = 1;
	private static final int DEL_M_ID = 2;
	private static final int OPEN_M_ID = 3;
	private static final int RETURN_M_ID = 4;

	private List<EmailAccount> accounts;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.email_account_list);
		fillData();
		if (accounts.size() > 0)
			setSelection(0);
	}

	private void fillData() {
		accounts = EmailDB.getInstance(this).fetchEmailAccounts();
		int count = accounts.size();
		List<String> accountNames = new ArrayList<String>(count);
		for (int index = 0; index < count; ++index) {
			accountNames.add(accounts.get(index).name);
		}
		ArrayAdapter<String> accountNamesAdapter = new ArrayAdapter<String>(
				this, R.layout.email_account_row, R.id.email_account_row_text,
				accountNames);
		setListAdapter(accountNamesAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Log.i(position);
		openAccount(position);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, NEW_M_ID, R.string.create, R.drawable.add);
		menu.add(0, EDIT_M_ID, R.string.edit, R.drawable.edit);
		menu.add(0, DEL_M_ID, R.string.del, R.drawable.delete);
		menu.add(0, OPEN_M_ID, R.string.open, R.drawable.open);
		menu.add(0, RETURN_M_ID, R.string.back, R.drawable.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {

		case NEW_M_ID:
			Intent newIntent = new Intent(this,
					cn.edu.nju.software.xyz.pim.email.ui.EditSettingsView.class);
			startSubActivity(newIntent, 0);
			break;
		case EDIT_M_ID:
			EmailAccount editEA = accounts.get(getListView()
					.getSelectedItemPosition());
			Intent editIntent = new Intent(this,
					cn.edu.nju.software.xyz.pim.email.ui.EditSettingsView.class);
			editIntent.putExtra(EmailDB.EmailAccountColumns.ID, editEA.id);
			startSubActivity(editIntent, 0);
			break;
		case DEL_M_ID:
			EmailAccount delEA = accounts.get(getListView()
					.getSelectedItemPosition());
			EmailDB.getInstance(this).deleteEmailAccount(delEA.id);
			fillData();
			break;
		case OPEN_M_ID:
			openAccount(getListView().getSelectedItemPosition());
			break;
		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void openAccount(int position) {
		// Log.i(position);
		Intent openIntent = new Intent(this,
				cn.edu.nju.software.xyz.pim.email.ui.FolderView.class);
		EmailAccount openEA = accounts.get(position);
		openIntent.putExtra(EmailDB.EmailAccountColumns.ID, openEA.id);
		startSubActivity(openIntent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			String data, Bundle extras) {
		super.onActivityResult(requestCode, resultCode, data, extras);
		fillData();
	}
}
