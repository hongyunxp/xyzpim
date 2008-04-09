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

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Menu.Item;
import android.widget.ArrayAdapter;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx 2008-4-9 下午05:40:59
 * 
 */
public class RSSArticlesView extends ListActivity {
	private static final int ACTIVITY_CREATE = 1;

	private static final int RETURN_M_ID = 0;

	private List<Article> articleList;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.rss_article_list);
		NewsDroidDB rssDbAdp = new NewsDroidDB(this);
		articleList = new ArrayList<Article>();
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			long feedId = extras.getLong("FEEDID");
			// RSSHandler rh = new RSSHandler();
			// rh.updateArticles(this,feedId );
			articleList = rssDbAdp.getArticles(feedId);

		}
		fillData();
	}

	@SuppressWarnings( { "static-access", "unchecked" })
	/**
	 * 向界面填充数据
	 */
	private void fillData() {
		// 创建适配器
		int count = articleList.size();
		List<String> articleTitle = new ArrayList<String>(count);

		for (int index = 0; index < count; ++index) {
			Integer INT = new Integer(index + 1);
			articleTitle.add(INT.toString() + "."
					+ articleList.get(index).Title);
		}
		ArrayAdapter feedsAdapter = new ArrayAdapter(this,
				R.layout.rss_article_row, articleTitle);
		this.setListAdapter(feedsAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, RETURN_M_ID, R.string.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {

		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}
	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	// @Override
	// protected void onListItemClick(ListView l, View v, int position, long id)
	// {
	// super.onListItemClick(l, v, position, id);
	// editGroupName(position);
	// }
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode,
			String data, Bundle extras) {
		super.onActivityResult(requestCode, resultCode, data, extras);
		// String groupName = extras.getString(GroupsDbAdapter.COL_NAME);
		switch (requestCode) {
		case ACTIVITY_CREATE:
			if (resultCode == RESULT_OK && null != extras) {
				String rssURLString = extras.getString("RSSURL");
				URL url = null;
				try {
					url = new URL(rssURLString);
				} catch (MalformedURLException e) {
					Log.e("XYZPIM", e.getLocalizedMessage(), e);
				}
				RSSHandler rh = new RSSHandler();
				rh.createFeed(this, url);
				NewsDroidDB rssDbAdp = new NewsDroidDB(this);
				rssList = rssDbAdp.getFeeds();
				fillData();
			}
			break;
		}
	}*/
}
