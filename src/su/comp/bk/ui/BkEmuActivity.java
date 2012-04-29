/*
 * Created: 16.02.2012
 *
 * Copyright (C) 2012 Victor Antonovich (v.antonovich@gmail.com)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package su.comp.bk.ui;

import java.io.IOException;

import su.comp.bk.R;
import su.comp.bk.arch.Computer;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Main application activity.
 */
public class BkEmuActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCompatibleOrientationMode();
        LayoutInflater layoutInflater = getLayoutInflater();
        View mainView = layoutInflater.inflate(R.layout.main, null);
        BkEmuView bkEmuView = (BkEmuView) mainView.findViewById(R.id.emu_view);
        Computer computer = new Computer();
        try {
            computer.configure(getResources(), Computer.Configuration.BK_0010_BASIC);
            bkEmuView.setComputer(computer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentView(mainView);
    }

	private void setCompatibleOrientationMode() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
	}

}