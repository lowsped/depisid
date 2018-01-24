package com.deviceid.mac.changer;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class HomeActivity extends AppCompatActivity {
    String device_id = "";
    com.deviceid.mac.changer.Mpermission mpermission;
    Button permission_graanted;
    Button button;
    InterstitialAd mInterstitialAd;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_home);
        AdRequest adRequest = new AdRequest.Builder().build();

        if(!isConnected(HomeActivity.this)) buildDialog(HomeActivity.this).show();
        else {
            Toast.makeText(HomeActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1142409875326374/8016259466");
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdOpened() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdLeftApplication() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdClosed() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });


        this.mpermission = new Mpermission(this);
        this.permission_graanted = (Button) findViewById(R.id.perm_granted);
        this.permission_graanted.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (HomeActivity.this.mpermission.checkPermissionForCallPhone()) {
                    HomeActivity.this.callCode();
                    return;
                }
                HomeActivity.this.permission_graanted.setVisibility(0);
                HomeActivity.this.mpermission.requestPermissionForCallPhone(0);
            }
        });
        if (this.mpermission.checkPermissionForCallPhone()) {
            callCode();
        } else {
            this.mpermission.requestPermissionForCallPhone(0);
        }
    }

    public void callCode() {
        this.permission_graanted.setVisibility(8);
        try {
            this.device_id = Secure.getString(getContentResolver(), "android_id");
            ((TextView) findViewById(R.id.device_id)).setText(this.device_id);
            ((TextView) findViewById(R.id.imei)).setText(((TelephonyManager) getSystemService("phone")).getDeviceId());
            TelephonyManager telemamanger = (TelephonyManager) getSystemService("phone");
            String getSimSerialNumber = telemamanger.getSimSerialNumber();
            String getSubsciberId = telemamanger.getSubscriberId();
            String getSimCompany = telemamanger.getSimOperatorName();
            TextView sim_com = (TextView) findViewById(R.id.sim_company);
            TextView sim_sub = (TextView) findViewById(R.id.sim_subscriber);
            ((TextView) findViewById(R.id.sim_serial)).setText(getSimSerialNumber);
            sim_com.setText(getSimCompany);
            sim_sub.setText(getSubsciberId);
            WifiInfo wInfo = ((WifiManager) getSystemService("wifi")).getConnectionInfo();
            String macAddress = wInfo.getMacAddress();
            String ipAddress = Formatter.formatIpAddress(wInfo.getIpAddress());
            TextView ip = (TextView) findViewById(R.id.ip_address);
            ((TextView) findViewById(R.id.mac_address)).setText(macAddress);
            ip.setText(ipAddress);
            ((TextView) findViewById(R.id.mobile_display)).setText(Build.DISPLAY);
            ((TextView) findViewById(R.id.mobile_hardware)).setText(Build.HARDWARE);
            ((TextView) findViewById(R.id.mobile_fingure_print)).setText(Build.FINGERPRINT);
            ((TextView) findViewById(R.id.mobile_manufacturer)).setText(Build.MANUFACTURER);
            ((TextView) findViewById(R.id.mobile_model)).setText(Build.MODEL);
        } catch (Exception e) {
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == 0) {
                    callCode();
                    return;
                }
                return;
            default:
                return;
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;

    }
    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("connect with INTERNET, when opening the app. thank you.. ");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        return builder;
    }
}
