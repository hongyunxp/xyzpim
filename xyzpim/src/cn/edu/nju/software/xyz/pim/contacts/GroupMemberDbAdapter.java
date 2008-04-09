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
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * 记录分组成员信息，一个分组中有多个成员，一个成员也可以属于多个分组 该类提供加入成员，删除成员，列出指定分组成员的操作
 * 
 * @author xmx 2008-4-7 下午08:20:18
 * 
 */
public class GroupMemberDbAdapter {

	public static final String COL_ROWID = "_id";
	public static final String COL_GID = "gid";
	public static final String COL_CID = "cid";

	/**
	 * Database creation sql statement
	 */
	private static final String TABLE_CREATE = "create table groupmembers (_id integer primary key autoincrement, gid integer not null, cid integer not null);";

	private static final String DATABASE_NAME = "contacts";
	private static final String DATABASE_TABLE = "groupmembers";
	private static final int DATABASE_VERSION = 2;

	private SQLiteDatabase mDb;
	private final Context mCtx;

	public GroupMemberDbAdapter(Context ctx) {
		mCtx = ctx;
	}

	public GroupMemberDbAdapter open() {
		try {
			mDb = mCtx.openDatabase(DATABASE_NAME, null);
		} catch (FileNotFoundException e) {
			try {
				mDb = mCtx.createDatabase(DATABASE_NAME, DATABASE_VERSION, 0,
						null);
				mDb.execSQL(TABLE_CREATE);
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

	public void addMember(int gid, int cid) {
		Cursor c = mDb.query(DATABASE_TABLE, new String[] { "COL_GID",
				"COL_CID" }, "gid=" + gid + "and cid=" + cid, null, null, null,
				null);
		// if语句十分重要，否则会重复插入
		if (0 != c.count()) {
			ContentValues values = new ContentValues();
			values.put(COL_GID, gid);
			values.put(COL_CID, cid);
			mDb.insert(DATABASE_TABLE, null, values);
		}
	}

	public void delMember(int gid, int cid) {
		mDb.delete(DATABASE_TABLE, "gid=" + gid + "and cid=" + cid, null);
	}

	public List<Integer> listMembers(int gid) {
		Cursor c = mDb.query(DATABASE_TABLE, new String[] { "COL_CID" }, "gid="
				+ gid, null, null, null, null);
		int count = c.count();
		List<Integer> list = new ArrayList<Integer>();
		for (int index = 0; index < count; ++index) {
			c.moveTo(index);
			int cid = c.getInt(c.getColumnIndex(COL_CID));
			list.add(cid);
		}
		return null;
	}
}
