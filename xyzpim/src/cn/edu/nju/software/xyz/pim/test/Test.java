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
package cn.edu.nju.software.xyz.pim.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import cn.edu.nju.software.xyz.pim.R;
import cn.edu.nju.software.xyz.pim.rss.Feed;
import cn.edu.nju.software.xyz.pim.rss.NewsDroidDB;
import cn.edu.nju.software.xyz.pim.rss.RSSHandler;

/**
 * @author xmx 2008-3-18 下午10:39:33
 * 
 */
public class Test extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle icicle) {
		setContentView(R.layout.housecalresult);
		super.onCreate(icicle);
		NewsDroidDB db = new NewsDroidDB(this);
		/*try {
			db.insertFeed("百度互联网焦点新闻", new URL(
					"http://news.baidu.com/n?cmd=1&class=internet&tn=rss"));
		} catch (Exception e) {

		}*/
		Feed feed = db.getFeeds().get(0);
		Log.i("XYZPIM", feed.Url.toString());
		RSSHandler rh = new RSSHandler();
		rh.updateArticles(this, feed);
		// Article a = db.getArticles(feed.FeedId).get(0);
		Log.i("XYZPIM", "" + "" + db.getArticles(feed.FeedId).size());
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
