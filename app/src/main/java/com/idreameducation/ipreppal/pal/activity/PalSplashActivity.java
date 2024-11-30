package com.idreameducation.ipreppal.pal.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.idreameducation.ipreppal.util.Util.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.idreameducation.ipreppal.util.Util.isTablet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.ServiceCloseBar;
import com.idreameducation.ipreppal.SetDefaultLauncher;
import com.idreameducation.ipreppal.deviceIdService.DeviceIdentifier;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.activity.loginPages.PalActivationDetailActivity;
import com.idreameducation.ipreppal.pal.activity.loginPages.PalAnonymousLoginActivity;
import com.idreameducation.ipreppal.pal.activity.loginPages.PalLanguageSelectionActivity;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONObject;

import java.io.File;

public class PalSplashActivity extends Activity {
    private Context context;
    private FirebaseAuth firebaseAuth;
    private Global global;
    private String deviceId;
    private final String SAMPLE_ALIAS = "MYALIAS";

    ImageView logo;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        Util.makePrefered(this);
        setContentView(R.layout.activity_splashscreen);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseApp.initializeApp(this);

        String type;

        type = getIntent().getStringExtra("goto");

        if(type==null) type="HomePage";

        System.out.println("----- type  "+type);

        if(Util.isLauncherMode(this)) new SetDefaultLauncher(PalSplashActivity.this).launchHomeOrClearDefaultsDialog();

