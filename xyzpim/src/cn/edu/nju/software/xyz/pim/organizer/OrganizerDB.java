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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import cn.edu.nju.software.xyz.pim.util.Log;

/**
 * @author savio 2008-4-15 下午03:21:18
 * 
 */
public class OrganizerDB {

	private static final String CREATE_TABLE_NOTES = "create table notes (note_id integer primary key autoincrement, "
			+ "title text not null, content text not null);";

	private static final String CREATE_TABLE_MEETINGS = "create table meetings (meeting_id integer primary key autoincrement, "
			+ "title text not null, start_time text not null, end_time text not null,is_notify integer not null,place text not null);";

	private static final String CREATE_TABLE_TASKS = "create table tasks (task_id integer primary key autoincrement, "
			+ "title text not null, date text not null,is_important integer not null,is_notify integer not null,content text not null);";

	private static final String NOTES_TABLE = "notes";
	private static final String MEETINGS_TABLE = "meetings";
	private static final String TASKS_TABLE = "tasks";
	private static final String DATABASE_NAME = "organizerdb";
	private static final int DATABASE_VERSION = 1;

	private SQLiteDatabase db;

	private static OrganizerDB ins;

	private OrganizerDB() {
	}

	private OrganizerDB(Context ctx) {
		try {
			db = ctx.openDatabase(DATABASE_NAME, null);
		} catch (FileNotFoundException e) {
			try {
				db = ctx.createDatabase(DATABASE_NAME, DATABASE_VERSION, 0,
						null);
				db.execSQL(CREATE_TABLE_NOTES);
				db.execSQL(CREATE_TABLE_MEETINGS);
				db.execSQL(CREATE_TABLE_TASKS);
			} catch (FileNotFoundException e1) {
				db = null;
			}
		}
	}

	public synchronized static OrganizerDB getInstance(Context ctx) {
		if (null == ins)
			ins = new OrganizerDB(ctx);
		return ins;
	}

	public boolean insertNote(String title, String content) {
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("content", content);
		return (db.insert(NOTES_TABLE, null, values) > 0);
	}

	public boolean deleteNote(Long noteId) {
		return (db.delete(NOTES_TABLE, "note_id=" + noteId.toString(), null) > 0);
	}

	public int updateNote(Long noteId, String title, String content) {
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("content", content);
		return (db.update(NOTES_TABLE, values, "note_id=" + noteId.toString(),
				null));
	}

	public int updateMeeting(Long MeetingId, String title, String start_time,
			String end_time, int notify, String place) {
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("start_time", start_time);
		values.put("end_time", end_time);
		values.put("is_notify", notify);
		values.put("place", place);
		return (db.update(MEETINGS_TABLE, values, "meeting_id="
				+ MeetingId.toString(), null));
	}

	public boolean insertMeeting(String title, String start_time,
			String end_time, int notify, String place) {
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("start_time", start_time);
		values.put("end_time", end_time);
		values.put("is_notify", notify);
		values.put("place", place);
		return (db.insert(MEETINGS_TABLE, null, values) > 0);
	}

	public boolean deleteMeeting(Long meetingId) {
		return (db.delete(MEETINGS_TABLE, "meeting_id=" + meetingId.toString(),
				null) > 0);
	}

	public int updateTask(Long taskId, String title, String date,
			int important, int notify, String content) {
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("date", date);
		values.put("is_important", important);
		values.put("is_notify", notify);
		values.put("content", content);
		return (db.update(TASKS_TABLE, values, "task_id=" + taskId.toString(),
				null));
	}

	public boolean insertTask(String title, String date, int important,
			int notify, String content) {
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("date", date);
		values.put("is_important", important);
		values.put("is_notify", notify);
		values.put("content", content);
		return (db.insert(TASKS_TABLE, null, values) > 0);
	}

	public boolean deleteTask(Long taskId) {
		return (db.delete(TASKS_TABLE, "task_id=" + taskId.toString(), null) > 0);
	}

