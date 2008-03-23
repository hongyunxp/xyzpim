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
import android.util.Log;
import android.widget.TextView;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx 2008-3-22 上午01:49:57
 * 
 */
public class HouseLoanResult extends Activity {

	private TextView loanAmountText;
	private TextView repayAmountText;
	private TextView interestAmountText;
	private TextView loanMonthText;
	private TextView repayPerMonthText;

	@Override
	protected void onCreate(Bundle icicle) {
		this.setContentView(R.layout.housecalresult);

		loanAmountText = (TextView) findViewById(R.id.loan_amount_text);
		repayAmountText = (TextView) findViewById(R.id.repay_amount_text);
		interestAmountText = (TextView) findViewById(R.id.interest_amount_text);
		loanMonthText = (TextView) findViewById(R.id.loan_month_text);
		repayPerMonthText = (TextView) findViewById(R.id.repay_per_month_text);

		loanAmountText.setText(""
				+ getIntent().getExtras().getDouble("loan_amount"));
		repayAmountText.setText(""
				+ getIntent().getExtras().getDouble("repay_amount"));
		interestAmountText.setText(""
				+ getIntent().getExtras().getDouble("interest_amount"));
		loanMonthText
				.setText("" + getIntent().getExtras().getInt("loan_month"));
		repayPerMonthText.setText(""
				+ getIntent().getExtras().getDouble("repay_per_month"));

		Log.i("XYZPIM", "" + getIntent().getExtras().getString("loan_amount"));

		super.onCreate(icicle);
	}

}
