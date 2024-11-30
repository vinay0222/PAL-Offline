package com.idreameducation.ipreppal.deeplink;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.model.DummyModel;
import com.idreameducation.ipreppal.model.FacilitatorStudentModel;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by droidNinja on 09/07/16.
 */
public class DeepLinkManager {

    private static final String TAG = DeepLinkManager.class.getSimpleName();
    private final FragmentActivity context;
    private Global global;
    private String addedSuccess;
    private String sorryMessage;
    private String okay;
    private String alreadyAdded;
    private String deleteFromAnother;
    private String cancel;
    private String add;
    private String error;

    public DeepLinkManager(FragmentActivity activity) {
        global = (Global) activity.getApplicationContext();
        this.context = activity;

    }

    public void checkForInvites() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(context.getIntent())
                .addOnSuccessListener(context, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData data) {
                        if (data == null) {
                            try {
                                String code = Util.getDataString(context);
                                if (code != null) {
                                    Log.i("Data String : ", code);
                                    Util.setDataString(context, null);
                                    setStaticText(null, false, code);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            // Get the deep link
                            Uri deepLink = data.getLink();
                            setStaticText(deepLink, true, null);
                        }

                    }
                }).addOnFailureListener(context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "getDynamicLink:onFailure", e);
            }
        });
    }


    public void addStudents(String data, String key) throws Exception {
        Util.showDialog(context);
        //board-class-lanuage-batchName-subjectName-facilitatorID
        DummyModel dummyModel = new DummyModel();
        dummyModel.setsBoard(Util.getSelectedBoardName(context));
        dummyModel.setsClass(data.split("-")[1]);
        dummyModel.setLangauge(data.split("-")[2]);
        dummyModel.setBatch(data.split("-")[3]);
        dummyModel.setSubject(data.split("-")[4]);
        dummyModel.setUserId(Util.getUserId(context));
        dummyModel.setName(Util.getUsername(context));
        dummyModel.setKey(key);
        String teacherName = data.split("-")[6];
        dummyModel.setUserMobile(Util.getUserMobile(context));
        dummyModel.setStudentImage(Util.getUserProfileUrl(context));
        dummyModel.setJoiningDate(Util.getCurrentDateWithDifferentFormat());
        String facilitatorId = data.split("-")[5];
        data = data.replace("-" + facilitatorId, "");

        final String aa = data;
        String subject = data.split("-")[4];
        subject = subject.substring(0, subject.length() - 1);
        subject = subject.replace("_", ", ");

        String msg = "You are now connected to " + teacherName + "'s Batch " + data.split("-")[3] + " for " + subject + "\nHappy Learning!";
        global.getDatabaseReference().child(ApplicationConstants.FACILITATOR).child(ApplicationConstants.STUDENTS).child(facilitatorId).child(data).child("List").child(Util.getUserId(context)).setValue(dummyModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Util.dismissDialog();
                if (task.getException() != null) {
                    Util.showToast(context, error);
                } else {
//                    int val =count+1;
//                    global.getDatabaseReference().child(ApplicationConstants.FACILITATOR).child(ApplicationConstants.STUDENTS).child(facilitatorId).child(aa).child("Count").setValue(val);
//                    Util.showSuccessDialog(context, addedSuccess, okay);


                    Util.showSuccessDialog(context, msg, okay);

                }
            }
        });
    }


    public void addFacilitatorToStudentData(final String data) throws Exception {
        //C_E_B-2-English-test-Math_-kSAaqdN8oGezoOha9SKpZGocRX63-Anurag-TO17XZ
        FacilitatorStudentModel facilitatorStudentModel = new FacilitatorStudentModel();
        facilitatorStudentModel.setName(data.split("-")[0] + "-" + data.split("-")[1] + "-" + data.split("-")[2] + "-" + data.split("-")[3] + "-" + data.split("-")[4] + "-" + data.split("-")[6] + "-" + data.split("-")[7]);
        facilitatorStudentModel.setFacilitatorId(data.split("-")[5]);
        facilitatorStudentModel.setSubject(data.split("-")[4]);
        facilitatorStudentModel.setTeacherName(data.split("-")[6]);
        //facilitatorStudentModel.setBatchID(map.get("shareCode"));

        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(ApplicationConstants.FACILITATOR).child(facilitatorStudentModel.getName()).setValue(facilitatorStudentModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String uniqueKey = databaseReference.getKey();
                try {
                    addStudents(data, uniqueKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void getFacilitatorForStudent(final String code) throws Exception {
//        String name = code.split("-")[0] + "-" + code.split("-")[1] + "-" + code.split("-")[2] + "-" + code.split("-")[3];
        String name = code.split("-")[0] + "-" + code.split("-")[1] + "-" + code.split("-")[2] + "-" + code.split("-")[3] + "-" + code.split("-")[4] + "-" + code.split("-")[6] + "-" + code.split("-")[7];
        global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(ApplicationConstants.FACILITATOR).child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        HashMap<String, String> facHashmap = (HashMap<String, String>) dataSnapshot.getValue();
                        String facilitatorId = facHashmap.get("facilitatorId");
                        String newFacId = code.split("-")[5];
                        if (facilitatorId.equalsIgnoreCase(newFacId)) {
                            Util.showToast(context, alreadyAdded);
                        } else {
                            /* Take Confirmation from user to add same class of another teacher */
                            confirmDialog(code, facilitatorId);
                        }
                    } else {
                        addFacilitatorToStudentData(code);
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

    private void confirmDialog(final String code, final String facId) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_confirmation);
        dialog.setCancelable(true);
        TextView textViewAdd = dialog.findViewById(R.id.textViewAdd);
        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText(deleteFromAnother);
        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        textViewAdd.setText(add);
        textViewCancel.setText(cancel);
        textViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String value = code.split("-")[0] + "-" + code.split("-")[1] + "-" + code.split("-")[2] + "-" + code.split("-")[3] + "-" + code.split("-")[4] + "-" + code.split("-")[6] + "-" + code.split("-")[7];
                    global.getDatabaseReference().child(ApplicationConstants.USER_DATA).child(Util.getUserId(context)).child(code.split("-")[0] + "-" + code.split("-")[1] + "-" + code.split("-")[2] + "-" + code.split("-")[3] + "-" + code.split("-")[4] + "-" + code.split("-")[6] + "-" + code.split("-")[7]).child(ApplicationConstants.FACILITATOR).removeValue();
                    global.getDatabaseReference().child(ApplicationConstants.FACILITATOR).child(ApplicationConstants.STUDENTS).child(facId).child(value).child("List").child(Util.getUserId(context)).removeValue();
                    dialog.dismiss();
                    addFacilitatorToStudentData(code);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));


    }


    private void setStaticText(final Uri result, boolean isDirectClassReferal, String code_) {

        global.getDatabaseReference().child("screen_text").child(Util.getSelectedBoard(context)).child("student").child("1").child(Util.getSelectedLanguage(context)).child(ApplicationConstants.SELECT_SUBJECT_SCREEN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        ArrayList<String> textArrayList = (ArrayList<String>) dataSnapshot.getValue();
                        sorryMessage = textArrayList.get(19);
                        okay = textArrayList.get(17);
                        alreadyAdded = textArrayList.get(18);
                        deleteFromAnother = textArrayList.get(20);
                        cancel = textArrayList.get(21);
                        add = textArrayList.get(22);
                        error = textArrayList.get(25);
                        addedSuccess = textArrayList.get(26);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Uri uri = result;
                    String code;
                    if (isDirectClassReferal) {
                        code = uri.getQueryParameter("facilitatorCode");
                        if (code == null) {
                            code = uri.getQueryParameter("ReferCode");
                            if (code != null) {
                                Util.showToast(context, "You can not use refer code Now");
                                return;
                            }
                        }
                    } else {
                        code = code_;
                    }

//                            String className = Util.getSelectedClassName(context);
//                            String boardID = Util.getSelectedBoard(context);
//                            String language = Util.getSelectedLanguagePackage(context);
//                            String board = code.split("-")[0];
//                            String sClass = code.split("-")[1];
//                            String slanguage = code.split("-")[2];
//                            if (!board.equalsIgnoreCase(boardID)) {
//                                Util.showToast(context , "Board is different");
//                            } else if (!sClass.equalsIgnoreCase(className)) {
//                                Util.showToast(context , "Class is different");
//                            } else if (!slanguage.equalsIgnoreCase(language)) {
//                                Util.showToast(context , "Language is different");
//                            } else {
//                            }
                    getFacilitatorForStudent(code);
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