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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * @author xmx 2008-3-5 下午08:02:15
 * 
 */
public class Main extends Activity {
	private ImageButton contactsButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		contactsButton = (ImageButton) findViewById(R.id.contactsButton);
		contactsButton.setOnClickListener(new OnClickListener() {
			private boolean flag = true;

			@Override
			public void onClick(View arg0) {
				if (flag) {
					contactsButton.setImageResource(R.drawable.email);
					flag = false;
				} else {
					contactsButton.setImageResource(R.drawable.contacts);
					flag = true;
				}

			}

		});
	}
}