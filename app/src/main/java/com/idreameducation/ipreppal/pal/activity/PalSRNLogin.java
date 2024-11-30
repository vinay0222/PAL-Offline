package com.idreameducation.ipreppal.pal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.RegisterModel;
import com.idreameducation.ipreppal.model.StudentModel;
import com.idreameducation.ipreppal.model.UserInfoModel;
import com.idreameducation.ipreppal.pal.activity.loginPages.PalActivationDetailActivity;
import com.idreameducation.ipreppal.pal.activity.loginPages.PalAnonymousLoginActivity;
import com.idreameducation.ipreppal.pal.activity.loginPages.PalLanguageSelectionActivity;
import com.idreameducation.ipreppal.roomdatabase.model.StudentDetailsModel;
import com.idreameducation.ipreppal.roomdatabase.repository.StudentDetailsRepository;
import com.idreameducation.ipreppal.userActivities.UserActivities;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PalSRNLogin extends AppCompatActivity {

    Context context;
    Global global;

    EditText srnText,dobEditText;
    TextView loginwithAPPID;
    TextView loginBtn;
    private final String userType = "Pal_Users";
    ProgressDialog dialog;
    Dialog dialog2;
    private Switch languageSwitch;
    private String language;

    TextView textViewtitle,EnterNameHintTextView;

    private HashMap<String,Object> updatedStudentInfo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util.setKeyboardWindowSettings(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pal_srnlogin);
        init();
    }

    private void init() {
        context=this;
        global= (Global) getApplicationContext();
        new UserActivities(this,false);

        studentDetailsRepository = new StudentDetailsRepository(context);

        srnText=findViewById(R.id.srnText);
        loginBtn=findViewById(R.id.loginBtn);
        dobEditText=findViewById(R.id.dobEditText);
        loginwithAPPID=findViewById(R.id.loginwithAPPID);
        languageSwitch = findViewById(R.id.languageSwitch);

        textViewtitle = findViewById(R.id.textViewtitle);
        EnterNameHintTextView = findViewById(R.id.EnterNameHintTextView);

        if (Util.getSelectedLanguage(context).equals("english")) {
            textViewtitle.setText("Please Enter SRN Number");
            EnterNameHintTextView.setText("Enter SRN Number");
            loginBtn.setText("Login");
        } else {
            textViewtitle.setText("कृपया SRN नंबर दर्ज करें");
            EnterNameHintTextView.setText("SRN नंबर दर्ज करें");
            loginBtn.setText("लॉग इन करें");
        }

        if(Util.isActivationDone(context)) {
            if (Util.getSelectedLanguage(context).equals("hindi")) loginwithAPPID.setText("उपयोगकर्ता विवरण के साथ लॉगिन करें");
            else loginwithAPPID.setText("Login with User Details");
        }
        else {
            if (Util.getSelectedLanguage(context).equals("hindi")) loginwithAPPID.setText("APP ID के साथ लॉगिन करें");
            else loginwithAPPID.setText("Login with APP ID");
        }

        language=Util.getSelectedLanguage(context);

        if (Util.getSelectedLanguage(context).equals("english")) {
            languageSwitch.setChecked(false);
        } else {
            languageSwitch.setChecked(true);
        }

        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) language = "hindi";
                else language = "english";
                if (language != null) {

                    if (Util.getSelectedLanguage(context) != language) {
                        Util.setLanguagePackageSelection(context, language);
                        Util.setLanguageSelection(context, language);
                        Util.showAnimatedDialog(context,Util.LANGUAGE_UPDATED);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
//
                                startActivity(getIntent());
//
                            }
                        }, 3000);
                    }

                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                String srnNO=srnText.getText().toString().trim();
                String dob=dobEditText.getText().toString().trim();

                Util.preventTwoClick(v);
                showDialog();

                if(srnNO.equals("")) Toast.makeText(context, "Enter SRN number", Toast.LENGTH_SHORT).show();
                else if(dob.equals("")) Toast.makeText(context, "Enter DOB", Toast.LENGTH_SHORT).show();
                /** validating emp id with DOB */
                else validateEmpIDorDOB(srnNO,dob);

            }
        });

        loginwithAPPID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.isActivationDone(context)) startActivity(new Intent(PalSRNLogin.this, PalAnonymousLoginActivity.class));
                else startActivity(new Intent(PalSRNLogin.this, PalActivationDetailActivity.class));
            }
        });


    }


    /** check enter */
    private void validateEmpIDorDOB(String empid,String dob) {

        global.getDatabaseReference().child("/PAL_Haryana/students").child(empid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()!=null) {

                            updatedStudentInfo = (HashMap<String, Object>) dataSnapshot.getValue();

                            /** removing dob because its using for auth and its not be upgradable */
                            updatedStudentInfo.remove("DateOfBirth");

                            /** validating dob */
                            if(dataSnapshot.child("DateOfBirth").getValue().toString().substring(0,10).equals(dob))

                            /** Sending Employee info for authentication */
                            checkUserExist(Objects.requireNonNull(dataSnapshot.getValue(StudentModel.class)));

                            else {
                                dismissDialog();
                                Toast.makeText(context, "Invalid DOB", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            dismissDialog();
                            Toast.makeText(context, "Wrong EmpID", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Request Failed", Toast.LENGTH_SHORT).show();
                        dismissDialog();
                    }
                });
    }

    /** checking userAlready exist? */
    private void checkUserExist(StudentModel StudentModel) {
        FirebaseFirestore.getInstance().collection(Util.getProjectID()+"_students").document(StudentModel.getSRN()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) signInUser(StudentModel);          // start next activity if user is authenticated
                else authenticateUser(StudentModel);                            // authenticate user
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                dismissDialog();
            }
        });
    }

    private void signInUser(StudentModel StudentModel) {
        /** add latest login date */
        StudentModel.setLastLoginTime(System.currentTimeMillis());
        StudentModel.setIdreamEmail(StudentModel.getSRN()+"@idreameducation.org");
        FirebaseAuth.getInstance().signInWithEmailAndPassword(StudentModel.getIdreamEmail(), StudentModel.getDateOfBirth()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                /** update values in firestore */
                addUserInfoInFirestore(StudentModel,true);                          // add user info in firestore
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                dismissDialog();
            }
        });
    }

    /** Authenticate User  */
    private void authenticateUser(StudentModel StudentModel) {

        /** creating email with employee code and with email address */
        StudentModel.setIdreamEmail(StudentModel.getSRN()+"@idreameducation.org");
        StudentModel.setAuthTime(System.currentTimeMillis());
        StudentModel.setLastLoginTime(System.currentTimeMillis());
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(StudentModel.getIdreamEmail(), StudentModel.getDateOfBirth())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        addUserInfoInFirestore(StudentModel,false);                          // add user info in firestore
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                        /** checking data maybe deleted in firestore  */
                        if(e.getMessage().toString().contains("The email address is already in use by another account"))
                            signInUser(StudentModel);
                        else dismissDialog();
                    }
                });

    }

    /** add User info in Firestore
     * @param StudentModel employe model to set/update in firestore
     * @param updateDate set true if you want to update data else its set the data in firestore*/
    private void addUserInfoInFirestore(StudentModel StudentModel, boolean updateDate) {
        DocumentReference db=FirebaseFirestore.getInstance().collection(Util.getProjectID()+"_students").document(StudentModel.getSRN());

        OnSuccessListener onSuccessListener=new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                goToNextActivity(StudentModel);
            }
        };

        OnFailureListener onFailureListener=new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                /** if data is not available then add data in firestore */
                if(e.getMessage().toString().contains("No document to update")) addUserInfoInFirestore(StudentModel,false);
                else dismissDialog();
            }
        };

        if(updateDate) db.update(updatedStudentInfo).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
        else db.set(StudentModel).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);

    }

    private void goToNextActivity(StudentModel StudentModel) {
        dismissDialog();
        Util.setUserId(context,StudentModel.getSRN());                         // adding user id
        Util.setUsername(context,StudentModel.getStudentName());              // adding user name
        Util.setActivation(context,true);
        Util.setClassNameSelection(context,getStudentClass(StudentModel));
        Util.setClassSelection(context,getStudentClass(StudentModel).replace(" ","_").toLowerCase());

        Util.setDistrict(context,StudentModel.getSchoolDistrictName());
        Util.setSchoolName(context,StudentModel.getSchoolName());
        Util.setSchoolId(context,StudentModel.getSchoolCode());
        Util.setNGOID(context,StudentModel.getSchoolCode());

        // need to be dynamic
        Util.setBoardNameSelection(context,"cbse");
        Util.setBoardSelection(context,"cbse");
        Util.setLanguageSelection(context,"english");
        Util.setLanguagePackageSelection(context,"english");
//        Util.setID(context,Util.getProjectID());
//        Util.se(context,StudentModel.getSchoolCode());

        startActivity(new Intent(context, PracticeTopicActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    private String getStudentClass(StudentModel studentModel) {

        String classnumber=studentModel.getsClass();

        if(classnumber.equals("11") || classnumber.equals("12")) classnumber=classnumber+" "+studentModel.getStream();

        return classnumber;
    }

    private void showDialog() {
        if(dialog2!=null && dialog2.isShowing()) dialog2.dismiss();
        dialog2=new Dialog(context);
        dialog2.setContentView(R.layout.simple_dialog_view);
        dialog2.setCancelable(false);
        dialog2.show();
    }

    private void dismissDialog() {
        if(dialog2==null) return;
        dialog2.dismiss();
    }


    // API request below (not using)
    private void requestToken() {

        if(dialog!=null) {
            if(dialog.isShowing()) dialog.dismiss();
        }

        dialog =new ProgressDialog(this);
        dialog.setTitle("Login");
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        if(!Util.checkInternetConnection(context)) {
            Toast.makeText(context, "No Connection", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Username",srnText.getText().toString().trim());
            jsonObject.put("Password",srnText.getText().toString().trim());
            jsonObject.put("DeviceId","d4e59514-f1e1-42f4-9114-b990e6382ec4");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonObject.toString();

        StringRequest request = new StringRequest(Request.Method.POST, "https://api.avsarhry.in/api/eLearn/Authentication/StudentLogin", new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String token = jsonObject.getString("Token");
                    requestLogin(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                dialog.dismiss();
                Toast.makeText(context, "Invalid SRN", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("authorization", "Bearer pTRTscBrv4QAoHhL");
//                return params;
//            }
        };
        RequestQueue requestQueue;
        try {
            requestQueue = Volley.newRequestQueue(PalSRNLogin.this);
            request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
        } catch (NullPointerException o) {
            try {
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
            } catch (Exception e) {dialog.dismiss();}
        }

    }

    private void requestLogin(String token) {

        JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("Username","1508779340");
//            jsonObject.put("Password","1508779340");
//            jsonObject.put("DeviceId","d4e59514-f1e1-42f4-9114-b990e6382ec4");
        final String requestBody = jsonObject.toString();

        StringRequest request = new StringRequest(Request.Method.GET, "https://api.avsarhry.in/api/eLearn/Authentication/StudentProfile/"+srnText.getText().toString().trim(), new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    Util.setSRNUser(context,true);
                    JSONObject jsonObject11 = new JSONObject(response);
                    JSONObject jsonObject = jsonObject11.getJSONObject("ResponseData");
                    requestClassID(jsonObject.getString("ClassId"),jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(context, "No user found Please check SRN number", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                dialog.dismiss();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer "+token);
                return params;
            }
        };
        RequestQueue requestQueue;
        try {
            requestQueue = Volley.newRequestQueue(PalSRNLogin.this);
            request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
        } catch (NullPointerException o) {
            try {
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
            } catch (Exception e) {dialog.dismiss();}
        }

    }

    private void requestClassID(String classID,JSONObject jsonObject) throws JSONException {



        JSONObject jsonObject1111 = new JSONObject();

        final String requestBody = jsonObject1111.toString();

        StringRequest request = new StringRequest(Request.Method.GET, "https://api.avsarhry.in/api/eLearn/Assessment/GetReportCardStats?srnNo="+jsonObject.getString("SRNNo")+"&classId="+classID, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject33 = new JSONObject(response);
//                    jsonObject33.keys()
//                    JSONArray ResponseData = jsonObject33.getJSONArray("");
                    JSONArray jsonObject1 = jsonObject33.getJSONArray("ResponseData");
                    JSONObject jsonObject3 = jsonObject1.getJSONObject(0);
                    String className = jsonObject3.getString("ClassName");
                    String ngo[] = jsonObject3.getString("SchoolName").split("-");
                    String ngoID=ngo[0];
                    className=className.replace("Class","").trim();
                    Util.setNGOID(context,ngoID);
                    Util.setClassSelection(context,className);
                    Util.setClassNameSelection(context,className);
                    haveLanguage=true;
                    String district = jsonObject.getString("School_District");
//                    String ngoID = "1111111111";
                    String appID = jsonObject.getString("SRNNo");
                    String tabID = Util.getAndroidId(context);
                    String schoolID = "SchoolID";
                    String projectID = "projectID";
                    String schoolName = "schoolName";
                    String state = "state";
                    String projectname = "projectname";
                    String sBoard = "cbse";
                    String studentName = jsonObject.getString("FirstName");
                    String studentPassword = jsonObject.getString("Id");
                    String studentMobile = jsonObject.getString("SRNNo");

                    Util.setNGOID(context, ngoID);
                    Util.setAPPID(context, appID);
                    Util.setTABID(context, tabID);
                    Util.setSchoolId(context,schoolID);
                    Util.setProjectId(context,projectID);
                    Util.setSchoolName(context,schoolName);
                    Util.setDistrict(context,district);
                    Util.setStateSelection(context,state);
                    Util.setProjectName(context,projectname);
                    Util.setForPal(context, "true");
                    Util.setBoardSelectionDummy(context, sBoard);
                    Util.setBoardSelection(context, sBoard);
                    Util.setBoardNameSelectionDummy(context, sBoard);
                    Util.setBoardNameSelection(context, sBoard);

                    try {

                        Util.setUserMobile(context, studentMobile);
                        studentName=(studentName.replace(" ","_")).toLowerCase();
                        Util.setUsername(context, studentName);
                        rollNo_student = studentPassword;

                        if (!Util.isOfflineMode(context)) {
                            if (Util.isNetworkAvailable(context)) {
                                getUserDetails(studentName, studentMobile);
                            } else dialog.dismiss();
                        }
                        else dialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Please Try again", Toast.LENGTH_SHORT).show();

                        System.out.println("============ hiding 7");
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                    Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                    Log.i("jsonObjectRequest", "Error bytes: " + new String(error.networkResponse.data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("authorization", "Bearer pTRTscBrv4QAoHhL");
//                return params;
//            }
        };
        RequestQueue requestQueue;
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        try {
            requestQueue = Volley.newRequestQueue(PalSRNLogin.this);

            request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
        } catch (NullPointerException o) {
            try {
                requestQueue = Volley.newRequestQueue(getApplicationContext());

                request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
            } catch (Exception e) {dialog.dismiss();}
        }

    }

    private StudentDetailsRepository studentDetailsRepository;

    private void getUserDetails(String studentName, String studentMobile) {

        String ngoID = Util.getNGOID(context);
        String userID = ngoID + "_" + studentName.replace(" ","_") + "_" + studentMobile;

        // global.getDatabaseReference().child("app_ngo_relation").child(ngoID).child(Util.getAPPID(context)).child("userID").setValue(userID);

        global.getDatabaseReference().child("offline_login_users").child(ngoID).child("students").child(userID.toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        HashMap<String, String> map = (HashMap<String, String>) snapshot.getValue();
                        String studentName = map.get("fullName");
                        String sClass = map.get("sClass");
                        String sBoard = map.get("educationBoard");
                        String sLanguage = map.get("sLanguage");
                        String phoneNo = map.get("mobile");
                        String appID = map.get("appID");
                        String boardID = map.get("boardID");
                        String dateStarted = map.get("dateStarted");
                        String schoolId = map.get("schoolId");
                        String rollNo = map.get("rollNo");

                        Util.setUsername(context, studentName.replace(" ","_"));
                        Util.setBoardSelectionDummy(context, sBoard);
                        Util.setBoardSelection(context, sBoard);
                        Util.setBoardNameSelectionDummy(context, sBoard);
                        Util.setBoardNameSelection(context, sBoard);
                        Util.setClassSelection(context, sClass);
                        Util.setClassSelectionDummy(context, sClass);
                        Util.setClassNameSelectionDummy(context, sClass);
                        Util.setClassNameSelection(context, sClass);
                        Util.setLanguageSelection(context, sLanguage);
                        Util.setLanguagePackageSelection(context, sLanguage);
                        Util.setUserMobile(context, phoneNo);
                        Util.setExistingUser(context, true);
                        Util.setUserRoll(context,rollNo);
                        Util.setUserId(context,userID);
                        if(sClass==null) {
                            UserActivities.resetPref();
                            Util.setActivation(context, true);
                            startActivity(new Intent(context, PalLanguageSelectionActivity.class).putExtra("isHomeScreen", haveLanguage).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            
                        }
                        else if (sClass.contains("11")||sClass.contains("12")) {
                            Util.setStreamClassSelection(context,sClass);
                        }

                        if (studentDetailsRepository.isDataExist(userID, ngoID)) {
                            studentDetailsRepository.updateField(userID, ngoID, studentName, studentName, studentName, sBoard, sClass, sLanguage);
                        } else {
                            StudentDetailsModel studentDetailsModel = new StudentDetailsModel();
                            studentDetailsModel.setNgoId(ngoID);
                            studentDetailsModel.setUserId(userID);
                            studentDetailsModel.setStudentName(studentName);
                            studentDetailsModel.setStudentPassword(studentName);
                            studentDetailsModel.setUserName(studentName);
                            studentDetailsModel.setBoard(Util.getSelectedBoard(context));
                            studentDetailsModel.setSClass(Util.getSelectedClass(context));
                            studentDetailsModel.setLanguage(Util.getSelectedLanguage(context));
                            studentDetailsRepository.insertStudentDetails(studentDetailsModel);

                        }
                        if (Util.isExistingUser(context)) {
                            //Show On Boarding Pop Up's
                            Util.setUserId(context, userID);
                            Util.setShowOnlySubjects(context, true);
                            Util.setIntroDialog(context, false);
                            Util.setToolTipHomeScreen(context, true);
                            Util.setToolTipContentScreen(context, true);
                            Util.setToolTipDiagnosticScreen(context, true);
                            Util.setExistingUser(context, false);

                            Util.setContentonboadingMode(context,true);
                            Util.setHomeboadingMode(context,true);
                            if(sClass!=null) {
                                UserActivities.syncDataFromFirebase();
                                Util.setActivation(context, true);
                                Intent intent = new Intent(PalSRNLogin.this, PracticeTopicActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
//                                startActivity(new Intent(PalSRNLogin.this, PracticeTopicActivity.class));

                                
                            }
                        }else {
                            Util.setIntroDialog(context, true);
                            afterRegisterProcess(userID, studentName, studentMobile);
                        }

                    } else {

                        if (Util.isNetworkAvailable(context))
                        {
                            Util.setIntroDialog(context, true);
                            afterRegisterProcess(userID, studentName, studentMobile);
                        }else {
                            if (Util.getSelectedLanguage(context)!=null)
                            {
                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                {
                                    Util.openGifDialogue(context,"इंटरनेट कनेक्शन काम नहीं कर रहा");
                                }else {
                                    Util.openGifDialogue(context,"Internet Connection is not working");
                                }
                                dialog.dismiss();
                            }else {
                                Util.openGifDialogue(context,"Internet Connection is not working");
                                dialog.dismiss();
                            }
                        }

//                        if (Util.getIsFirstTime(context).equalsIgnoreCase("true")) {
//                            Util.setIntroDialog(context, true);
//                            afterRegisterProcess(userID, studentName, studentMobile);
//                        } else {
//                            Util.setIntroDialog(context, true);
//                            afterRegisterProcess(userID, studentName, studentMobile);
//                        }
                    }
                } catch (Exception e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private String rollNo_student;
    private boolean haveLanguage=false;
    private void afterRegisterProcess(String UId, String name, String mobile) {
        global.setUserType("Student");
        RegisterModel registerModel = new RegisterModel();
        registerModel.setPackageLanguage(Util.getSelectedLanguage(context));
        registerModel.setBoardID(Util.getSelectedBoard(context));
        registerModel.setNgoID(Util.getNGOID(context));
        registerModel.setAppID(Util.getAPPID(context));
        registerModel.setClassID(Util.getSelectedClass(context));
        registerModel.setStudentClass(Util.getSelectedClass(context));
        registerModel.setLanguage(Util.getSelectedLanguage(context));
        registerModel.setEducationBoard(Util.getSelectedBoard(context));
        registerModel.setMobile(mobile);
        registerModel.setRollNo(rollNo_student);
        registerModel.setAge("");
        registerModel.setFullName(name.replace(" ","_"));

        String token = Util.getToken(context);
        registerModel.setToken(token);
        if (ApplicationConstants.isSDCardPresent) {
            registerModel.setIsSDCARDAvailable("True");
            registerModel.setIsLicenseVerificationCompleted("False");
        } else registerModel.setIsSDCARDAvailable("False");

        registerModel.setUserType(userType);
        registerModel.setDateStarted(Calendar.getInstance().getTimeInMillis() + "");
        global.getDatabaseReference().child(ApplicationConstants.USERS).child(ApplicationConstants.STUDENTS).child(UId).setValue(registerModel);
        global.getDatabaseReference().child("offline_login_users").child(Util.getNGOID(context)).child("students").child(UId).setValue(registerModel);
        global.getDatabaseReference().child("offline_login_users").child(Util.getNGOID(context)).child("students").child(UId).child("schoolId").setValue(Util.getSchoolId(context));
        global.getDatabaseReference().child("offline_login_users").child(Util.getNGOID(context)).child("students").child(UId).child("schoolName").setValue(Util.getSchoolName(context));

        Util.setUserId(context,UId);
        Util.setUserMobile(context,mobile);
        Util.setUsername(context,name);
        Util.setBoardNameSelection(context,"cbse");
        Util.setBoardNameSelectionDummy(context,"cbse");
        Util.setBoardSelection(context,"cbse");
        Util.setBoardSelectionDummy(context,"cbse");

        UserInfoModel userInfoModel=new UserInfoModel();
        userInfoModel.setPackageLanguage(Util.getSelectedLanguage(context));
        userInfoModel.setBoardID(Util.getSelectedBoard(context));
        userInfoModel.setNgoID(Util.getNGOID(context));
        userInfoModel.setAppID(Util.getAPPID(context));
        userInfoModel.setClassID(Util.getSelectedClass(context));
        userInfoModel.setStudentClass(Util.getSelectedClass(context));
        userInfoModel.setLanguage(Util.getSelectedLanguage(context));
        userInfoModel.setEducationBoard(Util.getSelectedBoard(context));
        userInfoModel.setMobile(mobile);
        userInfoModel.setRollNo(rollNo_student);
        userInfoModel.setAge("");
        userInfoModel.setFullName(name.replace(" ","_"));

        FirebaseFirestore.getInstance().collection("PAL_UserInfo").document(UId).set(userInfoModel);

        Util.setIntroDialog(context, true);
        Util.setActivation(context, true);
        Util.setUserId(context,UId);
        UserActivities.resetPref();
        dialog.dismiss();
        startActivity(new Intent(context, PalLanguageSelectionActivity.class).putExtra("isHomeScreen", haveLanguage).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        
    }

}