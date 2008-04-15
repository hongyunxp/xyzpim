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
package cn.edu.nju.software.xyz.pim.fair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import cn.edu.nju.software.xyz.pim.R;

/**
 * @author xmx 2008-3-12 下午03:12:07
 * 
 */
public class Fair extends Activity implements OnClickListener {

	private ImageButton scheduleButton;
	private ImageButton meetingButton;
	private ImageButton noteButton;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.fair);
		scheduleButton = (ImageButton) this.findViewById(R.id.schedule);
		scheduleButton.setOnClickListener(this);

		meetingButton = (ImageButton) this.findViewById(R.id.meeting);
		meetingButton.setOnClickListener(this);

		noteButton = (ImageButton) this.findViewById(R.id.note);
		noteButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		if (null != arg0) {
			if (arg0 == scheduleButton) {
				Intent i = new Intent(this,
						cn.edu.nju.software.xyz.pim.fair.TaskView.class);
				startSubActivity(i, 0);
			} else if (arg0 == meetingButton) {
				Intent i = new Intent(this,
						cn.edu.nju.software.xyz.pim.fair.MeetingView.class);
				startSubActivity(i, 0);
			} else if (arg0 == noteButton) {
				Intent i = new Intent(this,
						cn.edu.nju.software.xyz.pim.fair.NoteView.class);
				startSubActivity(i, 0);
			}
		}
	}

}
