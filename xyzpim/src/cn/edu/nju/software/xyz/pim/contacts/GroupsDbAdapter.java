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

import java.io.FileNotFoundException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * 联系人分组的助手类，提供基本的增删改查操作，参照android教程练习三中NotesDbAdapter实现
 * 
 * @author xmx 2008-4-4 下午09:22:00
 */
public class GroupsDbAdapter {
	public static String COL_ROWID = "_id";
	public static String COL_NAME = "gname";

	/**
	 * Database creation sql statement
	 */
	private static final String TABLE_GROUPS = "create table groups (_id integer primary key autoincrement, "
			+ "gname text not null);";
	private static final String TABLE_GROUPMEM = "create table groupmembers (_id integer primary key autoincrement, gid integer not null, curi text not null);";

	private static final String DATABASE_NAME = "contactsdb";
	private static final String DATABASE_GROUPS_TABLE = "groups";
	// private static final String DATABASE_GROUPMEM_TABLE = "groupmembers";
	private static final int DATABASE_VERSION = 2;

	private SQLiteDatabase mDb;
	private final Context mCtx;

	public GroupsDbAdapter(Context ctx) {
		mCtx = ctx;
	}

	public GroupsDbAdapter open() throws SQLException {
		try {
			mDb = mCtx.openDatabase(DATABASE_NAME, null);
		} catch (FileNotFoundException e) {
			try {
				mDb = mCtx.createDatabase(DATABASE_NAME, DATABASE_VERSION, 0,
						null);
				mDb.execSQL(TABLE_GROUPS);
				mDb.execSQL(TABLE_GROUPMEM);
				// mDb.execSQL("insert into groups (gname)
				// values(\"Friends\")");
			} catch (FileNotFoundException e1) {
				throw new SQLException("Could not create database");
			}
		}
		return this;
	}

	public void close() {
		mDb.close();
	}

	public long createGroup(String name) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COL_NAME, name);
		return mDb.insert(DATABASE_GROUPS_TABLE, null, initialValues);
	}

	public boolean deleteGroup(long rowId) {
		// TODO: 风险！ rowId与sql定义语句中integer类型不符
		return mDb.delete(DATABASE_GROUPS_TABLE, COL_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllGroups() {
		return mDb.query(DATABASE_GROUPS_TABLE, new String[] { COL_ROWID,
				COL_NAME }, null, null, null, null, null);
	}

	public Cursor fetchGroup(long rowId) throws SQLException {
		Cursor result = mDb.query(true, DATABASE_GROUPS_TABLE, new String[] {
				COL_ROWID, COL_NAME }, COL_NAME + "=" + rowId, null, null,
				null, null);
		if ((result.count() == 0) || !result.first()) {
			throw new SQLException("No group matching ID: " + rowId);
		}
		return result;
	}

	public boolean updateGroup(long rowId, String name) {
		ContentValues args = new ContentValues();
		args.put(COL_NAME, name);
		return mDb.update(DATABASE_GROUPS_TABLE, args, COL_ROWID + "=" + rowId,
				null) > 0;
	}
}
