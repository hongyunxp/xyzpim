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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx Mar 18, 2008 5:29:22 PM
 * 
 */
public class Finance extends Activity implements OnClickListener {
	private ImageButton houseloanButton;
	private ImageButton exchangerateButton;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.finance);
		houseloanButton = (ImageButton) findViewById(R.id.houseloanButton);
		exchangerateButton = (ImageButton) findViewById(R.id.exchangerateButton);
		houseloanButton.setOnClickListener(this);
		exchangerateButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		if (null != arg0) {
			if (arg0 == houseloanButton) {
				Intent i = new Intent(this,
						cn.edu.nju.software.xyz.pim.finance.HouseLoanCal.class);
				startSubActivity(i, 0);
			} else if (arg0 == exchangerateButton) {
				Intent i = new Intent(
						this,
						cn.edu.nju.software.xyz.pim.finance.ExchangeRateCal.class);
				startSubActivity(i, 0);
			}
		}

	}

}
