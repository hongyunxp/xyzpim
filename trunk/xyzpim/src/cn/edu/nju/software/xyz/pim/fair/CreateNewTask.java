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
package cn.edu.nju.software.xyz.pim.fair;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Menu.Item;
import android.widget.CheckBox;
import android.widget.EditText;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx 2008-3-13 下午01:16:54
 * 
 */
public class CreateNewTask extends Activity {

	private static final int RETURN_M_ID = 0;
	private static final int FINISH_M_ID = 1;

	private EditText titleText;
	private EditText DateText;
	private EditText notifyText;
	private CheckBox isImportant;
	private EditText contentText;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.create_newtask);
		titleText = (EditText) findViewById(R.id.task_title);
		DateText = (EditText) findViewById(R.id.task_time);
		notifyText = (EditText) findViewById(R.id.task_notify);
		isImportant = (CheckBox) findViewById(R.id.important_notify);
		contentText = (EditText) findViewById(R.id.task_content);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, FINISH_M_ID, R.string.finish);
		menu.add(0, RETURN_M_ID, R.string.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {

		case FINISH_M_ID:
			int im;
			FairDB FairDbAdp = FairDB.getInstance(this);
			String title = titleText.getText().toString();
			String date = DateText.getText().toString();
			String notify = notifyText.getText().toString();
			if (isImportant.isChecked())
				im = 1;
			else
				im = 0;
			String content = contentText.getText().toString();
			FairDbAdp.insertTask(title, date, im, notify, content);
			finish();
			return true;
		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
