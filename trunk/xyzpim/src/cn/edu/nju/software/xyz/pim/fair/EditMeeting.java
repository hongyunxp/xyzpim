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

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author savio 2008-4-15 下午09:59:09
 * 
 */
public class EditMeeting extends Activity {

	private static final int RETURN_M_ID = 0;
	private static final int FINISH_M_ID = 1;

	private EditText titleText;
	private Button startDateButton;
	private Button startTimeButton;
	private Button endDateButton;
	private Button endTimeButton;
	private CheckBox notifyText;
	private EditText placeText;

	private long meetingId;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.create_meeting);
		titleText = (EditText) findViewById(R.id.meeting_title);
		startDateButton = (Button) findViewById(R.id.meeting_start_date);
		startTimeButton = (Button) findViewById(R.id.meeting_start_time);
		endDateButton = (Button) findViewById(R.id.meeting_end_date);
		endTimeButton = (Button) findViewById(R.id.meeting_end_time);
		notifyText = (CheckBox) findViewById(R.id.meeting_notify);
		placeText = (EditText) findViewById(R.id.meeting_place);

		startDateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int year = 2008, month = 1, day = 1;

				FairDB fairDbAdp = FairDB.getInstance(EditMeeting.this);
				Meeting meeting = fairDbAdp.getMeeting(meetingId);

				String reg = "(\\d{4})-(\\d{1,2})-(\\d{1,2})\\s(\\d{1,2}):(\\d{1,2})";
				Matcher m = Pattern.compile(reg).matcher(meeting.StartDateTime);
				if (m.find()) {
					year = Integer.parseInt(m.group(1));
					month = Integer.parseInt(m.group(2));
					day = Integer.parseInt(m.group(3));
				}
				new DatePickerDialog(EditMeeting.this, startDateListener, year,
						month, day, Calendar.SUNDAY).show();
			}
		});

		startTimeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int hour = 0, minute = 0;

				FairDB fairDbAdp = FairDB.getInstance(EditMeeting.this);
				Meeting meeting = fairDbAdp.getMeeting(meetingId);

				String reg = "(\\d{4})-(\\d{1,2})-(\\d{1,2})\\s(\\d{1,2}):(\\d{1,2})";
				Matcher m = Pattern.compile(reg).matcher(meeting.StartDateTime);
				if (m.find()) {
					hour = Integer.parseInt(m.group(4));
					minute = Integer.parseInt(m.group(5));
				}
				new TimePickerDialog(EditMeeting.this, startTimeListener,
						"Set", hour, minute, true).show();
			}
		});

		endDateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int year = 2008, month = 1, day = 1;

				FairDB fairDbAdp = FairDB.getInstance(EditMeeting.this);
				Meeting meeting = fairDbAdp.getMeeting(meetingId);

				String reg = "(\\d{4})-(\\d{1,2})-(\\d{1,2})\\s(\\d{1,2}):(\\d{1,2})";
				Matcher m = Pattern.compile(reg).matcher(meeting.EndDateTime);
				if (m.find()) {
					year = Integer.parseInt(m.group(1));
					month = Integer.parseInt(m.group(2));
					day = Integer.parseInt(m.group(3));
				}
				new DatePickerDialog(EditMeeting.this, endDateListener, year,
						month, day, Calendar.SUNDAY).show();
			}
		});

		endTimeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int hour = 0, minute = 0;

				FairDB fairDbAdp = FairDB.getInstance(EditMeeting.this);
				Meeting meeting = fairDbAdp.getMeeting(meetingId);

				String reg = "(\\d{4})-(\\d{1,2})-(\\d{1,2})\\s(\\d{1,2}):(\\d{1,2})";
				Matcher m = Pattern.compile(reg).matcher(meeting.EndDateTime);
				if (m.find()) {
					hour = Integer.parseInt(m.group(4));
					minute = Integer.parseInt(m.group(5));
				}
				new TimePickerDialog(EditMeeting.this, endTimeListener, "Set",
						hour, minute, true).show();
			}
		});

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			meetingId = extras.getLong("MeetingId");
			FairDB fairDbAdp = FairDB.getInstance(this);
			Meeting meeting = fairDbAdp.getMeeting(meetingId);
			titleText.setText(meeting.Title);

			String reg = "(\\d{4}-\\d{1,2}-\\d{1,2})\\s(\\d{1,2}:\\d{1,2})";
			Matcher m = Pattern.compile(reg).matcher(meeting.StartDateTime);
			if (m.find()) {
				String startDate = m.group(1);
				String startTime = m.group(2);
				startDateButton.setText(startDate);
				startTimeButton.setText(startTime);
			}

			m = Pattern.compile(reg).matcher(meeting.EndDateTime);
			if (m.find()) {
				String endDate = m.group(1);
				String endTime = m.group(2);
				endDateButton.setText(endDate);
				endTimeButton.setText(endTime);
			}

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
			String startDateTime = startDateButton.getText().toString() + " "
					+ startTimeButton.getText().toString();
			String endDateTime = endDateButton.getText().toString() + " "
					+ endTimeButton.getText().toString();
			if (notifyText.isChecked())
				notify = 1;
			else
				notify = 0;
			String place = placeText.getText().toString();
			FairDbAdp.updateMeeting(meetingId, title, startDateTime,
					endDateTime, notify, place);
			finish();
			return true;
		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private final DatePicker.OnDateSetListener startDateListener = new DatePicker.OnDateSetListener() {

		public void dateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			StringBuilder buf = new StringBuilder();
			buf.append(String.valueOf(year));
			buf.append('-');
			buf.append(String.valueOf(monthOfYear));
			buf.append('-');
			buf.append(String.valueOf(dayOfMonth));

			startDateButton.setText(buf.toString());
		}
	};

	private final TimePicker.OnTimeSetListener startTimeListener = new TimePicker.OnTimeSetListener() {

		public void timeSet(TimePicker view, int hourOfDay, int minute) {
			StringBuilder buf = new StringBuilder();
			buf.append(String.valueOf(hourOfDay));
			buf.append(':');
			buf.append(String.valueOf(minute));

			startTimeButton.setText(buf.toString());
		}
	};

	private final DatePicker.OnDateSetListener endDateListener = new DatePicker.OnDateSetListener() {

		public void dateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			StringBuilder buf = new StringBuilder();
			buf.append(String.valueOf(year));
			buf.append('-');
			buf.append(String.valueOf(monthOfYear));
			buf.append('-');
			buf.append(String.valueOf(dayOfMonth));

			endDateButton.setText(buf.toString());
		}
	};

	private final TimePicker.OnTimeSetListener endTimeListener = new TimePicker.OnTimeSetListener() {

		public void timeSet(TimePicker view, int hourOfDay, int minute) {
			StringBuilder buf = new StringBuilder();
			buf.append(String.valueOf(hourOfDay));
			buf.append(':');
			buf.append(String.valueOf(minute));

			endTimeButton.setText(buf.toString());
		}
	};

}
