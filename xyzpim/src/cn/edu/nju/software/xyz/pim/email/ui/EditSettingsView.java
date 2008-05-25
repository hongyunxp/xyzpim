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
package cn.edu.nju.software.xyz.pim.email.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Menu.Item;
import android.widget.CheckBox;
import android.widget.EditText;
import cn.edu.nju.software.xyz.pim.R;
import cn.edu.nju.software.xyz.pim.email.EmailAccount;
import cn.edu.nju.software.xyz.pim.email.EmailDB;

/**
 * @author xmx 2008-4-27 上午03:00:26
 * 
 */
public class EditSettingsView extends Activity {

	private static final int FINISH_M_ID = 0;
	private static final int RETURN_M_ID = 1;

	private EditText accountNameText;
	private EditText addrText;
	private CheckBox sslCheckBox;
	private EditText smtpHostText;
	private EditText smtpPortText;
	private EditText pop3HostText;
	private EditText pop3PortText;
	private EditText userNameText;
	private EditText passwordText;

	private Long a_id; // account id

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.email_settings);

		accountNameText = (EditText) findViewById(R.id.email_account_name_text);
		addrText = (EditText) findViewById(R.id.email_account_addr_text);
		sslCheckBox = (CheckBox) findViewById(R.id.email_ssl_checkbox);
		smtpHostText = (EditText) findViewById(R.id.email_smtp_host_text);
		smtpPortText = (EditText) findViewById(R.id.email_smtp_port_text);
		pop3HostText = (EditText) findViewById(R.id.email_pop3_host_text);
		pop3PortText = (EditText) findViewById(R.id.email_pop3_port_text);
		userNameText = (EditText) findViewById(R.id.email_username_text);
		passwordText = (EditText) findViewById(R.id.email_password_text);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			a_id = extras.getLong(EmailDB.EmailAccountColumns.ID);
			EmailAccount ea = EmailDB.getInstance(this).fetchEmailAccount(a_id);
			accountNameText.setText(ea.name);
			addrText.setText(ea.emailAddr);
			sslCheckBox.setChecked(ea.isSSL);
			smtpHostText.setText(ea.smtpHost);
			smtpPortText.setText(String.valueOf(ea.smtpPort));
			pop3HostText.setText(ea.popHost);
			pop3PortText.setText(String.valueOf(ea.popPort));
			userNameText.setText(ea.user);
			passwordText.setText(ea.password);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, FINISH_M_ID, R.string.finish, R.drawable.ok);
		menu.add(0, RETURN_M_ID, R.string.back, R.drawable.back);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		switch (item.getId()) {

		case FINISH_M_ID:
			String accountName = accountNameText.getText().toString();
			String addr = addrText.getText().toString();
			boolean isSSL = sslCheckBox.isChecked();
			String smtpHost = smtpHostText.getText().toString();
			String smtpPort = smtpPortText.getText().toString();
			String pop3Host = pop3HostText.getText().toString();
			String pop3Port = pop3PortText.getText().toString();
			String userName = userNameText.getText().toString();
			String password = passwordText.getText().toString();
			EmailAccount ea = new EmailAccount();
			ea.name = accountName;
			ea.emailAddr = addr;
			ea.isSSL = isSSL;
			ea.smtpHost = smtpHost;
			ea.smtpPort = Integer.parseInt(smtpPort);
			ea.popHost = pop3Host;
			ea.popPort = Integer.parseInt(pop3Port);
			ea.user = userName;
			ea.password = password;
			if (null != a_id)
				EmailDB.getInstance(this).updateEmailAccount(a_id, ea);
			else
				EmailDB.getInstance(this).createEmailAccount(ea);
			finish();
			break;
		case RETURN_M_ID:
			finish();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

}
