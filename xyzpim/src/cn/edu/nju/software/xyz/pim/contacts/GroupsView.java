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
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.Menu.Item;
import android.widget.SimpleCursorAdapter;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx 2008-4-4 下午10:06:20
 * 
 */
public class GroupsView extends ListActivity {

	private static final int NEW_M_ID = 0;
	private static final int DEL_M_ID = 1;
	private static final int EDIT_M_ID = 2;
	private static final int RETURN_M_ID = 3;

	private GroupsDbAdapter mGroupDbAdp;

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
	 * 填充数据
	 */
	private void fillData() {
		// 获得所有的分组
		Cursor groupsCursor = mGroupDbAdp.fetchAllGroups();
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
		menu.add(0, NEW_M_ID, R.string.contacts_group_menu_new);
		menu.add(0, DEL_M_ID, R.string.contacts_group_menu_del);
		menu.add(0, EDIT_M_ID, R.string.contacts_group_menu_edit);
		menu.add(0, RETURN_M_ID, R.string.contacts_group_menu_return);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {
		case NEW_M_ID:
			createGroup();
			return true;
		case DEL_M_ID:
			// mDbHelper.deleteNote(getListView().getSelectedItemId());
			// fillData();
			return true;
		case EDIT_M_ID:
			return true;
		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void createGroup() {
		// TODO Auto-generated method stub

	}
}
