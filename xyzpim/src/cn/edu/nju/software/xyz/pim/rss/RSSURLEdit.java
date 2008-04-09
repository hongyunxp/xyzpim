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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx 2008-4-9 下午05:06:49
 * 
 */
public class RSSURLEdit extends Activity implements OnClickListener {
	private EditText urlText;
	private Button okButton;
	private Button cancelButton;

	private String rssURLString;

	// private Long rowId;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.rss_url_edit);

		urlText = (EditText) findViewById(R.id.rss_url_text);
		okButton = (Button) findViewById(R.id.rss_url_ok);
		cancelButton = (Button) findViewById(R.id.rss_url_cancel);

		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		if (null != arg0) {
			if (arg0 == okButton) {
				rssURLString = urlText.getText().toString();
				Bundle bundle = new Bundle();
				bundle.putString("RSSURL", rssURLString);
				/*if (null != rowId)
					bundle.putLong(GroupsDbAdapter.COL_ROWID, rowId);*/
				setResult(RESULT_OK, null, bundle);
			} else if (arg0 == cancelButton) {
				setResult(RESULT_CANCELED, null, null);
			}
			finish();
		}
	}
}
