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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
	// private TextView loanResultText;
	private Button calculateButton;

	@Override
	protected void onCreate(Bundle icicle) {
		setContentView(R.layout.housecal);

		loanTypeSppiner = (Spinner) findViewById(R.id.loan_type);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.loan_type, android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		loanTypeSppiner.setAdapter(adapter);
		loanTypeSppiner.setOnItemSelectedListener(this);
		loanAmountText = (EditText) findViewById(R.id.loan_amount);
		loanMonthText = (EditText) findViewById(R.id.loan_month);
		loanRateText = (EditText) findViewById(R.id.loan_rate);
		// loanResultText = (TextView) this.findViewById(R.id.loan_result);
		calculateButton = (Button) findViewById(R.id.house_calculate);
		calculateButton.setOnClickListener(this);

		super.onCreate(icicle);
	}

	@Override
	public void onClick(View arg0) {
		if (null != arg0) {
			// 处理计算事件
			if (arg0 == calculateButton) {
				if ("".equals(loanAmountText.getText().toString().trim())
						|| "".equals(loanMonthText.getText().toString().trim())
						|| "".equals(loanRateText.getText().toString().trim()))
					return;

				// 构造房贷计算输入信息
				Martgage m = new Martgage();
				m.loan = Double
						.parseDouble(loanAmountText.getText().toString());
				m.month = Integer.parseInt(loanMonthText.getText().toString());
				m.rate = Double.parseDouble(loanRateText.getText().toString());

				LoanInfo info = new LoanInfo(); // 结果信息
				Calc calc = new Calc();
				calc.execute(m, info); // 计算

				Intent i = new Intent(this, HouseLoanResult.class);

				// 附加要传递的结果信息
				i.putExtra("loan_amount", info.loan);
				i.putExtra("repay_amount", info.totalPay);
				i.putExtra("interest_amount", info.bornNumber);
				i.putExtra("loan_month", info.month);
				i.putExtra("repay_per_month", info.payForMonth);

				startSubActivity(i, 0);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemSelected(AdapterView parent, View v, int position, long id) {
		if (0 == id) {
			loanRateText.setText("0.015");
		} else if (1 == id) {
			loanRateText.setText("0.001");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onNothingSelected(AdapterView arg0) {
		// Nothing!
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
		info.totalPay = start / end * mart.month;
		info.bornNumber = info.totalPay - info.loan;
		info.payForMonth = start / end;
	}
}