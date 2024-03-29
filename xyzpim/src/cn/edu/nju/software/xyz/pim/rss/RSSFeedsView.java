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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.edu.nju.software.xyz.pim.R;
import cn.edu.nju.software.xyz.pim.util.InputDialog;
import cn.edu.nju.software.xyz.pim.util.Log;
import cn.edu.nju.software.xyz.pim.util.OnInputOKListener;

/**
 * @author savio 2008-4-9 下午02:59:17
 * 
 */
public class RSSFeedsView extends ListActivity {
	private static final int ACTIVITY_OPEN = 1;

	private static final int NEW_M_ID = 0;
	private static final int OPEN_M_ID = 1;
	private static final int RETURN_M_ID = 2;
	private static final int DEL_M_ID = 3;

	private List<Feed> rssList;
	private List<String> feedTitle;
	@SuppressWarnings("unchecked")
	private ArrayAdapter feedsAdapter;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.rss_list);

		feedTitle = new ArrayList<String>();
		feedsAdapter = new ArrayAdapter(RSSFeedsView.this, R.layout.rss_row,
				feedTitle);
		setListAdapter(feedsAdapter);

		fillData();

	}

	@SuppressWarnings( { "static-access", "unchecked" })
	/**
	 * 向界面填充数据
	 */
	private void fillData() {
		final ProgressDialog pb = ProgressDialog.show(this, "Getting Feeds",
				"", true, false);

		final Handler handler = new Handler();
		final Runnable dissMiss = new Runnable() {
			public void run() {
				feedsAdapter = new ArrayAdapter(RSSFeedsView.this,
						R.layout.rss_row, feedTitle);
				setListAdapter(feedsAdapter);
				pb.dismiss();
			}
		};
		Thread getFeedsThread = new Thread(new Runnable() {

			@Override
			public void run() {
				NewsDroidDB rssDbAdp = NewsDroidDB
						.getInstance(RSSFeedsView.this);
				// rssList = new ArrayList<Feed>();
				rssList = rssDbAdp.getFeeds();
				// 创建适配器
				int count = rssList.size();
				feedTitle.clear();
				for (int index = 0; index < count; ++index) {
					feedTitle.add(rssList.get(index).Title);
				}
				handler.post(dissMiss);
			}

		});
		getFeedsThread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, NEW_M_ID, R.string.create, R.drawable.add);
		menu.add(0, DEL_M_ID, R.string.del, R.drawable.delete);
		menu.add(0, OPEN_M_ID, R.string.open, R.drawable.open);
		menu.add(0, RETURN_M_ID, R.string.back, R.drawable.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {

		case NEW_M_ID:
			InputDialog rssURLDialog = new InputDialog(this,
					new OnInputOKListener() {
						@Override
						public void onInputOK(String inputString) {
							String rssURLString = inputString;
							URL url = null;
							try {
								url = new URL(rssURLString);
							} catch (MalformedURLException e) {
								Log.e(e.getMessage());
							}
							RSSHandler rh = RSSHandler.getInstance();
							rh.createFeed(RSSFeedsView.this, url);
							NewsDroidDB rssDbAdp = NewsDroidDB
									.getInstance(RSSFeedsView.this);
							rssList = rssDbAdp.getFeeds();
							fillData();

						}
					});
			rssURLDialog.show("Create a Feed", "Input the feed URL", "http://");
			return true;
		case DEL_M_ID:
			NewsDroidDB rssDbAdp = NewsDroidDB.getInstance(this);
			rssDbAdp.deleteFeed(rssList.get((int) getListView()
					.getSelectedItemId()).FeedId);
			rssList = rssDbAdp.getFeeds();
			// mGroupDbAdp.deleteGroup(getListView().getSelectedItemId());
			fillData();
			return true;
		case OPEN_M_ID:
			openFeed((int) getListView().getSelectedItemId());
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
		/*RSSHandler rh = RSSHandler.getInstance();
		rh.updateArticles(this, rssList.get(position));*/
		startSubActivity(openIntent, ACTIVITY_OPEN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			String data, Bundle extras) {
		super.onActivityResult(requestCode, resultCode, data, extras);
		switch (requestCode) {

		}
	}
}
