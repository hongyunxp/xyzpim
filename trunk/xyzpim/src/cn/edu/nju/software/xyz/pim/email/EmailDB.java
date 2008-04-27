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
package cn.edu.nju.software.xyz.pim.email;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author xmx 2008-4-14 下午08:04:00
 * 
 */
public class EmailDB {
	public class EmailAccountColumns {
		public static final String ID = "a_id";
		public static final String NAME = "a_name";
		public static final String SSL = "ssl";
		public static final String SMTPHOST = "smtp_host";
		public static final String SMTPPORT = "smtp_port";
		public static final String POPHOST = "pop_host";
		public static final String POPPORT = "pop_port";
		public static final String USER = "user";
		public static final String PASSWORD = "password";
	}

	public class EmailMessageColumns {
		public static final String ID = "m_id";
		public static final String UID = "uid";
		public static final String FOLDER = "folder";
		public static final String SUBJECT = "subject";
		public static final String DATE = "date";
		public static final String FROM = "_from";
		public static final String TO = "_to";
		public static final String CONTENT = "content";
		public static final String AID = "a_id";
	}

	public class EmailFolder {
		public static final String INBOX = "inbox";
		public static final String OUTBOX = "outbox";
	}

	private static final String CREATE_TABLE_EMAIL_ACCOUNTS = "create table email_accounts "
			+ "(a_id integer primary key autoincrement, "
			+ "a_name text not null, smtp_host text, "
			+ "ssl integet not null, "
			+ "smtp_port integer, pop_host text, "
			+ "pop_port integer, user text, password text);";
	private static final String CREATE_TABLE_EMAIL_MESSAGES = "create table email_messages "
			+ "(m_id text primary key, uid text, "
			+ "folder text, subject text, date text, "
			+ "_from text, _to text, content text, a_id integer);";

	private static final String DATABASE_NAME = "emaildb";

	private static final String DATABASE_EMAIL_ACCOUNTS_TABLE = "email_accounts";
	private static final String DATABASE_EMAIL_MESSAGES_TABLE = "email_messages";

	private static final int DATABASE_VERSION = 2;

	private SQLiteDatabase db;
	// private final Context mCtx;

	private static EmailDB ins;

	private EmailDB() {
	}

	private EmailDB(Context ctx) {
		try {
			db = ctx.openDatabase(DATABASE_NAME, null);
		} catch (FileNotFoundException e) {
			try {
				db = ctx.createDatabase(DATABASE_NAME, DATABASE_VERSION, 0,
						null);
				db.execSQL(CREATE_TABLE_EMAIL_ACCOUNTS);
				db.execSQL(CREATE_TABLE_EMAIL_MESSAGES);
			} catch (FileNotFoundException e1) {
				db = null;
			}
		}
	}

	public synchronized static EmailDB getInstance(Context ctx) {
		if (null == ins)
			ins = new EmailDB(ctx);
		return ins;
	}

	public long createEmailMessage(Message msg, String folder, int aid) {
		ContentValues cv = new ContentValues();
		cv.put(EmailMessageColumns.ID, msg.id);
		cv.put(EmailMessageColumns.UID, msg.uid);
		cv.put(EmailMessageColumns.FOLDER, folder);
		cv.put(EmailMessageColumns.SUBJECT, msg.subject);
		cv.put(EmailMessageColumns.DATE, msg.date);
		cv.put(EmailMessageColumns.FROM, msg.from);
		cv.put(EmailMessageColumns.TO, msg.to);
		cv.put(EmailMessageColumns.CONTENT, msg.content.getRawContentText());
		cv.put(EmailMessageColumns.AID, aid);
		return db.insert(DATABASE_EMAIL_MESSAGES_TABLE, null, cv);
	}

	public boolean deleteEmailMessage(String id) {
		return db.delete(DATABASE_EMAIL_MESSAGES_TABLE, EmailMessageColumns.ID
				+ "=" + "\'" + id + "\'", null) > 0;
	}

	public List<Message> fetchEmailMessages(long aid, String folder) {
		Cursor c = db.query(DATABASE_EMAIL_MESSAGES_TABLE, null,
				EmailMessageColumns.AID + "=" + aid + " and "
						+ EmailMessageColumns.FOLDER + "=" + "\'" + folder
						+ "\'", null, null, null, null);

		List<Message> re = new ArrayList<Message>();
		for (int index = 0; index < c.count(); ++index) {
			c.moveTo(index);
			Message msg = new Message();
			msg.id = c.getString(c.getColumnIndex(EmailMessageColumns.ID));
			msg.uid = c.getString(c.getColumnIndex(EmailMessageColumns.UID));
			msg.subject = c.getString(c
					.getColumnIndex(EmailMessageColumns.SUBJECT));
			msg.date = c.getString(c.getColumnIndex(EmailMessageColumns.DATE));
			msg.from = c.getString(c.getColumnIndex(EmailMessageColumns.FROM));
			msg.to = c.getString(c.getColumnIndex(EmailMessageColumns.TO));
			re.add(msg);
		}
		return re;
	}

