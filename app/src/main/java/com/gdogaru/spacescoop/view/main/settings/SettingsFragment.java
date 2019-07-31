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

package com.gdogaru.spacescoop.view.main.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.AppSettingsController;
import com.gdogaru.spacescoop.view.common.BaseFragment;
import com.gdogaru.spacescoop.view.main.HasTitle;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingsFragment extends BaseFragment implements HasTitle {

    @BindView(R.id.wifi_only)
    Switch wifiOnlySwitch;
    @BindView(R.id.notification_enabled)
    Switch notifySwitch;

    @Inject
    AppSettingsController settingsController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        wifiOnlySwitch.setChecked(settingsController.getUpdateWifiOnly());
        notifySwitch.setChecked(settingsController.getNotifyUpdates());

        wifiOnlySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> settingsController.setUpdateWifiOnly(isChecked));
        notifySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> settingsController.setNotifyUpdates(isChecked));
    }

    @Override
    public String getTitle() {
        return getString(R.string.settings);
    }
}
