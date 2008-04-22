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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx Mar 18, 2008 5:49:10 PM
 * 
 */
public class ExchangeRateCal extends Activity implements OnClickListener {

	private Spinner money1Spinner;
	private Spinner money2Spinner;

	private EditText money1AmmountEditText;
	private EditText money2AmmountEditText;

	private Button calButton;

	// private Button moreInfoButton;

	@Override
	protected void onCreate(Bundle icicle) {
		// 下拉框里添加选项的方法可参考APIDEMO中的com.google.android.samples.view.Spinner1
		setContentView(R.layout.exchageratecal);

		money1AmmountEditText = (EditText) findViewById(R.id.money1amount);
		money2AmmountEditText = (EditText) findViewById(R.id.money2amount);

		money1Spinner = (Spinner) findViewById(R.id.money1type);
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
				this, R.array.money_type, android.R.layout.simple_spinner_item);
		adapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		money1Spinner.setAdapter(adapter1);

		money2Spinner = (Spinner) findViewById(R.id.money2type);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.money_type, android.R.layout.simple_spinner_item);
		adapter2
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		money2Spinner.setAdapter(adapter2);

		calButton = (Button) findViewById(R.id.exchange_rate_calculate);
		// moreInfoButton = (Button) findViewById(R.id.exchage_rate_more_info);

		calButton.setOnClickListener(this);

		super.onCreate(icicle);
	}

	@Override
	public void onClick(View arg0) {
		if (null != arg0) {
			if (arg0 == calButton) {
				if (""
						.equals(money1AmmountEditText.getText().toString()
								.trim()))
					return;

				int x = money1Spinner.getSelectedItemPosition();
				int y = money2Spinner.getSelectedItemPosition();
				double ammount = Double.parseDouble(money1AmmountEditText
						.getText().toString());
				money2AmmountEditText.setText("" + excute(x, y, ammount));
			}
		}

	}

	private double excute(int x, int y, double ammount) {
		double rate[][] = {
				{ 1, 32.7700, 123.330, 7.81590, 7.60250, 0.49630, 0.73410,
						1.04840, 1.16240, 31.2500 },
				{ 0.03052, 1, 3.76350, 0.23851, 0.23200, 0.01514, 0.02240,
						0.03199, 0.03547, 0.95362 },
				{ 0.00811, 0.26571, 1, 0.06337, 0.06164, 0.00402, 0.00595,
						0.00850, 0.00943, 0.25339 },
				{ 0.12794, 4.19274, 15.7794, 1, 0.97270, 0.06350, 0.09392,
						0.13414, 0.14872, 3.99826 },
				{ 0.13154, 4.31042, 16.2223, 1.02807, 1, 0.06528, 0.09656,
						0.13790, 0.15290, 4.11049 },
				{ 2.01491, 66.0286, 248.499, 15.7483, 15.3184, 1, 1.47915,
						2.11243, 2.34213, 62.9659 },
				{ 1.36221, 44.6397, 168.002, 0.6469, 10.3562, 0.67607, 1,
						1.42814, 1.58344, 42.5691 },
				{ 0.95383, 31.2572, 117.636, 7.45507, 7.25153, 0.47339,
						0.70021, 1, 1.10874, 29.8073 },
				{ 0.86029, 28.1917, 106.099, 6.72393, 6.54035, 0.42696,
						0.63154, 0.90193, 1, 26.8840 },
				{ 0.03200, 1.04864, 3.94656, 0.25011, 0.24328, 0.01588,
						0.02349, 0.03355, 0.03720, 1 }, };
		return ammount * rate[x][y];
	}

}
