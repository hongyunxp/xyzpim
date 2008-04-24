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
package cn.edu.nju.software.xyz.pim.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx 2008-4-24 上午10:42:58
 * 
 */
public class InputDialog extends Dialog {

	protected TextView messageText;
	protected EditText inputText;

	// public String inputString;
	private Button okButton;
	private Button cancelButton;

	private final OnInputOKListener oil;

	public InputDialog(Context context, OnInputOKListener oil) {
		super(context);
		this.oil = oil;
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		setCancelable(true);
		setContentView(R.layout.input_dialog);

		messageText = (TextView) findViewById(R.id.input_message);
		inputText = (EditText) findViewById(R.id.input_text);

		okButton = (Button) findViewById(R.id.input_ok);
		cancelButton = (Button) findViewById(R.id.input_cancel);

		okButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				oil.onInputOK(inputText.getText().toString());
				dismiss();
			}

		});

		cancelButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// inputString = null;
				cancel();
			}

		});
	}

	public void show(String title, String message) {
		show();
		setTitle(title);
		messageText.setText(message);

	}

	public void show(String title, String message, String preInput) {
		show();
		setTitle(title);
		messageText.setText(message);
		inputText.setText(preInput);

	}

}
