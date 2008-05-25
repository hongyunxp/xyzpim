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

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
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

	private static final int RETURN_M_ID = 0;
	private static final int REFRESH_M_ID = 1;
	private double rate[][] = null;

	private Button moreInfoButton;

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
		moreInfoButton = (Button) findViewById(R.id.exchage_rate_more_info);

		calButton.setOnClickListener(this);
		moreInfoButton.setOnClickListener(this);

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
			} else if (arg0 == moreInfoButton) {
				if (rate == null) {
					try {
						rate = CurrencyAdp.getCurrency();
					} catch (Exception e) {
						new AlertDialog.Builder(this).setTitle(
								"Warning: " + e.getMessage())
								.setNegativeButton(R.string.back,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {

												/* User clicked Yes so do some stuff */
											}
										}).show();
					}
				}
				StringBuilder rateHtml = new StringBuilder();
				String mTypes[] = getResources().getStringArray(
						R.array.money_type);
				rateHtml.append("<html><table>");
				rateHtml.append("<tr>");
				rateHtml.append("<td>" + "</td>");
				for (int i = 0; i < mTypes.length; ++i) {
					rateHtml.append("<td>" + mTypes[i] + "</td>");
				}
				rateHtml.append("</tr>");

				for (int i = 0; i < rate.length; ++i) {
					rateHtml.append("<tr>");
					rateHtml.append("<td>" + mTypes[i] + "</td>");
					for (int j = 0; j < rate[i].length; ++j) {
						rateHtml.append("<td>" + rate[i][j] + "</td>");
					}
					rateHtml.append("</tr>");
				}
				rateHtml.append("</table></html>");

				String rateHtmlStr = rateHtml.toString();

				Intent i = new Intent(this,
						cn.edu.nju.software.xyz.pim.finance.ShowRate.class);
				i.putExtra("RATEHTML", rateHtmlStr);
				startSubActivity(i, 0);
			}
		}

	}

	private double excute(int x, int y, double ammount) {
		double result = 0;
		if (rate == null) {
			try {
				rate = CurrencyAdp.getCurrency();
				result = ammount * rate[x][y];
			} catch (Exception e) {
				new AlertDialog.Builder(this).setTitle(
						"Warning: " + e.getMessage()).setNegativeButton(
						R.string.back, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								/* User clicked Yes so do some stuff */
							}
						}).show();
			}
		} else
			result = ammount * rate[x][y];
		return result;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, REFRESH_M_ID, R.string.update_rate, R.drawable.refresh);
		menu.add(0, RETURN_M_ID, R.string.back, R.drawable.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {

		case REFRESH_M_ID:

			try {
				rate = CurrencyAdp.getCurrency();
			} catch (IOException e) {
				new AlertDialog.Builder(this).setTitle(
						"Warning: " + e.getMessage()).setNegativeButton(
						R.string.back, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								/* User clicked Yes so do some stuff */
							}
						}).show();
			}
			return true;
		case RETURN_M_ID:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
