package com.gdogaru.spacescoop.view.main.credits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.view.common.BaseFragment;
import com.gdogaru.spacescoop.view.main.HasTitle;


public class CreditsFragment extends BaseFragment implements HasTitle {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_credits, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView information = view.findViewById(R.id.creditsWebView);
        information.getSettings().setJavaScriptEnabled(true);
        information.loadUrl("file:///android_asset/credits.html");
    }

    @Override
    public String getTitle() {
        return getString(R.string.credits);
    }
}
