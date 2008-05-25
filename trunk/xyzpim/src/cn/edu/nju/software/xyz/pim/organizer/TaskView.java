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
package cn.edu.nju.software.xyz.pim.organizer;

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

/**
 * @author savio 2008-4-15 下午03:20:23
 * 
 */
public class TaskView extends ListActivity {
	private static final int ACTIVITY_CREATE = 1;
	private static final int ACTIVITY_EDIT = 2;

	private static final int NEW_M_ID = 0;
	private static final int OPEN_M_ID = 1;
	private static final int RETURN_M_ID = 2;
	private static final int DEL_M_ID = 3;

	private List<Task> taskList;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.task_list);
		OrganizerDB organizerDbAdp = OrganizerDB.getInstance(this);
		taskList = new ArrayList<Task>();
		taskList = organizerDbAdp.getTasks();
		fillData();

	}

	@SuppressWarnings( { "static-access", "unchecked" })
	/**
	 * 向界面填充数据
	 */
	private void fillData() {
		// 创建适配器
		int count = taskList.size();
		List<String> noteTitle = new ArrayList<String>(count);
		for (int index = 0; index < count; ++index) {
			noteTitle.add(taskList.get(index).Title);
		}
		ArrayAdapter notesAdapter = new ArrayAdapter(this, R.layout.task_row,
				noteTitle);
		setListAdapter(notesAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, NEW_M_ID, R.string.create, R.drawable.add);
		menu.add(0, DEL_M_ID, R.string.del, R.drawable.delete);
		menu.add(0, OPEN_M_ID, R.string.open, R.drawable.open);
		menu.add(0, RETURN_M_ID, R.string.back, R.drawable.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {

		case NEW_M_ID:
			Intent newURLIntent = new Intent(this, EditTask.class);
			startSubActivity(newURLIntent, ACTIVITY_CREATE);
			return true;
		case DEL_M_ID:
			OrganizerDB OrganizerDbAdp = OrganizerDB.getInstance(this);
			OrganizerDbAdp.deleteTask(taskList.get((int) getListView()
					.getSelectedItemId()).TaskId);
			taskList = OrganizerDbAdp.getTasks();
			fillData();
			return true;
		case OPEN_M_ID:
			openTask((int) getListView().getSelectedItemId());
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
		openTask(position);
	}

	private void openTask(int position) {
		Intent openIntent = new Intent(this, EditTask.class);
		openIntent.putExtra("TaskId", taskList.get(position).TaskId);
		startSubActivity(openIntent, ACTIVITY_EDIT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			String data, Bundle extras) {
		super.onActivityResult(requestCode, resultCode, data, extras);
		switch (requestCode) {
		case ACTIVITY_CREATE:
		case ACTIVITY_EDIT:
			OrganizerDB organizerDbAdp = OrganizerDB.getInstance(this);
			taskList = new ArrayList<Task>();
			taskList = organizerDbAdp.getTasks();
			fillData();
		}
	}
}
