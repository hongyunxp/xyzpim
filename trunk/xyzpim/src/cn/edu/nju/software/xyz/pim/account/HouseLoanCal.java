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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx Mar 18, 2008 5:46:48 PM
 * 
 */
public class HouseLoanCal extends Activity implements OnClickListener,
		OnItemSelectedListener {
	private Spinner loanTypeSppiner;
	private EditText loanAmountText;
	private EditText loanMonthText;
	private EditText loanRateText;
	private TextView loanResultText;
	private Button calculateButton;

	@Override
	protected void onCreate(Bundle icicle) {
		setContentView(R.layout.housecal);
		loanTypeSppiner = (Spinner) this.findViewById(R.id.loan_type);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.loan_type, android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		loanTypeSppiner.setAdapter(adapter);
		loanTypeSppiner.setOnItemSelectedListener(this);
		loanAmountText = (EditText) this.findViewById(R.id.loan_amount);
		loanMonthText = (EditText) this.findViewById(R.id.loan_month);
		loanRateText = (EditText) this.findViewById(R.id.loan_rate);
		loanResultText = (TextView) this.findViewById(R.id.loan_result);
		calculateButton = (Button) this.findViewById(R.id.house_calculate);
		calculateButton.setOnClickListener(this);
		super.onCreate(icicle);
	}

	@Override
	public void onClick(View arg0) {
		if (null != arg0) {
			if (arg0 == calculateButton) {
				Martgage m = new Martgage();
				m.loan = Double
						.parseDouble(loanAmountText.getText().toString());
				m.month = Integer.parseInt(loanMonthText.getText().toString());
				m.rate = Double.parseDouble(loanRateText.getText().toString());
				LoanInfo info = new LoanInfo();
				Calc calc = new Calc();
				calc.execute(m, info);
				double pay = info.payForMonth;
				loanResultText.setText("" + pay);
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView parent, View v, int position, long id) {
		TextView tv = (TextView) v;
		Log.v(this.toString(), String.valueOf(tv.getText()));
	}

	@Override
	public void onNothingSelected(AdapterView arg0) {
		// TODO Auto-generated method stub

	}
}

class LoanInfo {
	int month = 0;
	double loan = 0;
	double totalPay = 0;
	double bornNumber = 0;
	double payForMonth = 0;
}

class Martgage {
	int month = 0;
	double rate = 0;
	double loan = 0;
}

class Calc {

	void execute(Martgage mart, LoanInfo info) {

		double start = mart.loan * mart.rate;
		double end = 1;

		for (int i = mart.month; i > 0; i--) {
			start *= (1 + mart.rate);
		}
		for (int j = mart.month; j > 0; j--) {
			end *= (1 + mart.rate);
		}
		end = end - 1;
		info.month = mart.month;
		info.loan = mart.loan;
		info.totalPay = start;
		info.bornNumber = info.totalPay - info.loan;
		info.payForMonth = start / end;
	}
}