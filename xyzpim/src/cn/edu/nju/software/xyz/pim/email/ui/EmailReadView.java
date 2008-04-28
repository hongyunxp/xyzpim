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

import java.util.Iterator;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import cn.edu.nju.software.xyz.pim.R;
import cn.edu.nju.software.xyz.pim.email.ContentPart;
import cn.edu.nju.software.xyz.pim.email.EmailDB;
import cn.edu.nju.software.xyz.pim.email.EmailException;
import cn.edu.nju.software.xyz.pim.email.Message;
import cn.edu.nju.software.xyz.pim.util.Log;

/**
 * @author xmx 2008-4-27 下午07:59:47
 * 
 */
public class EmailReadView extends Activity {

	private TextView subjectText;
	private TextView datetimeText;
	private TextView fromText;
	private TextView toText;
	private WebView contentView;

	private Long m_id;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.email_read);

		subjectText = (TextView) findViewById(R.id.email_read_subject_text);
		datetimeText = (TextView) findViewById(R.id.email_read_date_time_text);
		fromText = (TextView) findViewById(R.id.email_read_from_text);
		toText = (TextView) findViewById(R.id.email_read_to_text);
		contentView = (WebView) findViewById(R.id.email_read_content);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			m_id = extras.getLong(EmailDB.EmailMessageColumns.ID);
		}

		if (null != m_id) {
			// Log.i(m_id);
			Message msg = EmailDB.getInstance(this).fetchEmailMessage(m_id);
			subjectText.setText(msg.subject);
			datetimeText.setText(msg.date);
			fromText.setText(msg.from);
			toText.setText(msg.to);
			try {
				// contentView.loadData(msg.content.getContentString(),
				// msg.content.getMIMEType(), "UTF-8");
				String contentStr = msg.content.getContentString();
				Log.i(contentStr);
				if (null != contentStr)
					contentView.loadData(contentStr, msg.content.getMIMEType(),
							"UTF-8");
				else {
					String mimeType = msg.content.getMIMEType();
					if ("multipart/alternative".equalsIgnoreCase(mimeType)
							|| "multipart/mixed".equalsIgnoreCase(mimeType)) {
						Iterator<ContentPart> it = msg.content
								.getSubContentPart().iterator();
						while (it.hasNext()) {
							ContentPart subCP = it.next();
							if ("text/html".equalsIgnoreCase(subCP
									.getMIMEType())
									|| "text/plain".equalsIgnoreCase(subCP
											.getMIMEType())) {
								contentView.loadData(subCP.getContentString(),
										subCP.getMIMEType(), "UTF-8");
							}
						}
					}
				}
			} catch (EmailException e) {
				Log.e(e.getMessage());
			}
		}
	}

}
