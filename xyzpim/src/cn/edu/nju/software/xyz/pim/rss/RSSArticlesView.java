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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx 2008-4-9 下午05:40:59
 * 
 */
public class RSSArticlesView extends ListActivity {
	private static final int ACTIVITY_CREATE = 1;

	private static final int REFRESH_M_ID = 0;
	private static final int RETURN_M_ID = 1;

	private long feedId;
	private List<Article> articleList;

	// /private List<String> articleTitle;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.rss_article_list);

		Bundle extras = getIntent().getExtras();
		if (extras != null)
			feedId = extras.getLong("FEEDID");
		fillData();
	}

	@SuppressWarnings( { "static-access", "unchecked" })
	/**
	 * 向界面填充数据
	 */
	private void fillData() {
		articleList = NewsDroidDB.getInstance(this).getArticles(feedId);
		List<String> articleTitle = new ArrayList<String>(articleList.size());
		int count = articleList.size();

		articleTitle.clear();
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
		menu.add(0, REFRESH_M_ID, R.string.refresh);
		menu.add(0, RETURN_M_ID, R.string.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {
		case REFRESH_M_ID:
			RSSHandler.getInstance().updateArticles(this,
					NewsDroidDB.getInstance(this).getFeed(feedId));
			fillData();
			break;
		case RETURN_M_ID:
			finish();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		openFeed(position);
	}

	private void openFeed(int position) {
		Intent openIntent = new Intent(this, RSSDescriptionView.class);
		openIntent.putExtra("ARTICLEID", articleList.get(position).ArticleId);
		startSubActivity(openIntent, ACTIVITY_CREATE);
	}

}
