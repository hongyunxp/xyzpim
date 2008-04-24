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
 * @author savio 2008-4-15 下午10:33:28
 * 
 */
public class EditTask extends Activity {

	private static final int RETURN_M_ID = 0;
	private static final int FINISH_M_ID = 1;

	private EditText titleText;
	private CheckBox isNotify;
	private CheckBox isImportant;
	private EditText contentText;
	private Button dateButton;
	private Button timeButton;

	private Long TaskId;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.task_edit);
		titleText = (EditText) findViewById(R.id.task_title);
		dateButton = (Button) findViewById(R.id.task_date);
		timeButton = (Button) findViewById(R.id.task_time);
		isNotify = (CheckBox) findViewById(R.id.task_notify);
		isImportant = (CheckBox) findViewById(R.id.important_notify);
		contentText = (EditText) findViewById(R.id.task_content);

		dateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int year = 2008, month = 1, day = 1;

				if (null != TaskId) {
					FairDB fairDbAdp = FairDB.getInstance(EditTask.this);
					Task task = fairDbAdp.getTask(TaskId);

					String reg = "(\\d{4})-(\\d{1,2})-(\\d{1,2})\\s(\\d{1,2}):(\\d{1,2})";
					Matcher m = Pattern.compile(reg).matcher(task.Date);
					if (m.find()) {
						year = Integer.parseInt(m.group(1));
						month = Integer.parseInt(m.group(2));
						day = Integer.parseInt(m.group(3));
					}
				}

				new DatePickerDialog(EditTask.this, dateButtonListener, year,
						month, day, Calendar.SUNDAY).show();
			}

		});

		timeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int hour = 0, minute = 0;

				if (null != TaskId) {
					FairDB fairDbAdp = FairDB.getInstance(EditTask.this);
					Task task = fairDbAdp.getTask(TaskId);

					String reg = "(\\d{4})-(\\d{1,2})-(\\d{1,2})\\s(\\d{1,2}):(\\d{1,2})";
					Matcher m = Pattern.compile(reg).matcher(task.Date);
					if (m.find()) {
						hour = Integer.parseInt(m.group(4));
						minute = Integer.parseInt(m.group(5));
					}
				}

				new TimePickerDialog(EditTask.this, timeListener, "Set", hour,
						minute, true).show();
			}
		});

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			TaskId = extras.getLong("TaskId");
			FairDB fairDbAdp = FairDB.getInstance(this);
			Task task = fairDbAdp.getTask(TaskId);
			titleText.setText(task.Title);

			String reg = "(\\d{4}-\\d{1,2}-\\d{1,2})\\s(\\d{1,2}:\\d{1,2})";
			Matcher m = Pattern.compile(reg).matcher(task.Date);
			if (m.find()) {
				String date = m.group(1);
				String time = m.group(2);
				dateButton.setText(date);
				timeButton.setText(time);
			}
			// isNotify.setText(task.Notify);
			if (task.IsNotify == 0)
				isNotify.setChecked(false);
			else
				isNotify.setChecked(true);
			if (task.IsImportant == 0)
				isImportant.setChecked(false);
			else
				isImportant.setChecked(true);
			contentText.setText(task.Content);
		}
	}

	private final DatePicker.OnDateSetListener dateButtonListener = new DatePicker.OnDateSetListener() {

		public void dateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			StringBuilder buf = new StringBuilder();
			buf.append(String.valueOf(year));
			buf.append('-');
			buf.append(String.valueOf(monthOfYear));
			buf.append('-');
			buf.append(String.valueOf(dayOfMonth));

			dateButton.setText(buf.toString());
		}
	};

	private final TimePicker.OnTimeSetListener timeListener = new TimePicker.OnTimeSetListener() {

		public void timeSet(TimePicker view, int hourOfDay, int minute) {
			StringBuilder buf = new StringBuilder();
			buf.append(String.valueOf(hourOfDay));
			buf.append(':');
			buf.append(String.valueOf(minute));

			timeButton.setText(buf.toString());
		}
	};

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
			int im,
			in;
			FairDB FairDbAdp = FairDB.getInstance(this);
			String title = titleText.getText().toString();
			String dateTime = dateButton.getText().toString() + " "
					+ timeButton.getText().toString();
			// String notify = notifyText.getText().toString();
			if (isNotify.isChecked())
				in = 1;
			else
				in = 0;
			if (isImportant.isChecked())
				im = 1;
			else
				im = 0;
			String content = contentText.getText().toString();
			if (null == TaskId)
				FairDbAdp.insertTask(title, dateTime, im, in, content);
			else
				FairDbAdp.updateTask(TaskId, title, dateTime, im, in, content);
			finish();
			return true;
		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
