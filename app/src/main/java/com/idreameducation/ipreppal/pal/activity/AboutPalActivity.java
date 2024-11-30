package com.idreameducation.ipreppal.pal.activity;

import static com.google.firebase.firestore.Query.Direction.ASCENDING;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.AboutPALModel;
import com.idreameducation.ipreppal.pal.adapter.AboutPALAdapter;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AboutPalActivity extends AppCompatActivity {

    public static String ABOUTPAL="About_PAL";
    public static String FAQ="PAL_FAQ";
    String TAG="AboutPALActivity";
    Context context;
    Global global;
    RecyclerView recyclerview;
    TextView titleTextView;
    ArrayList<AboutPALModel> questionsList;


    LinearLayout searchLayout;

    EditText searchEdittext;

    Switch languageSwitch;


    ImageView searchBtn;

    private String pageLanguage="english";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_about_pal);

        init();
        setStaticText();
    }

    private void init() {
        context=this;
        global= (Global) getApplicationContext();

        titleTextView=findViewById(R.id.titleTextView);
        recyclerview=findViewById(R.id.recyclerview);
        searchEdittext=findViewById(R.id.searchEdittext);
        languageSwitch=findViewById(R.id.languageSwitch);
        searchLayout=findViewById(R.id.searchLayout);
        searchBtn=findViewById(R.id.searchBtn);

        pageLanguage=Util.getSelectedLanguage(context);

        if(Util.getPageType(context).equals(FAQ)) {
            titleTextView.setText("Frequently asked Questions");
            recyclerview.setLayoutManager(new LinearLayoutManager(context));
        }
        else {
            titleTextView.setText("How iPrep PAL Works");
            if(Util.isPortraitMode(context)) recyclerview.setLayoutManager(new LinearLayoutManager(context));
                else recyclerview.setLayoutManager(new GridLayoutManager(context, 2));
        }
        getQuestions(null,Util.getPageType(context));

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!searchEdittext.getText().toString().trim().equals("")) getQuestions(searchEdittext.getText().toString().toLowerCase(),Util.getPageType(context));
                else {
                    getQuestions(null,Util.getPageType(context));
                    Toast.makeText(context, "Empty Field", Toast.LENGTH_SHORT).show();
                }
            }
        });

        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) pageLanguage="hindi";
                else pageLanguage="english";
                getQuestions(null,Util.getPageType(context));
            }
        });

    }

    private void setStaticText() {
        if(pageLanguage.equals("hindi")){
            languageSwitch.setChecked(true);
            searchEdittext.setHint("खोजे");
            if(Util.getPageType(context).equals(FAQ)) titleTextView.setText("सामान्य प्रश्न");
            else titleTextView.setText("iPrep PAL कैसे काम करता है");
        }else{
            languageSwitch.setChecked(false);
            searchEdittext.setHint("Search");
            if(Util.getPageType(context).equals(FAQ)) titleTextView.setText("Frequently asked Questions");
            else titleTextView.setText("How iPrep PAL Works");
        }
    }

    private void getQuestions(String tags,String key) {

        /** Change Page Language**/
        setStaticText();

        if(!Util.checkInternetConnection(context)) {
            searchLayout.setVisibility(View.GONE);
            getQuestionsOffline(key);
            return;
        }


        /** init firestore ref */
        FirebaseFirestore db=FirebaseFirestore.getInstance();

        /** Create a reference to the support collection */
        CollectionReference citiesRef = db.collection(key);

        /** Create a query against the collection. */
        Query query;
        if(tags!=null) query= citiesRef.whereEqualTo("language",pageLanguage).whereArrayContains("tags",tags).orderBy("id", ASCENDING);
        else query= citiesRef.whereEqualTo("language",pageLanguage).orderBy("id", ASCENDING);

        /** init list & adapter */
        questionsList=new ArrayList<>();
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                questionsList.clear();
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    try {
                        AboutPALModel model = d.toObject(AboutPALModel.class);
                        model.setSelected(false);
                        questionsList.add(model);
                        HashMap<String,Object> columnFilterMap = new ObjectMapper().readValue(String.valueOf(model), new TypeReference<HashMap<String,Object>>() {});

                        System.out.println("-");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

//                try {
//                    JSONObject columnFilterMap = new ObjectMapper().readValue(String.valueOf(questionsList), new TypeReference<JSONObject>() {});
//
//                    System.out.println("- columnFilterMap "+columnFilterMap);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                saveArrayListtoJSON(questionsList);
//                try {
//                    JSONArray columnFilterMap = new ObjectMapper().readValue(String.valueOf(questionsList), new TypeReference<JSONArray>() {});
//
//                    System.out.println("- columnFilterMap "+columnFilterMap);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                String json = questionsList.toString();

                AboutPALAdapter aboutPALAdapter=new AboutPALAdapter(questionsList,key);
                recyclerview.setAdapter(aboutPALAdapter); // online
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });
    }

    private void getQuestionsOffline(String key) {

        questionsList=new ArrayList<>();

        String filePath = ".iDream_content/offlinetab_PAL/aboutPAL.txt";            // file path
        JSONObject jsonObject = Util.readJsonFile(context, filePath);
        JSONObject jsonObject_ = null;
        try {
            jsonObject_ = jsonObject.getJSONObject(Util.getSelectedBoard(context));
            JSONObject jsonObject__ = jsonObject_.getJSONObject(pageLanguage);
            JSONArray jsonObject___ = jsonObject__.getJSONArray(key);

            for(int i=0;i<=jsonObject___.length()-1;i++) {

                JSONObject object = (JSONObject) jsonObject___.get(i);

                try {
                    AboutPALModel columnFilterMap = new ObjectMapper().readValue(String.valueOf(object), new TypeReference<AboutPALModel>() {});
//                    AboutPALModel model = d.toObject(AboutPALModel.class);
                    columnFilterMap.setSelected(false);
                    questionsList.add(columnFilterMap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AboutPALAdapter aboutPALAdapter=new AboutPALAdapter(questionsList,key);
        recyclerview.setAdapter(aboutPALAdapter); // offline

    }

    public void saveArrayListtoJSON(ArrayList<AboutPALModel> modelList) {
        JSONArray myJSONArray = new JSONArray();
        JSONObject obj = new JSONObject();
        for(AboutPALModel model : modelList){
            try {
                obj.put(model.getId(), model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            myJSONArray.put(obj);
        }

        String json = new Gson().toJson(myJSONArray);

        System.out.println("==== "+ myJSONArray);

    }
}