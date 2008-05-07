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
package cn.edu.nju.software.xyz.pim.finance;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author saviozhang 2008-5-7 下午07:55:10
 * 
 */
public class ShowRate extends Activity {

	// private double rate[][] = null;

	@Override
	protected void onCreate(Bundle icicle) {
		setContentView(R.layout.show_rate);

		WebView rwv = (WebView) findViewById(R.id.rate_info);
		String html = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			html = extras.getString("RATEHTML");

		}
		rwv.loadData(html, "text/html", "UTF-8");
		super.onCreate(icicle);
	}
}
