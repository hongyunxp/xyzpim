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

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import cn.edu.nju.software.xyz.pim.R;
import cn.edu.nju.software.xyz.pim.util.InputDialog;
import cn.edu.nju.software.xyz.pim.util.OnInputOKListener;

/**
 * @author xmx 2008-4-4 下午10:06:20
 * 
 */
public class GroupsView extends ListActivity {

	private static final int ACTIVITY_OPEN = 3;

	private static final int NEW_M_ID = 0;
	private static final int DEL_M_ID = 1;
	private static final int RENAME_M_ID = 2;
	private static final int OPEN_M_ID = 3;
	private static final int GROUPMAIL_M_ID = 4;
	private static final int RETURN_M_ID = 5;

	private GroupsDbAdapter mGroupDbAdp;
	private Cursor groupsCursor;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.groups_list);
		mGroupDbAdp = new GroupsDbAdapter(this);
		mGroupDbAdp.open();

		fillData();

	}

	@SuppressWarnings("static-access")
	/**
	 * 向界面填充数据
	 */
	private void fillData() {
		// 获得所有的分组
		groupsCursor = mGroupDbAdp.fetchAllGroups();
		startManagingCursor(groupsCursor);

		// 需要显示的列名
		String[] from = new String[] { mGroupDbAdp.COL_NAME };

		// 绑定显示的textview控件
		int[] to = new int[] { R.id.groups_row_text };

		// 创建适配器
		SimpleCursorAdapter groups = new SimpleCursorAdapter(this,
				R.layout.groups_row, groupsCursor, from, to);
		setListAdapter(groups);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, NEW_M_ID, R.string.create, R.drawable.add);
		menu.add(0, DEL_M_ID, R.string.del, R.drawable.delete);
		menu.add(0, RENAME_M_ID, R.string.rename, R.drawable.edit);
		menu.add(0, OPEN_M_ID, R.string.open, R.drawable.open);
		menu.add(0, RETURN_M_ID, R.string.back, R.drawable.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {
		case NEW_M_ID:
			createGroup();
			return true;
		case DEL_M_ID:
			mGroupDbAdp.deleteGroup(getListView().getSelectedItemId());
			fillData();
			return true;
		case RENAME_M_ID:
			editGroupName(getSelectedItemPosition());
			return true;
		case OPEN_M_ID:
			openGroup(getSelectedItemPosition());
			return true;
		case GROUPMAIL_M_ID:
			// TODO: 群发邮件！！！！
			return true;
		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		openGroup(position);
	}

	private void createGroup() {
		/*Intent i = new Intent(this, GroupNameEdit.class);
		startSubActivity(i, ACTIVITY_CREATE);*/
		InputDialog groupNameDialog = new InputDialog(this,
				new OnInputOKListener() {
					@Override
					public void onInputOK(String inputString) {
						String groupName = inputString;
						mGroupDbAdp.createGroup(groupName);
						fillData();

					}
				});
		groupNameDialog.show("Create a Group", "Input the group name");
	}

	private void editGroupName(int position) {
		if (position < 0)
			return;
		Cursor c = groupsCursor;
		c.moveTo(position);
		String currentGroupName = c.getString(c
				.getColumnIndex(GroupsDbAdapter.COL_NAME));
		final long rowId = c.getLong(c
				.getColumnIndex(GroupsDbAdapter.COL_ROWID));
		InputDialog groupNameDialog = new InputDialog(this,
				new OnInputOKListener() {
					@Override
					public void onInputOK(String inputString) {
						String groupName = inputString;
						mGroupDbAdp.updateGroup(rowId, groupName);

						fillData();

					}
				});
		groupNameDialog.show("Edit Group Name", "Input the group name",
				currentGroupName);
	}

	private void openGroup(int position) {
		Cursor c = groupsCursor;
		c.moveTo(position);
		Intent i = new Intent(this, GroupMembersView.class);
		i.putExtra(GroupsDbAdapter.COL_ROWID, c.getInt(c
				.getColumnIndex(GroupsDbAdapter.COL_ROWID)));
		startSubActivity(i, ACTIVITY_OPEN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			String data, Bundle extras) {
		super.onActivityResult(requestCode, resultCode, data, extras);
		// String groupName = extras.getString(GroupsDbAdapter.COL_NAME);
		switch (requestCode) {

		}
	}
}
