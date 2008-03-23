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
import android.provider.Contacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * @author xmx 2008-3-5 下午08:02:15
 * 
 */
public class Main extends Activity implements OnClickListener {

	private ImageButton contactsButton;
	private ImageButton fairButton;
	private ImageButton accountButton;
	private ImageButton testButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		contactsButton = (ImageButton) findViewById(R.id.contactsButton);
		fairButton = (ImageButton) findViewById(R.id.fairButton);
		accountButton = (ImageButton) findViewById(R.id.accountButton);
		testButton = (ImageButton) findViewById(R.id.testButton);
		contactsButton.setOnClickListener(this);
		fairButton.setOnClickListener(this);
		accountButton.setOnClickListener(this);
		testButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (null != arg0) {
			if (arg0 == contactsButton) {
				startSubActivity(new Intent(Intent.VIEW_ACTION,
						Contacts.People.CONTENT_URI), 0);
			} else if (arg0 == fairButton) {
				Intent i = new Intent(this,
						cn.edu.nju.software.xyz.pim.fair.Fair.class);
				startSubActivity(i, 0);
			} else if (arg0 == accountButton) {
				Intent i = new Intent(this,
						cn.edu.nju.software.xyz.pim.account.Account.class);
				startSubActivity(i, 0);
			} else if (arg0 == testButton) {
				Intent i = new Intent(this,
						cn.edu.nju.software.xyz.pim.test.Test.class);
				startSubActivity(i, 0);
			}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode,
			String data, Bundle extras) {

	}

}