	public List<Note> getNotes() {
		ArrayList<Note> notes = new ArrayList<Note>();
		try {
			Cursor c = db.query(NOTES_TABLE, new String[] { "note_id", "title",
					"content" }, null, null, null, null, null);

			int numRows = c.count();
			c.first();
			for (int i = 0; i < numRows; ++i) {
				Note note = new Note();
				note.NoteId = c.getLong(0);
				note.Title = c.getString(1);
				note.Content = c.getString(2);
				notes.add(note);
				c.next();
			}
		} catch (SQLException e) {
			Log.e(e.getMessage());
		}
		return notes;
	}

	public Note getNote(long id) {
		Note note = new Note();
		try {
			Cursor c = db.query(NOTES_TABLE, new String[] { "note_id", "title",
					"content" }, "note_id=?",
					new String[] { String.valueOf(id) }, null, null, null);

			int numRows = c.count();
			if (numRows != 1) {
				Log.e("newRows=" + String.valueOf(numRows));
			}
			c.first();
			note.NoteId = c.getLong(0);
			note.Title = c.getString(1);
			note.Content = c.getString(2);
		} catch (SQLException e) {
			Log.e(e.getMessage());
		}
		return note;
	}

	public List<Meeting> getMeetings() {
		ArrayList<Meeting> meetings = new ArrayList<Meeting>();
		try {
			Cursor c = db.query(MEETINGS_TABLE, new String[] { "meeting_id",
					"title", "start_time", "end_time", "is_notify", "place" },
					null, null, null, null, null);

			int numRows = c.count();
			c.first();
			for (int i = 0; i < numRows; ++i) {
				Meeting meeting = new Meeting();
				meeting.MeetId = c.getLong(0);
				meeting.Title = c.getString(1);
				meeting.StartDateTime = c.getString(2);
				meeting.EndDateTime = c.getString(3);
				meeting.IsNotify = c.getInt(4);
				meeting.Place = c.getString(5);
				meetings.add(meeting);
				c.next();
			}
		} catch (SQLException e) {
			Log.e(e.getMessage());
		}
		return meetings;
	}

	public Meeting getMeeting(long id) {
		Meeting meeting = new Meeting();
		try {
			Cursor c = db.query(MEETINGS_TABLE, new String[] { "meeting_id",
					"title", "start_time", "end_time", "is_notify", "place" },
					"meeting_id=?", new String[] { String.valueOf(id) }, null,
					null, null);

			int numRows = c.count();
			if (numRows != 1) {
				Log.e("newRows=" + String.valueOf(numRows));
			}
			c.first();
			meeting.MeetId = c.getLong(0);
			meeting.Title = c.getString(1);
			meeting.StartDateTime = c.getString(2);
			meeting.EndDateTime = c.getString(3);
			meeting.IsNotify = c.getInt(4);
			meeting.Place = c.getString(5);
		} catch (SQLException e) {
			Log.e(e.getMessage());
		}
		return meeting;
	}

	public List<Task> getTasks() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		try {
			Cursor c = db.query(TASKS_TABLE, new String[] { "task_id", "title",
					"date", "is_important", "is_notify", "content" }, null,
					null, null, null, null);

			int numRows = c.count();
			c.first();
			for (int i = 0; i < numRows; ++i) {
				Task task = new Task();
				task.TaskId = c.getLong(0);
				task.Title = c.getString(1);
				task.Date = c.getString(2);
				task.IsImportant = c.getInt(3);
				task.IsNotify = c.getInt(4);
				task.Content = c.getString(5);
				tasks.add(task);
				c.next();
			}
		} catch (SQLException e) {
			Log.e(e.getMessage());
		}
		return tasks;
	}

	public Task getTask(long id) {
		Task task = new Task();
		try {
			Cursor c = db.query(TASKS_TABLE, new String[] { "task_id", "title",
					"date", "is_important", "is_notify", "content" },
					"task_id=?", new String[] { String.valueOf(id) }, null,
					null, null);

			int numRows = c.count();
			if (numRows != 1) {
				Log.e("newRows=" + String.valueOf(numRows));
			}
			c.first();
			task.TaskId = c.getLong(0);
			task.Title = c.getString(1);
			task.Date = c.getString(2);
			task.IsImportant = c.getInt(3);
			task.IsNotify = c.getInt(4);
			task.Content = c.getString(5);
		} catch (SQLException e) {
			Log.e(e.getMessage());
		}
		return task;
	}
}
