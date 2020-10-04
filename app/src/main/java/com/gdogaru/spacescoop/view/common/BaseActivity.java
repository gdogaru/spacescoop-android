/*
 * Copyright (c) 2019 Gabriel Dogaru - gdogaru@gmail.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.gdogaru.spacescoop.view.common;

import androidx.appcompat.app.AppCompatActivity;

import com.gdogaru.spacescoop.R;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * @author Gabriel Dogaru (gdogaru@gmail.com)
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    protected Bus bus;

    public boolean isLandscapeTablet() {
        return getResources().getBoolean(R.bool.isLandscapeTablet);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        bus.unregister(this);
        super.onPause();
    }
}
