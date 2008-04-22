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

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Menu.Item;
import android.webkit.WebView;
import android.widget.TextView;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author savio 2008-4-9 下午09:57:12
 * 
 */
public class RSSDescriptionView extends Activity {

	private static final int RETURN_M_ID = 0;

	private long articleId;
	private Article article;
	private TextView titleText;
	private TextView dateText;
	private WebView descriptionWebView;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.rss_description);

		titleText = (TextView) findViewById(R.id.rss_article_title_lable);
		dateText = (TextView) findViewById(R.id.rss_article_date_lable);
		descriptionWebView = (WebView) findViewById(R.id.rss_article_description_content);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			articleId = extras.getLong("ARTICLEID");

		}

		fillData();
	}

	private void fillData() {
		NewsDroidDB rssDbAdp = NewsDroidDB.getInstance(this);
		article = rssDbAdp.getArticle(articleId);
		titleText.setText(article.Title);
		dateText.setText(article.date);
		descriptionWebView.loadData(article.Discription, "text/html", "UTF-8");
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
}
