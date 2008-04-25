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
package cn.edu.nju.software.xyz.pim.rss;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import cn.edu.nju.software.xyz.pim.util.Log;

/**
 * @author savio 2008-4-8 下午09:09:47 Handles all the SQL Lite database work
 */
public class NewsDroidDB {
	// create the table of feeds
	private static final String CREATE_TABLE_FEEDS = "create table feeds (feed_id integer primary key autoincrement, "
			+ "title text not null, url text not null);";

	// create the table of articles
	private static final String CREATE_TABLE_ARTICLES = "create table articles (article_id integer primary key autoincrement, "
			+ "feed_id int not null, title text not null, url text not null,date text not null,discription not null);";

	private static final String FEEDS_TABLE = "feeds";
	private static final String ARTICLES_TABLE = "articles";
	private static final String DATABASE_NAME = "newdroiddb";
	private static final int DATABASE_VERSION = 1;

	private SQLiteDatabase db;

	private static NewsDroidDB ins;

	private NewsDroidDB() {
	}

	private NewsDroidDB(Context ctx) {
		try {
			db = ctx.openDatabase(DATABASE_NAME, null);
		} catch (FileNotFoundException e) {
			try {
				db = ctx.createDatabase(DATABASE_NAME, DATABASE_VERSION, 0,
						null);
				db.execSQL(CREATE_TABLE_FEEDS);
				db.execSQL(CREATE_TABLE_ARTICLES);
			} catch (FileNotFoundException e1) {
				db = null;
			}
		}
	}

	public synchronized static NewsDroidDB getInstance(Context ctx) {
		if (null == ins)
			ins = new NewsDroidDB(ctx);
		return ins;
	}

	public boolean insertFeed(String title, URL url) {
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("url", url.toString());
		return (db.insert(FEEDS_TABLE, null, values) > 0);
	}

	public boolean deleteFeed(Long feedId) {
		return (db.delete(FEEDS_TABLE, "feed_id=" + feedId.toString(), null) > 0)
				&& (db.delete(ARTICLES_TABLE, "feed_id=" + feedId.toString(),
						null) >= 0);
	}

	public boolean insertArticle(Long feedId, String title, URL url,
			String date, String discription) {
		ContentValues values = new ContentValues();
		values.put("feed_id", feedId);
		values.put("title", title);
		values.put("url", url.toString());
		values.put("date", date);
		values.put("discription", discription);
		return (db.insert(ARTICLES_TABLE, null, values) > 0);
	}

	public boolean deleteAricles(Long feedId) {
		return (db.delete(ARTICLES_TABLE, "feed_id=" + feedId.toString(), null) > 0);
	}

	public List<Feed> getFeeds() {
		ArrayList<Feed> feeds = new ArrayList<Feed>();
		try {
			Cursor c = db.query(FEEDS_TABLE, new String[] { "feed_id", "title",
					"url" }, null, null, null, null, null);

			int numRows = c.count();
			c.first();
			for (int i = 0; i < numRows; ++i) {
				Feed feed = new Feed();
				feed.FeedId = c.getLong(0);
				feed.Title = c.getString(1);
				feed.Url = new URL(c.getString(2));
				feeds.add(feed);
				c.next();
			}
		} catch (SQLException e) {
			Log.e(e.getMessage());
		} catch (MalformedURLException e) {
			Log.e(e.getMessage());
		}
		return feeds;
	}

	public Feed getFeed(long id) {
		Feed feed = new Feed();
		try {
			Cursor c = db.query(FEEDS_TABLE, new String[] { "feed_id", "title",
					"url" }, "feed_id=?", new String[] { String.valueOf(id) },
					null, null, null);

			int numRows = c.count();
			if (numRows != 1) {
				Log.e("newRows=" + String.valueOf(numRows));
			}
			c.first();
			feed.FeedId = c.getLong(0);
			feed.Title = c.getString(1);
			feed.Url = new URL(c.getString(2));
		} catch (SQLException e) {
			Log.e(e.getMessage());
		} catch (MalformedURLException e) {
			Log.e(e.getMessage());
		}
		return feed;
	}

	public List<Article> getArticles(Long feedId) {
		ArrayList<Article> articles = new ArrayList<Article>();
		try {
			Cursor c = db.query(ARTICLES_TABLE, new String[] { "article_id",
					"feed_id", "title", "url", "date", "discription" },
					"feed_id=" + feedId.toString(), null, null, null, null);

			int numRows = c.count();
			c.first();
			for (int i = 0; i < numRows; ++i) {
				Article article = new Article();
				article.ArticleId = c.getLong(0);
				article.FeedId = c.getLong(1);
				article.Title = c.getString(2);
				article.Url = new URL(c.getString(3));
				article.date = c.getString(4);
				article.Discription = c.getString(5);
				articles.add(article);
				c.next();
			}
		} catch (SQLException e) {
			Log.e(e.getMessage());
		} catch (MalformedURLException e) {
			Log.e(e.getMessage());
		}
		return articles;
	}

	public Article getArticle(Long articleId) {
		Article article = new Article();

		try {
			Cursor c = db.query(ARTICLES_TABLE, new String[] { "article_id",
					"feed_id", "title", "url", "date", "discription" },
					"article_id=" + articleId.toString(), null, null, null,
					null);

			int numRows = c.count();
			c.first();
			for (int i = 0; i < numRows; ++i) {
				article.ArticleId = c.getLong(0);
				article.FeedId = c.getLong(1);
				article.Title = c.getString(2);
				article.Url = new URL(c.getString(3));
				article.date = c.getString(4);
				article.Discription = c.getString(5);
				c.next();
			}
		} catch (SQLException e) {
			Log.e(e.getMessage());
		} catch (MalformedURLException e) {
			Log.e(e.getMessage());
		}
		return article;
	}

}
