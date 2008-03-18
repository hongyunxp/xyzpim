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
package cn.edu.nju.software.xyz.pim.account;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx Mar 18, 2008 5:46:48 PM
 * 
 */
public class HouseLoanCal extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle icicle) {
		// 下拉框里添加选项的方法可参考APIDEMO中的com.google.android.samples.view.Spinner1
		setContentView(R.layout.housecal);
		super.onCreate(icicle);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
