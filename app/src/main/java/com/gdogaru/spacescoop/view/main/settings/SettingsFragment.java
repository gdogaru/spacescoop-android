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
