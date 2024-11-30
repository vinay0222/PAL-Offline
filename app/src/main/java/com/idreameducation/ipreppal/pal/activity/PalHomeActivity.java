package com.idreameducation.ipreppal.pal.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.fragments.BatchesFragments;
import com.idreameducation.ipreppal.util.Util;

public class PalHomeActivity extends AppCompatActivity {
    private Context context;
    private Global global;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_pal_home);
        assignIds();
    }
    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


//        String packageName = context.getPackageName();
//        ComponentName componentName = new ComponentName(packageName, PalHomeActivity.class + ".PalHomeActivity.alias");
//        PackageManager pm = getPackageManager();
//        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//
//        hideNavigationBar(getWindow());
//        PackageManager packageManager = context.getPackageManager();
//        ComponentName componentName1 = new ComponentName(context, PalHomeActivity.class);
//        packageManager.setComponentEnabledSetting(componentName1, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//
//
//        ComponentName componentName2 = new ComponentName(context,PalHomeActivity.class);
//        PackageManager pm2 = getPackageManager();
//        pm2.setComponentEnabledSetting(componentName2, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(selector);

//        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.batches){
                    openFragment();
                }
                return false;
            }


        });
    }

    private FragmentManager manager;
    private FragmentTransaction transaction;

    public void openFragment() {
        Fragment BatchFragment = new BatchesFragments();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.container, BatchFragment, "tag");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onPause() {

        Util.preventPause(context,getTaskId());
        super.onPause();
    }


    public static void hideNavigationBar(Window window) {
//        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
//
//        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//
//        // This work only for android 4.4+
//        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
//            window.getDecorView().setSystemUiVisibility(flags);
//
//            // Code below is to handle presses of Volume up or Volume down.
//            // Without this, after pressing volume buttons, the navigation bar will
//            // show up and won't hide
//            final View decorView = window.getDecorView();
//            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//                @Override
//                public void onSystemUiVisibilityChange(int visibility) {
//                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
//                        decorView.setSystemUiVisibility(flags);
//                    }
//                }
//            });
//        }
    }



}
