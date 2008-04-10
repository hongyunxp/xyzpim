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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author savio 2008-4-9 下午02:59:17
 * 
 */
public class RSSFeedsView extends ListActivity {
	private static final int ACTIVITY_CREATE = 1;

	private static final int NEW_M_ID = 0;
	private static final int OPEN_M_ID = 1;
	private static final int RETURN_M_ID = 2;
	private static final int DEL_M_ID = 3;

	private List<Feed> rssList;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.rss_list);
		NewsDroidDB rssDbAdp = new NewsDroidDB(this);
		rssList = new ArrayList<Feed>();
		rssList = rssDbAdp.getFeeds();
		fillData();

	}

	@SuppressWarnings( { "static-access", "unchecked" })
	/**
	 * 向界面填充数据
	 */
	private void fillData() {
		// 创建适配器
		int count = rssList.size();
		List<String> feedTitle = new ArrayList<String>(count);
		for (int index = 0; index < count; ++index) {
			feedTitle.add(rssList.get(index).Title);
		}
		ArrayAdapter feedsAdapter = new ArrayAdapter(this, R.layout.rss_row,
				feedTitle);
		this.setListAdapter(feedsAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, NEW_M_ID, R.string.create);
		menu.add(0, DEL_M_ID, R.string.del);
		menu.add(0, OPEN_M_ID, R.string.open);
		menu.add(0, RETURN_M_ID, R.string.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {

		case NEW_M_ID:
			Intent newURLIntent = new Intent(this, RSSURLEdit.class);
			startSubActivity(newURLIntent, ACTIVITY_CREATE);
			return true;
		case DEL_M_ID:
			NewsDroidDB rssDbAdp = new NewsDroidDB(this);
			rssDbAdp.deleteFeed(rssList.get((int) getListView()
					.getSelectedItemId()).FeedId);
			rssList = rssDbAdp.getFeeds();
			// mGroupDbAdp.deleteGroup(getListView().getSelectedItemId());
			fillData();
			return true;
		case OPEN_M_ID:
			openFeed((int) this.getListView().getSelectedItemId());
			return true;
		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		openFeed(position);
	}

	private void openFeed(int position) {
		Intent openIntent = new Intent(this, RSSArticlesView.class);
		openIntent.putExtra("FEEDID", rssList.get(position).FeedId);
		RSSHandler rh = new RSSHandler();
		rh.updateArticles(this, rssList.get(position));
		startSubActivity(openIntent, ACTIVITY_CREATE);
	}

	@Override
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
	}
}
