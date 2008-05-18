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

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import cn.edu.nju.software.xyz.pim.R;
import cn.edu.nju.software.xyz.pim.contacts.GroupsDbAdapter;

/**
 * @author xmx 2008-5-17 下午10:52:21
 * 
 */
public class GroupSelectView extends ListActivity {

	private GroupsDbAdapter mGroupDbAdp;
	private Cursor groupsCursor;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.groups_list);
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
	public void onListItemClick(ListView l, View v, int position, long id) {
		Cursor c = groupsCursor;
		// int position = getSelectedItemPosition();
		c.moveTo(position);
		int gid = c.getInt(c.getColumnIndex(GroupsDbAdapter.COL_ROWID));
		Bundle bundle = new Bundle();
		bundle.putInt(GroupsDbAdapter.COL_ROWID, gid);
		setResult(RESULT_OK, null, bundle);
		finish();
		// return true;
	}
}
