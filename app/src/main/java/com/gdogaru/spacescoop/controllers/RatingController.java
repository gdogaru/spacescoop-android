package com.gdogaru.spacescoop.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.gdogaru.spacescoop.R;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.gdogaru.spacescoop.controllers.AnalyticsHelper.Event.RATE_LATER;
import static com.gdogaru.spacescoop.controllers.AnalyticsHelper.Event.RATE_NOW;

@Singleton
public class RatingController {
    private static final int VIEWS_UNTIL = 5;
    private final AnalyticsHelper analyticsHelper;
    private final AppSettingsController settingsController;

    @Inject
    public RatingController(AppSettingsController settingsController, AnalyticsHelper analyticsHelper) {
        this.settingsController = settingsController;
        this.analyticsHelper = analyticsHelper;
    }

    public void logUsage(Context context) {
        int usage = settingsController.getAppViews();
        if (usage >= 0) {
            settingsController.setAppViews(++usage);
        }
    }

    public void tryToRate(Context context) {
        int usage = settingsController.getAppViews();
        if (usage >= VIEWS_UNTIL) {
            showRateDialog(context);
            settingsController.setAppViews(-1);
        }
    }

    public void showRateDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.rate, null);
        Button rateButton = view.findViewById(R.id.rateButton);
        Button laterButton = view.findViewById(R.id.laterButton);
        Button neverButton = view.findViewById(R.id.neverButton);
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setView(view, 1, 1, 1, 1);
        setRateButtonClickListener(dialog, rateButton, context);
        setLaterButtonClickListener(dialog, laterButton);
        setNeverButtonClickListener(dialog, neverButton);
        dialog.show();
    }

    public void goToPlay(Context context) {
        settingsController.setAppViews(-1);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        context.startActivity(intent);
    }

    private void setRateButtonClickListener(final AlertDialog dialog, Button rateButton, final Context context) {
        rateButton.setOnClickListener(v -> {
            analyticsHelper.logEvent(RATE_NOW, 1);
            dialog.dismiss();
            goToPlay(context);
        });
    }

    private void setLaterButtonClickListener(final AlertDialog dialog, Button laterButton) {
        laterButton.setOnClickListener(v -> {
            analyticsHelper.logEvent(RATE_LATER, 1);
            dialog.dismiss();
            settingsController.setAppViews(0);
        });
    }

    private void setNeverButtonClickListener(final AlertDialog dialog, Button neverButton) {
        neverButton.setOnClickListener(v -> {
            settingsController.setAppViews(-1);
            dialog.dismiss();
        });
    }
}
