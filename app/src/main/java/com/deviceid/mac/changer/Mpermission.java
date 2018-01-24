package com.deviceid.mac.changer;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Mpermission {
    Activity activity;

    public Mpermission(Activity activity) {
        this.activity = activity;
    }

    public boolean checkPermissionForCallPhone() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_PHONE_STATE") == 0) {
            return true;
        }
        return false;
    }

    public void requestPermissionForCallPhone(final int PERMISSIONCODE) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.CALL_PHONE")) {
            showMessageOKCancel("You need to allow access to READ_PHONE_STATE", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(Mpermission.this.activity, new String[]{"android.permission.READ_PHONE_STATE", "android.permission.INTERNET", "android.permission.ACCESS_WIFI_STATE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.GET_ACCOUNTS"}, PERMISSIONCODE);
                }
            });
            return;
        }
        ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_PHONE_STATE", "android.permission.INTERNET", "android.permission.ACCESS_WIFI_STATE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.GET_ACCOUNTS"}, PERMISSIONCODE);
    }

    private void showMessageOKCancel(String message, OnClickListener okListener) {
        new Builder(this.activity).setMessage(message).setPositiveButton("OK", okListener).setNegativeButton("Cancel", null).create().show();
    }
}