        try {
            assignIds();

            ActivityCompat.requestPermissions((Activity) context, new String[]{READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            verifyStoragePermissions(this);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void moveFurther() {
        try {
            String filePath = "/storage/emulated/0/iDream_content/offlinetab_PAL/AppLanguage.txt";
            JSONObject jsonObject = Util.readJsonFile(this, filePath);
            JSONObject object = jsonObject.getJSONObject("Language");
            String languageSelected = object.getString("name");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideStatusBar() {
        if (SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    private void assignIds() throws Exception {
        Intent serviceIntent = new Intent(this, ServiceCloseBar.class);
        try {
            startService(serviceIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        context = this;
        firebaseAuth = FirebaseAuth.getInstance();
        global = (Global) getApplicationContext();

        if(!isTablet(context)) {
            Util.setPortraitMode(context, true);
            Util.setPortraitView(this);
        }
        else {
            Util.setPortraitMode(context, false);
            Util.setLandscapeView(this);
        }

        logo=findViewById(R.id.logo);
        logo.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fadein);
        logo.startAnimation(animation);
//        if (Build.VERSION.SDK_INT >= 30){
//            if (!Environment.isExternalStorageManager()){
//                Intent getpermission = new Intent();
//                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                startActivity(getpermission);
//            }
//        }

//        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
//        surfaceView = new SurfaceView(this);
//        ViewGroup.LayoutParams layoutParams = new WindowManager.LayoutParams(1, 1,
//                Build.VERSION.SDK_INT < Build.VERSION_CODES.O ?
//                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY :
//                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
//                PixelFormat.TRANSLUCENT);
//        windowManager.addView(surfaceView, layoutP

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(this)) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, 0);
//            }
//        }
//        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/offlinetab_PAL/PALiDream.txt");
//        Util.setOfflineMode(context,file.exists());

        /** check Manage access permission for offline usage */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (Environment.isExternalStorageManager()){
//                // If you don't have access, launch a new activity to show the user the system's dialog
//                // to allow access to the external storage
//                accessStoragePermissionGranted();
//            }else{
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
//                intent.setData(uri);
//                try {
//                    startActivity(intent);
//                } catch (ActivityNotFoundException e) {
//                    accessStoragePermissionGranted();
//                }
//            }
//        }

        /** in case of mobile view we don't need to have manage access permission so we can proceed without it */
        if(Util.isPortraitMode(context)) accessStoragePermissionGranted();

        Util.setIsFullScreen(context,false);

    }

    private void moveToFurther() {
        String type;

        type = getIntent().getStringExtra("goto");

        if(type==null) type="HomePage";

        // SRN Login

//        if(FirebaseAuth.getInstance().getCurrentUser() == null || Util.getUserId(context) == null) startActivity(new Intent(context, PalSRNLogin.class).putExtra("flow", "no"));
//        else if (Util.getSelectedLanguage(context) == null) startActivity(new Intent(context, PalLanguageSelectionActivity.class));
//        else startActivity(new Intent(context, PracticeTopicActivity.class).putExtra("goto",type));

//        finish();

//  APP ID code

        System.out.println("---type "+type);

        if (firebaseAuth.getCurrentUser() != null) {
            if (Util.isActivationDone(context))
                if (Util.getForPal(context).equalsIgnoreCase("true")) {
                    String uID = Util.getUserId(context);
                    if (uID == null) startActivity(new Intent(context, PalAnonymousLoginActivity.class).putExtra("flow", "no"));
                    else {
                        if (Util.getSelectedClass(context) != null) startActivity(new Intent(context, PracticeTopicActivity.class));
                        else {
                            if (Util.getUsername(context)!=null) startActivity(new Intent(context, PalAnonymousLoginActivity.class).putExtra("flow", "no"));
                            else startActivity(new Intent(context, PalLanguageSelectionActivity.class));
                        }
                    }
                } else startActivity(new Intent(context, PalAnonymousLoginActivity.class).putExtra("flow", "no"));
            else startActivity(new Intent(context, PalActivationDetailActivity.class));

        } else {
//            if (!Util.isActivationDone(context)) startActivity(new Intent(context, PalSRNLogin.class));
            if (!Util.isActivationDone(context)) startActivity(new Intent(context, PalActivationDetailActivity.class));
             else {
                String uID = Util.getUserId(context);
                if (uID == null) startActivity(new Intent(context, PalAnonymousLoginActivity.class).putExtra("flow", "no"));
                 else {
                    if (Util.getSelectedClass(context) != null) startActivity(new Intent(context, PracticeTopicActivity.class).putExtra("goto",type));
                    else {
                        if (Util.getUsername(context)==null) startActivity(new Intent(context, PalAnonymousLoginActivity.class).putExtra("flow", "no"));
                        else startActivity(new Intent(context, PalLanguageSelectionActivity.class));
                    }
                }
            }
        }

        finish();


    }

    private void accessStoragePermissionGranted() {
        if(Util.isPortraitMode(context)) new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    String serialNumber = DeviceIdentifier.getPseudoID();
                    System.out.println(" ======== serialNumber "+serialNumber);
                    Util.setAndroidId(context, serialNumber);
                    // Intialise Database reference and setting it globally
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    global.setDatabaseReference(databaseReference);


                    if(Util.isOfflineMode(context)) {
                        File file = new File(Util.getSDCardPath(context) + "/.iDream_content/offlinetab_PAL/PALiDream.txt");
                        if(file.exists()) new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (Util.isPermissionGranted(context)) moveToFurther();
//                                    else ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_PHONE_STATE, String.valueOf(MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)}, 123);
//                                else ActivityCompat.requestPermissions((Activity) context,
//                                        new String[]{
//                                                Manifest.permission.READ_EXTERNAL_STORAGE,
//                                                Manifest.permission.MANAGE_EXTERNAL_STORAGE
//                                        }, 123
//                                );
                            }
                        }, 500);
                        else Toast.makeText(context, "Offline folder not found !", Toast.LENGTH_LONG).show();
                    }
                    else new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (Util.isPermissionGranted(context)) moveToFurther();
                            else ActivityCompat.requestPermissions((Activity) context, new String[]{READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS, String.valueOf(MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)}, 123);
                        }
                    }, 500);
                } catch (Exception e) {
                    e.printStackTrace();
                    Util.showToast(context, e.getLocalizedMessage());
                }
            }
        }, 1500);
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionn = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
//            );
        }

        if (permissionn != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    MY_PERMISSIONS_REQUEST_WERITE_EXTERNAL_STORAGE
//            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123: {
//                 If request is cancelled, the result arrays are empty.

                if (SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    System.out.println("================ true");
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Util.setPermissonGranted(context, true);
                        if (deviceId == null) {
                            String serialNumber = DeviceIdentifier.getPseudoID();
                            Util.setAndroidId(context, serialNumber);
                        }
                        moveToFurther();
                    } else {
                        Toast.makeText(context, "Permission denied to read your External storage", Toast.LENGTH_LONG).show();


                    }

                }
                else {
                    System.out.println("================ false");
                    Util.setPermissonGranted(context, true);
                    if (deviceId == null) {
                        String serialNumber = DeviceIdentifier.getPseudoID();
                        Util.setAndroidId(context, serialNumber);
                    }
                    moveToFurther();
                    return;
                }

            }
        }
    }

    @SuppressLint("MissingPermission")
    public String getIMEIDeviceId(Context context) {

        if (SDK_INT >= Build.VERSION_CODES.Q) deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
         else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (SDK_INT >= Build.VERSION_CODES.M)
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) return "";
                    assert mTelephony != null;
            if (mTelephony.getDeviceId() != null)
                if (SDK_INT >= Build.VERSION_CODES.O) deviceId = mTelephony.getImei();
                else deviceId = mTelephony.getDeviceId();
             else deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        Log.d("deviceId", deviceId);
        return deviceId;
    }

    @Override
    protected void onPause() {

        Util.preventPause(context,getTaskId());
        super.onPause();
    }

    @Override
    public void onAttachedToWindow() {
//        getWindow().setType(WindowManager.LayoutParams.TY);
        super.onAttachedToWindow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("======= clickedc "+keyCode);
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
