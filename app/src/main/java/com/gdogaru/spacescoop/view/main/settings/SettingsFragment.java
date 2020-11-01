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

import androidx.annotation.NonNull;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.AppSettingsController;
import com.gdogaru.spacescoop.databinding.MainSettingsBinding;
import com.gdogaru.spacescoop.view.common.BaseFragment;
import com.gdogaru.spacescoop.view.main.HasTitle;

import javax.inject.Inject;


public class SettingsFragment extends BaseFragment implements HasTitle {
    @Inject
    AppSettingsController settingsController;
    private MainSettingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = MainSettingsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.wifiOnly.setChecked(settingsController.getUpdateWifiOnly());
        binding.notificationEnabled.setChecked(settingsController.getNotifyUpdates());

        binding.wifiOnly.setOnCheckedChangeListener((buttonView, isChecked) -> settingsController.setUpdateWifiOnly(isChecked));
        binding.notificationEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> settingsController.setNotifyUpdates(isChecked));
    }

    @Override
    public String getTitle() {
        return getString(R.string.settings);
    }
}
