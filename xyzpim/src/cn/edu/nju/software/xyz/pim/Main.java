/*
 * XYZPIM, A pim on Android Platform
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
package cn.edu.nju.software.xyz.pim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * @author xmx 2008-3-5 下午08:02:15
 * 
 */
public class Main extends Activity implements OnClickListener {

	private ImageButton contactsButton;
	private ImageButton organizerButton;
	private ImageButton emailButton;
	private ImageButton financeButton;
	private ImageButton rssButton;

	// private ImageButton testButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		contactsButton = (ImageButton) findViewById(R.id.contactsButton);
		organizerButton = (ImageButton) findViewById(R.id.organizerButton);
		emailButton = (ImageButton) findViewById(R.id.emailButton);
		financeButton = (ImageButton) findViewById(R.id.financeButton);
		rssButton = (ImageButton) findViewById(R.id.rssButton);
		// testButton = (ImageButton) findViewById(R.id.testButton);
		contactsButton.setOnClickListener(this);
		organizerButton.setOnClickListener(this);
		emailButton.setOnClickListener(this);
		financeButton.setOnClickListener(this);
		rssButton.setOnClickListener(this);
		// testButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		if (null != arg0) {
			if (arg0 == contactsButton) {
				Intent i = new Intent(this,
						cn.edu.nju.software.xyz.pim.contacts.GroupsView.class);
				startSubActivity(i, 0);
			} else if (arg0 == organizerButton) {
				Intent i = new Intent(this,
						cn.edu.nju.software.xyz.pim.organizer.Organizer.class);
				startSubActivity(i, 0);
			} else if (arg0 == emailButton) {
				Intent i = new Intent(
						this,
						cn.edu.nju.software.xyz.pim.email.ui.EmailAccountList.class);
				startSubActivity(i, 0);

			} else if (arg0 == financeButton) {
				Intent i = new Intent(this,
						cn.edu.nju.software.xyz.pim.finance.Finance.class);
				startSubActivity(i, 0);
			} else if (arg0 == rssButton) {
				Intent i = new Intent(this,
						cn.edu.nju.software.xyz.pim.rss.RSSFeedsView.class);
				startSubActivity(i, 0);
			} /*else if (arg0 == testButton) {
						String str = null;
						Log.i(str);
						Intent i = new Intent(this,
								cn.edu.nju.software.xyz.pim.test.Test.class);
						startSubActivity(i, 0);
						InputDialog id = new InputDialog(this, new OnInputListener() {

							@Override
							public void onInput(String inputString) {
								Log.i(inputString);

							}

						});
						id.show("Input", "Input your name!");
						Log.i("good");
					}*/
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			String data, Bundle extras) {

	}

}