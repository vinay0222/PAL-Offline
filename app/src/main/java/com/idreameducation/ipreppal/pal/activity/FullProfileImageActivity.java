package com.idreameducation.ipreppal.pal.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;

/**
 * Created by apple on 03/05/18.
 */

public class FullProfileImageActivity extends AppCompatActivity {
    private ImageView imageView,crossImageView;
    private Context context;
    private Global global;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_profile_image);
        context = this;
        global = (Global) getApplicationContext();
            global.sendData("Full Image", this.getClass().getName());
        imageView = findViewById(R.id.imageView);
        crossImageView = findViewById(R.id.crossImageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        crossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
     //   imageView.setMaxZoom(4f);


        if(Util.getImageURL(context)!=null) {
            Glide.with(context)
                    .load(Util.getImageURL(context))
                    .into(imageView);
        }
        else {
            String link = "https://download.iprep.in/super_app_content/assessment_images/";
            String offlinelink = Util.getSDCardPath(context) + "/.iDream_content/AssessmentImages_"+Util.getSelectedLanguage(context).toLowerCase()+"/";
            try{
                /* Set Image on ImageView*/
                if (global.getFullImage() != null) {
                    if(Util.isOfflineMode(context)){
                        File file = new File(offlinelink+global.getFullImage());
                        Uri uri = Uri.fromFile(file);

                        Glide.with(context)
                                .load(uri)
                                .into(imageView);
                    }else {
                        Glide.with(context)
                                .load(global.getFullImage())
                                .error(Glide.with(context).load(link+global.getFullImage()).error(
                                        Glide.with(context).load(offlinelink+global.getFullImage())
                                ))
                                .into(imageView);
                    }

                }
                else {
                    Glide.with(context)
                            .load(Util.getUserProfileUrl(context))
                            .into(imageView);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            global.setFullImage(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Util.setImageURL(context,null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }

    @Override
    protected void onStop() {
        Util.setLogoutSelection(context,false);
        super.onStop();
    }
}
