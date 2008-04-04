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
import android.widget.SimpleCursorAdapter;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx 2008-4-4 下午10:06:20
 * 
 */
public class GroupsView extends ListActivity {

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
	private void fillData() {
		// Get all of the rows from the database and create the item list
		Cursor groupsCursor = mGroupDbAdp.fetchAllGroups();
		startManagingCursor(groupsCursor);

		// Create an array to specify the fields we want to display in the list
		// (only TITLE)
		String[] from = new String[] { mGroupDbAdp.COL_NAME };

		// and an array of the fields we want to bind those fields to (in this
		// case just text1)
		int[] to = new int[] { R.id.groups_row_text };

		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter groups = new SimpleCursorAdapter(this,
				R.layout.groups_row, groupsCursor, from, to);
		setListAdapter(groups);
	}

}
