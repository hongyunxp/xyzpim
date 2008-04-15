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
 * @author savio 2008-4-15 下午09:59:09
 * 
 */
public class EditMeeting extends Activity {

	private static final int RETURN_M_ID = 0;
	private static final int FINISH_M_ID = 1;

	private EditText titleText;
	private EditText startDateText;
	private EditText endDateText;
	private CheckBox notifyText;
	private EditText placeText;

	private long meetingId;

	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.create_meeting);
		titleText = (EditText) findViewById(R.id.meeting_title);
		startDateText = (EditText) findViewById(R.id.meeting_start_time);
		endDateText = (EditText) findViewById(R.id.meeting_end_time);
		notifyText = (CheckBox) findViewById(R.id.meeting_notify);
		placeText = (EditText) findViewById(R.id.meeting_place);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			meetingId = extras.getLong("MeetingId");
			FairDB fairDbAdp = FairDB.getInstance(this);
			Meeting meeting = fairDbAdp.getMeeting(meetingId);
			titleText.setText(meeting.Title);
			startDateText.setText(meeting.StartTime);
			endDateText.setText(meeting.EndTime);
			if (meeting.IsNotify == 0)
				notifyText.setChecked(false);
			else
				notifyText.setChecked(true);
			placeText.setText(meeting.Place);
		}
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
			int notify;
			FairDB FairDbAdp = FairDB.getInstance(this);
			String title = titleText.getText().toString();
			String startDate = startDateText.getText().toString();
			String endDate = endDateText.getText().toString();
			if (notifyText.isChecked())
				notify = 1;
			else
				notify = 0;
			String place = placeText.getText().toString();
			FairDbAdp.updateMeeting(meetingId, title, startDate, endDate,
					notify, place);
			finish();
			return true;
		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

}