	public Message fetchEmailMessage(long aid, String folder, String id) {
		Cursor c = db.query(true, DATABASE_EMAIL_ACCOUNTS_TABLE, null,
				EmailMessageColumns.AID + "=" + aid + " and "
						+ EmailMessageColumns.FOLDER + "=" + "\'" + folder
						+ "\'" + " and " + EmailMessageColumns.ID + "=" + "\'"
						+ id + "\'", null, null, null, null);
		if ((c.count() == 0) || !c.first()) {
			throw new SQLException("No email account matching ID: " + id);
		}

		Message msg = new Message();
		msg.id = c.getString(c.getColumnIndex(EmailMessageColumns.ID));
		msg.uid = c.getString(c.getColumnIndex(EmailMessageColumns.UID));
		msg.subject = c
				.getString(c.getColumnIndex(EmailMessageColumns.SUBJECT));
		msg.date = c.getString(c.getColumnIndex(EmailMessageColumns.DATE));
		msg.from = c.getString(c.getColumnIndex(EmailMessageColumns.FROM));
		msg.to = c.getString(c.getColumnIndex(EmailMessageColumns.TO));

		return msg;
	}

	public long createEmailAccount(EmailAccount ea) {
		ContentValues cv = new ContentValues();
		cv.put(EmailAccountColumns.NAME, ea.name);
		if (ea.isSSL)
			cv.put(EmailAccountColumns.SSL, 1);
		else
			cv.put(EmailAccountColumns.SSL, 0);
		cv.put(EmailAccountColumns.SMTPHOST, ea.smtpHost);
		cv.put(EmailAccountColumns.SMTPPORT, ea.smtpPort);
		cv.put(EmailAccountColumns.POPHOST, ea.popHost);
		cv.put(EmailAccountColumns.POPPORT, ea.popPort);
		cv.put(EmailAccountColumns.USER, ea.user);
		cv.put(EmailAccountColumns.PASSWORD, ea.password);
		return db.insert(DATABASE_EMAIL_ACCOUNTS_TABLE, null, cv);
	}

	public boolean deleteEmailAccount(long id) {
		return (db.delete(DATABASE_EMAIL_MESSAGES_TABLE,
				EmailMessageColumns.AID + "=" + id, null) >= 0)
				&& (db.delete(DATABASE_EMAIL_ACCOUNTS_TABLE,
						EmailAccountColumns.ID + "=" + id, null) > 0);
	}

	public List<EmailAccount> fetchEmailAccounts() {
		Cursor c = db.query(DATABASE_EMAIL_ACCOUNTS_TABLE, null, null, null,
				null, null, null);

		List<EmailAccount> re = new ArrayList<EmailAccount>();
		for (int index = 0; index < c.count(); ++index) {
			c.moveTo(index);
			EmailAccount ea = new EmailAccount();
			ea.id = c.getInt(c.getColumnIndex(EmailAccountColumns.ID));
			ea.name = c.getString(c.getColumnIndex(EmailAccountColumns.NAME));
			if (c.getInt(c.getColumnIndex(EmailAccountColumns.SSL)) == 1)
				ea.isSSL = true;
			else
				ea.isSSL = false;
			ea.smtpHost = c.getString(c
					.getColumnIndex(EmailAccountColumns.SMTPHOST));
			ea.smtpPort = c.getInt(c
					.getColumnIndex(EmailAccountColumns.SMTPPORT));
			ea.popHost = c.getString(c
					.getColumnIndex(EmailAccountColumns.POPHOST));
			ea.popPort = c
					.getInt(c.getColumnIndex(EmailAccountColumns.POPPORT));
			ea.user = c.getString(c.getColumnIndex(EmailAccountColumns.USER));
			ea.password = c.getString(c
					.getColumnIndex(EmailAccountColumns.PASSWORD));
			re.add(ea);
		}
		return re;
	}

	public EmailAccount fetchEmailAccount(long id) {
		Cursor c = db.query(true, DATABASE_EMAIL_ACCOUNTS_TABLE, null,
				EmailAccountColumns.ID + "=" + id, null, null, null, null);
		if ((c.count() == 0) || !c.first()) {
			throw new SQLException("No email account matching ID: " + id);
		}

		EmailAccount re = new EmailAccount();
		re.id = c.getInt(c.getColumnIndex(EmailAccountColumns.ID));
		re.name = c.getString(c.getColumnIndex(EmailAccountColumns.NAME));
		if (c.getInt(c.getColumnIndex(EmailAccountColumns.SSL)) == 1)
			re.isSSL = true;
		else
			re.isSSL = false;
		re.smtpHost = c.getString(c
				.getColumnIndex(EmailAccountColumns.SMTPHOST));
		re.smtpPort = c.getInt(c.getColumnIndex(EmailAccountColumns.SMTPPORT));
		re.popHost = c.getString(c.getColumnIndex(EmailAccountColumns.POPHOST));
		re.popPort = c.getInt(c.getColumnIndex(EmailAccountColumns.POPPORT));
		re.user = c.getString(c.getColumnIndex(EmailAccountColumns.USER));
		re.password = c.getString(c
				.getColumnIndex(EmailAccountColumns.PASSWORD));

		return re;
	}

	public boolean updateEmailAccount(long id, EmailAccount ea) {
		ContentValues cv = new ContentValues();
		cv.put(EmailAccountColumns.NAME, ea.name);
		if (ea.isSSL)
			cv.put(EmailAccountColumns.SSL, 1);
		else
			cv.put(EmailAccountColumns.SSL, 0);
		cv.put(EmailAccountColumns.SMTPHOST, ea.smtpHost);
		cv.put(EmailAccountColumns.SMTPPORT, ea.smtpPort);
		cv.put(EmailAccountColumns.POPHOST, ea.popHost);
		cv.put(EmailAccountColumns.POPPORT, ea.popPort);
		cv.put(EmailAccountColumns.USER, ea.user);
		cv.put(EmailAccountColumns.PASSWORD, ea.password);

		return db.update(DATABASE_EMAIL_ACCOUNTS_TABLE, cv,
				EmailAccountColumns.ID + "=" + id, null) > 0;
	}
}
