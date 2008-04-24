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
package cn.edu.nju.software.xyz.pim.contacts;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx 2008-4-5 下午09:35:41
 * 
 */
public class GroupMembersView extends ListActivity {

	private int gid;

	private static final int ADD_M_ID = 0;
	private static final int DEL_M_ID = 1;
	private static final int OPEN_M_ID = 2;
	private static final int RETURN_M_ID = 3;

	private static final int REQ_CODE_PICKCONTACT = 1;

	private List<String> memURIList;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.group_members_list);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			gid = extras.getInt(GroupsDbAdapter.COL_ROWID);
			// Log.i("XYZPIM", "" + gid);
		}

		fillData();

	}

	private void fillData() {
		// 获得本分组的所有成员
		memURIList = GroupMemberDbAdapter.getInstance(this).listMembers(gid);
		List<String> memNameList = new ArrayList<String>();
		int count = memURIList.size();
		for (int index = 0; index < count; ++index) {
			Uri memURI = Uri.parse(memURIList.get(index));
			Cursor c = managedQuery(memURI, null, null, null);
			// Log.i("XYZPIM", "" + c.count());
			c.moveTo(0);
			memNameList
					.add(c
							.getString(c
									.getColumnIndex(android.provider.Contacts.PeopleColumns.NAME)));
		}
		ArrayAdapter<String> peopleNamesAdapter = new ArrayAdapter<String>(
				GroupMembersView.this, R.layout.group_members_row,
				R.id.groupmem_row_text, memNameList);
		setListAdapter(peopleNamesAdapter);

	}

	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		openMem(position);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, ADD_M_ID, R.string.add);
		menu.add(0, DEL_M_ID, R.string.del);
		menu.add(0, OPEN_M_ID, R.string.open);
		menu.add(0, RETURN_M_ID, R.string.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {
		case ADD_M_ID:
			startSubActivity(new Intent(Intent.PICK_ACTION,
					Contacts.People.CONTENT_URI), REQ_CODE_PICKCONTACT);

			return true;
		case DEL_M_ID:
			String memURI = memURIList.get(getSelectedItemPosition());
			GroupMemberDbAdapter.getInstance(this).delMember(gid, memURI);
			fillData();
			return true;
		case OPEN_M_ID:
			openMem(getSelectedItemPosition());
			return true;
		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void openMem(int position) {
		Uri uri = Uri.parse(memURIList.get(position));
		startSubActivity(new Intent(Intent.VIEW_ACTION, uri), 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			String data, Bundle extras) {
		super.onActivityResult(requestCode, resultCode, data, extras);
		switch (requestCode) {
		case REQ_CODE_PICKCONTACT:
			// Log.i("XYZPIM", data);
			GroupMemberDbAdapter memDB = GroupMemberDbAdapter.getInstance(this);
			memDB.addMember(gid, data);
			fillData();
			break;
		}
	}
}
