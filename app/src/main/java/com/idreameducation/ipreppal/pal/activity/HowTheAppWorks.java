package com.idreameducation.ipreppal.pal.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;

public class HowTheAppWorks extends AppCompatActivity {

    public static HowTheAppWorks activity;
    private TextView textViewofflineUsage;
    private TextView TextViewVideo;
    private Context context;
    private TextView textViewBooks;
    private TextView textViewProfile;
    private TextView textViewAnalytics;
    private TextView textViewPractice;
    private TextView textViewVideoLessons;
    private TextView textViewToys;
    private TextView textViewMastery;
    private TextView textViewPaper;
    private TextView textViewLifeSkills;
    private TextView textViewDummyText;

    private TextView textViewVideoLessonsDetails;
    private TextView textViewProfileDetails;
    private TextView textViewbooksDetails;
    private TextView textViewPracticeDetails;
    private TextView textViewMasteryDetails;
    private TextView textViewToysDetails;
    private TextView textViewPaperDetails;
    private TextView textViewLifeSkillsDetails;
    private TextView textViewofflineUsageDetails;
    private TextView textViewAnalyticsDetails;

    private String comingsoon;
    private Global global;
    private boolean isPLayoutOpened = false;
    private boolean isMLayoutOpened = false;
    private final boolean isOLayoutOpened = false;
    private boolean isALayoutOpened = false;
    private boolean isPPLayoutOpened = false;
    private int layoutPosition = 0;
    private boolean isDataLoaded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_how_the_app_works);
        assignIds();
        listners();
    }

    private void listners() {
        findViewById(R.id.practiceLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutPosition == 5) {
                    resetVisibility();
                    resetImages();
                    layoutPosition = 0;
                    return;
                }
                getDetails("Practice");
            }
        });

        findViewById(R.id.booksLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutPosition == 4) {
                    resetVisibility();
                    resetImages();
                    layoutPosition = 0;
                    return;
                }
                getDetails("Books");
            }
        });

        findViewById(R.id.lifeSkillsLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutPosition == 3) {
                    resetVisibility();
                    resetImages();
                    layoutPosition = 0;
                    return;
                }
                getDetails("Skills");
            }
        });

        findViewById(R.id.toysLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutPosition == 2) {
                    resetVisibility();
                    resetImages();
                    layoutPosition = 0;
                    return;
                }
                getDetails("Toys");
            }
        });

        findViewById(R.id.videoLessonsLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutPosition == 1) {
                    resetVisibility();
                    resetImages();
                    layoutPosition = 0;
                    return;
                }
                getDetails("VideoLessons");
            }
        });

        findViewById(R.id.TextViewVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextViewVideo.setEnabled(false);
                Util.showToast(context, comingsoon);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TextViewVideo.setEnabled(true);
                    }
                }, 2000);
            }
        });

        findViewById(R.id.profileLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutPosition == 9) {
                    resetVisibility();
                    resetImages();
                    layoutPosition = 0;
                    return;
                }
                getDetails("Profile");
            }
        });

        findViewById(R.id.AnalyticsLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutPosition == 8) {
                    resetVisibility();
                    resetImages();
                    layoutPosition = 0;
                    return;
                }
                getDetails("analytics");
            }
        });

        findViewById(R.id.TestPaperLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutPosition == 7) {
                    resetVisibility();
                    resetImages();
                    layoutPosition = 0;
                    return;
                }
                getDetails("test");
            }
        });

        findViewById(R.id.MasteryLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutPosition == 6) {
                    resetVisibility();
                    resetImages();
                    layoutPosition = 0;
                    return;
                }
                getDetails("Mastery");
            }
        });
    }

    private void assignIds() {
        activity = this;
        context = this;
        global = (Global) getApplicationContext();
        global.sendData("iDream Work", this.getClass().getName());
        textViewVideoLessonsDetails = findViewById(R.id.textViewVideoLessonsDetails);
        textViewPracticeDetails = findViewById(R.id.textViewPracticeDetails);
        textViewToysDetails = findViewById(R.id.textViewToysDetails);
        textViewMasteryDetails = findViewById(R.id.textViewMasteryDetails);
        textViewLifeSkillsDetails = findViewById(R.id.textViewLifeSkillsDetails);
        textViewPaperDetails = findViewById(R.id.textViewPaperDetails);
        textViewLifeSkills = findViewById(R.id.textViewLifeSkills);
        textViewbooksDetails = findViewById(R.id.textViewBooksDetails);
        textViewProfileDetails = findViewById(R.id.textViewProfileDetails);
        textViewofflineUsageDetails = findViewById(R.id.textViewofflineUsageDetails);
        textViewAnalyticsDetails = findViewById(R.id.textViewAnalyticsDetails);


        textViewAnalytics = findViewById(R.id.textViewAnalytics);
        textViewVideoLessons = findViewById(R.id.textViewVideoLessons);
        textViewPractice = findViewById(R.id.textViewPractice);
        textViewPaper = findViewById(R.id.textViewPaper);
        textViewBooks = findViewById(R.id.textViewBooks);
        textViewProfile = findViewById(R.id.textViewProfile);
        textViewMastery = findViewById(R.id.textViewMastery);
        textViewToys = findViewById(R.id.textViewToys);
        textViewofflineUsage = findViewById(R.id.textViewofflineUsage);
        textViewDummyText = findViewById(R.id.textViewDummyText);

        TextViewVideo = findViewById(R.id.TextViewVideo);

        //hideNavigationBar(getWindow());

        try {
            setStaticText();
            Util.setBackButton(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }

    private void resetImages() {
        textViewToys.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.plus_icon, 0);
        textViewMastery.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.plus_icon, 0);
        textViewofflineUsage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.plus_icon, 0);
        textViewPractice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.plus_icon, 0);
        textViewVideoLessons.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.plus_icon, 0);
        textViewPaper.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.plus_icon, 0);
        textViewLifeSkills.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.plus_icon, 0);
        textViewBooks.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.plus_icon, 0);
        textViewAnalytics.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.plus_icon, 0);
        textViewProfile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.plus_icon, 0);
    }

    private void resetVisibility() {

        textViewToysDetails.setVisibility(View.GONE);
        textViewMasteryDetails.setVisibility(View.GONE);
        textViewofflineUsageDetails.setVisibility(View.GONE);
        textViewPracticeDetails.setVisibility(View.GONE);
        //  textViewVideoLessons.setVisibility(View.GONE);
        textViewVideoLessonsDetails.setVisibility(View.GONE);
        textViewLifeSkillsDetails.setVisibility(View.GONE);
        textViewPaperDetails.setVisibility(View.GONE);
        textViewbooksDetails.setVisibility(View.GONE);
        textViewAnalyticsDetails.setVisibility(View.GONE);
        textViewProfileDetails.setVisibility(View.GONE);
    }

    public void setStaticText() {

        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONArray array = object.getJSONArray("iPrep Work Activity");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if (textArrayList.size() > 0) {
                    comingsoon = textArrayList.get(0);
                    textViewDummyText.setText(textArrayList.get(1));
                    textViewAnalytics.setText(textArrayList.get(7));
                    textViewVideoLessons.setText(textArrayList.get(3));
                    textViewPractice.setText(textArrayList.get(9));
                    textViewToys.setText(textArrayList.get(4));
                    textViewBooks.setText(textArrayList.get(5));
                    textViewMastery.setText(textArrayList.get(12));
                    textViewLifeSkills.setText(textArrayList.get(13));
                    textViewPaper.setText(textArrayList.get(10));
                    textViewProfile.setText(textArrayList.get(11));
                    TextViewVideo.setText(textArrayList.get(6));
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Util.showDialog(context);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        isDataLoaded = true;
                        if (dataSnapshot != null) {
                            Util.dismissDialog();
                            ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                            comingsoon = textArrayList.get(0);
                            textViewDummyText.setText(textArrayList.get(1));
                            textViewAnalytics.setText(textArrayList.get(7));
                            textViewVideoLessons.setText(textArrayList.get(3));
                            textViewPractice.setText(textArrayList.get(9));
                            textViewToys.setText(textArrayList.get(4));
                            textViewBooks.setText(textArrayList.get(5));
                            textViewMastery.setText(textArrayList.get(12));
                            textViewLifeSkills.setText(textArrayList.get(13));
                            textViewPaper.setText(textArrayList.get(10));
                            textViewProfile.setText(textArrayList.get(11));

                            TextViewVideo.setText(textArrayList.get(6));


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Util.dismissDialog();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("iPrep Work Activity").addValueEventListener(valueEventListener);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isDataLoaded) {
                        Util.dismissDialog();
                        global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("iPrep Work Activity").removeEventListener(valueEventListener);
                        Util.showInternetConnectioError(context);
                    }
                }
            }, 8000);
        }


    }

    private void getDetails(final String type) {
        resetVisibility();
        resetImages();

        if (Util.isOfflineMode(context)) {
            String filePath = ".iDream_content/offlinetab_PAL/StaticTextDB.txt";
            JSONObject jsonObject = Util.readJsonFile(context, filePath);
            try {
                JSONObject object = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONArray array = object.getJSONArray("iPrep Work Detail Activity");
                ArrayList<String> textArrayList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String message = array.getString(i);
                    textArrayList.add(message);
                }
                if (textArrayList.size() > 0) {
                    String title1 = textArrayList.get(0);
                    String title2 = textArrayList.get(1);
                    String title3 = textArrayList.get(2);
                    String title4 = textArrayList.get(3);
                    String title5 = textArrayList.get(4);
                    String detail1 = textArrayList.get(5);
                    String detail2 = textArrayList.get(6);
                    String detail3 = textArrayList.get(7).replace("\n", "<br>");
                    String detail4 = textArrayList.get(8).replace("\n", "<br>");
                    String detail5 = textArrayList.get(9);
                    String detail6 = textArrayList.get(10).replace("\n", "<br>");

                    String detail7 = textArrayList.get(11).replace("\n", "<br>");
                    String detail8 = textArrayList.get(12).replace("\n", "<br>");
                    String detail9 = textArrayList.get(13).replace("\n", "<br>");
                    String detail10 = textArrayList.get(14).replace("\n", "<br>");


                    switch (type) {
                        case "VideoLessons":
                            textViewVideoLessonsDetails.setText(detail1);
                            textViewVideoLessonsDetails.setVisibility(View.VISIBLE);
                            isPLayoutOpened = true;
                            textViewVideoLessons.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                            layoutPosition = 1;
                            break;
                        case "Books":
                            textViewbooksDetails.setText(Html.fromHtml(detail3));
                            textViewbooksDetails.setVisibility(View.VISIBLE);
                            isPPLayoutOpened = true;
                            textViewBooks.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                            layoutPosition = 4;
                            break;
                        case "Skills":
                            textViewLifeSkillsDetails.setText(Html.fromHtml(detail6));
                            textViewLifeSkillsDetails.setVisibility(View.VISIBLE);
                            textViewLifeSkills.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                            layoutPosition = 3;
                            break;
                        case "Practice":
                            textViewPracticeDetails.setText(Html.fromHtml(detail7));
                            textViewPracticeDetails.setVisibility(View.VISIBLE);
                            textViewPractice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                            layoutPosition = 5;
                            break;
                        case "test":
                            textViewPaperDetails.setText(Html.fromHtml(detail9));
                            textViewPaperDetails.setVisibility(View.VISIBLE);
                            isPPLayoutOpened = true;
                            textViewPaper.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                            layoutPosition = 7;
                            break;
                        case "Toys":
                            textViewToysDetails.setText(detail2);
                            textViewToysDetails.setVisibility(View.VISIBLE);
                            isMLayoutOpened = true;
                            textViewToys.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                            layoutPosition = 2;
                            break;
                        case "Mastery":
                            textViewMasteryDetails.setText(Html.fromHtml(detail8));
                            textViewMasteryDetails.setVisibility(View.VISIBLE);
                            isMLayoutOpened = true;
                            textViewMastery.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                            layoutPosition = 6;
                            break;
                        case "analytics":
                            textViewAnalyticsDetails.setText(detail4);
                            textViewAnalyticsDetails.setVisibility(View.VISIBLE);
                            isALayoutOpened = true;
                            textViewAnalytics.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                            layoutPosition = 8;
                            break;
                        case "Profile":
                            textViewProfileDetails.setText(Html.fromHtml(detail10));
                            textViewProfileDetails.setVisibility(View.VISIBLE);
                            isALayoutOpened = true;
                            textViewProfile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                            layoutPosition = 9;
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (!Util.isNetworkAvailable(context) || Util.isOfflineMode(context)) {
                Util.showToast(context, Util.getCommonMessages(context).get(6));
                return;
            }

            Util.showDialog(context);
            global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child("iPrep Work Detail Activity").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        Util.dismissDialog();
                        if (dataSnapshot != null) {
                            ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                            String title1 = textArrayList.get(0);
                            String title2 = textArrayList.get(1);
                            String title3 = textArrayList.get(2);
                            String title4 = textArrayList.get(3);
                            String title5 = textArrayList.get(4);
                            String detail1 = textArrayList.get(5);
                            String detail2 = textArrayList.get(6);
                            String detail3 = textArrayList.get(7).replace("\n", "<br>");
                            String detail4 = textArrayList.get(8).replace("\n", "<br>");
                            String detail5 = textArrayList.get(9);
                            String detail6 = textArrayList.get(10).replace("\n", "<br>");

                            String detail7 = textArrayList.get(11).replace("\n", "<br>");
                            String detail8 = textArrayList.get(12).replace("\n", "<br>");
                            String detail9 = textArrayList.get(13).replace("\n", "<br>");
                            String detail10 = textArrayList.get(14).replace("\n", "<br>");


                            switch (type) {
                                case "VideoLessons":
                                    textViewVideoLessonsDetails.setText(detail1);
                                    textViewVideoLessonsDetails.setVisibility(View.VISIBLE);
                                    isPLayoutOpened = true;
                                    textViewVideoLessons.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                                    layoutPosition = 1;
                                    break;
                                case "Books":
                                    textViewbooksDetails.setText(Html.fromHtml(detail3));
                                    textViewbooksDetails.setVisibility(View.VISIBLE);
                                    isPPLayoutOpened = true;
                                    textViewBooks.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                                    layoutPosition = 4;
                                    break;
                                case "Skills":
                                    textViewLifeSkillsDetails.setText(Html.fromHtml(detail6));
                                    textViewLifeSkillsDetails.setVisibility(View.VISIBLE);
                                    textViewLifeSkills.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                                    layoutPosition = 3;
                                    break;
                                case "Practice":
                                    textViewPracticeDetails.setText(Html.fromHtml(detail7));
                                    textViewPracticeDetails.setVisibility(View.VISIBLE);
                                    textViewPractice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                                    layoutPosition = 5;
                                    break;
                                case "test":
                                    textViewPaperDetails.setText(Html.fromHtml(detail9));
                                    textViewPaperDetails.setVisibility(View.VISIBLE);
                                    isPPLayoutOpened = true;
                                    textViewPaper.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                                    layoutPosition = 7;
                                    break;
                                case "Toys":
                                    textViewToysDetails.setText(detail2);
                                    textViewToysDetails.setVisibility(View.VISIBLE);
                                    isMLayoutOpened = true;
                                    textViewToys.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                                    layoutPosition = 2;
                                    break;
                                case "Mastery":
                                    textViewMasteryDetails.setText(Html.fromHtml(detail8));
                                    textViewMasteryDetails.setVisibility(View.VISIBLE);
                                    isMLayoutOpened = true;
                                    textViewMastery.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                                    layoutPosition = 6;
                                    break;
                                case "analytics":
                                    textViewAnalyticsDetails.setText(detail4);
                                    textViewAnalyticsDetails.setVisibility(View.VISIBLE);
                                    isALayoutOpened = true;
                                    textViewAnalytics.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                                    layoutPosition = 8;
                                    break;
                                case "Profile":
                                    textViewProfileDetails.setText(Html.fromHtml(detail10));
                                    textViewProfileDetails.setVisibility(View.VISIBLE);
                                    isALayoutOpened = true;
                                    textViewProfile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.black_forward, 0);
                                    layoutPosition = 9;
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

    }

    @Override
    protected void onStop() {
        Util.setLogoutSelection(context,false);
        super.onStop();
    }

    @Override
    protected void onPause() {

        Util.preventPause(context,getTaskId());
        super.onPause();
    }

}