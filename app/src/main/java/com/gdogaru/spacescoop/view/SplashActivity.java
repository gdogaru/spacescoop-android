package com.gdogaru.spacescoop.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gdogaru.spacescoop.alarm.AlarmHelper;
import com.gdogaru.spacescoop.view.main.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Gabriel on 7/21/2015.
 */
public class SplashActivity extends AppCompatActivity {
    private static final String NEEDED_PERM = "needed_perm";
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7002;
    boolean allPermissionsGranted = false;
    private ArrayList<String> permissionsNeeded;

    public static void start(Context context, boolean clean) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (clean) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(NEEDED_PERM)) {
            permissionsNeeded = savedInstanceState.getStringArrayList(NEEDED_PERM);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        checkAndRequestPermissions();
    }

    private void openMainActivity() {
        AlarmHelper.createNotificationChannel(this);
        MainActivity.start(this, true);
        finish();
    }

    private void checkAndRequestPermissions() {
        permissionsNeeded = getPermissionsNeeded();
        if (!permissionsNeeded.isEmpty()) {
            allPermissionsGranted = false;
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        } else {
            allPermissionsGranted = true;
            openMainActivity();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (permissionsNeeded != null) {
            outState.putStringArrayList(NEEDED_PERM, permissionsNeeded);
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                List<String> notGranted = new ArrayList<>(permissionsNeeded);
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        notGranted.remove(permissions[i]);
                    }
                }
                if (notGranted.size() > 0) {
                    if (getPermissionsNeeded().size() > 0) {
                        showDialogOK("The app cannot function without these permissions.",
                                (dialog, which) -> {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            checkAndRequestPermissions();
                                            break;
                                    }
                                });
                    }
                    allPermissionsGranted = false;
                } else {
                    allPermissionsGranted = true;
                    openMainActivity();
                }
            }
        }
    }

    private ArrayList<String> getPermissionsNeeded() {
        List<String> permissionsToCheck = Arrays.asList(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_WIFI_STATE);
        ArrayList<String> permissionsNeeded = new ArrayList<>();
        for (String permission : permissionsToCheck) {
            int permissionSendMessage = ContextCompat.checkSelfPermission(this, permission);
            if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
            }
        }
        return permissionsNeeded;
    }
}
