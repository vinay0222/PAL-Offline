package com.idreameducation.ipreppal.educationApplication;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;


import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.idreameducation.ipreppal.model.ReferModel;
import com.idreameducation.ipreppal.roomdatabase.db.ContentDatabase;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.lifecycle.LifecycleOwner;
import androidx.room.Room;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Anuragkataria on 10/15/2017.
 */

public class Global extends Application implements GoogleApiClient.OnConnectionFailedListener {
    public static boolean activityVisible; // Variable that will check the

    public DatabaseReference databaseReference;
    private ArrayList<HashMap<String, HashMap<String, String>>> topicArrayList;
    private HashMap<String, String> bookMap;
    private HashMap<String, String> videoHashmap;
    private FirebaseDatabase database = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String categoryName;
    private int cumulativeMastery;
    private int progress;
    private boolean isPreferenceChange;
    private String foundationalLevel;
    private String json;
    private String referUserId;
    private ReferModel referModel;
    private String fullImage;
    private boolean isImageAdded;
    private String link;
    private String userType;
    private String studentClass;
    private String language;
    private String boardName;
    private String userNameForChat;
    private ArrayList<String> userIdForFacilitator;
    private ArrayList<String> userNameForFacilitator;
    private FirebaseUser user;
    private String uniqueId;
    private boolean isFlagEnabled;
    private String foundationalTopicId;
    private String lastAttemptedDate;
    private String type;
    private int levelNo;
    private int board;
    private int sClass;
    private String allThreeValue;
    private ArrayList<String> allFourValuesStudents;
    private GoogleSignInAccount googleAccount;
    private ArrayList<HashMap<String, String>> videoArrayList;
    private ArrayList<HashMap<String, String>> facilitatorList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> sortedBoardArraylist = new ArrayList<>();
    //    private String topicName;
    private Context context;
    private ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> vList;
    private ArrayList<String> topicArrayList_;
    private ContentDatabase contentDatabase;
    private final String DB_NAME = "db_Content";
    private LifecycleOwner lifecycleOwner;

    public HashMap<String, String> getVideoHashmap() {
        return videoHashmap;
    }

    public void setVideoHashmap(HashMap<String, String> videoHashmap) {
        this.videoHashmap = videoHashmap;
    }

    public ArrayList<HashMap<String, String>> getFacilitatorList() {
        return facilitatorList;
    }

    public void setFacilitatorList(ArrayList<HashMap<String, String>> facilitatorList) {
        this.facilitatorList = facilitatorList;
    }

    public void setCateGoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public DatabaseReference getDatabaseReference() {
        if (databaseReference == null) {
            databaseReference = database.getReference();
        }
        return database.getReference();
    }




    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public int getCumulativeMastery() {
        return cumulativeMastery;
    }

    public void setCumulativeMastery(int cumulativeMastery) {
        this.cumulativeMastery = cumulativeMastery;
    }

    public String getFoundationalLevel() {
        return foundationalLevel;
    }

    public void setFoundationalLevel(String foundationalLevel) {
        this.foundationalLevel = foundationalLevel;
    }

    public boolean isPreferenceChange() {
        return false;
    }

    public void setPreferenceChange(boolean preferenceChange) {
        isPreferenceChange = preferenceChange;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getFullImage() {
        return fullImage;
    }

    public void setFullImage(String fullImage) {
        this.fullImage = fullImage;
    }

    public String getReferUserId() {
        return referUserId;
    }

    public void setReferUserId(String referUserId) {
        this.referUserId = referUserId;
    }

    public ReferModel getReferModel() {
        return referModel;
    }

    public void setReferModel(ReferModel referModel) {
        this.referModel = referModel;
    }

    public boolean isImageAdded() {
        return isImageAdded;
    }

    public void setImageAdded(boolean imageAdded) {
        isImageAdded = imageAdded;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getUserNameForChat() {
        return userNameForChat;
    }

    public void setUserNameForChat(String userNameForChat) {
        this.userNameForChat = userNameForChat;
    }

    public ArrayList<String> getUserIdForFacilitator() {
        return userIdForFacilitator;
    }

    public void setUserIdForFacilitator(ArrayList<String> userIdForFacilitator) {
        this.userIdForFacilitator = userIdForFacilitator;
    }

    public ArrayList<String> getUserNameForFacilitator() {
        return userNameForFacilitator;
    }

    public void setUserNameForFacilitator(ArrayList<String> userNameForFacilitator) {
        this.userNameForFacilitator = userNameForFacilitator;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
       // MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        try {
            Fabric.with(this, new Crashlytics());
            contentDatabase = Room.databaseBuilder(context, ContentDatabase.class, DB_NAME).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (database == null) {
                database = FirebaseDatabase.getInstance();
                database.setPersistenceEnabled(true);
                databaseReference = database.getReference();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FirebaseUser user = null;
        try {
            user = FirebaseAuth.getInstance().getCurrentUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.user = user;
        try {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setUserDetailForAnalytics() {
        try {
            mFirebaseAnalytics.setUserProperty("StudentClass" , Util.getSelectedClass(context));
            mFirebaseAnalytics.setUserProperty("StudentBoard" , Util.getSelectedBoard(context));
            mFirebaseAnalytics.setUserProperty("StudentState" , Util.getSelectedState(context));
            mFirebaseAnalytics.setUserProperty("StudentUserID" , Util.getUserId(context));
            mFirebaseAnalytics.setUserProperty("StudentName" , Util.getUsername(context));
            mFirebaseAnalytics.setUserId(Util.getUserId(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendData(String id, String name) {
        try {
            Bundle bundle = new Bundle();
            if (Util.getUserId(context) == null) {
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
            } else {
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Util.getUserId(context));
            }
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);

            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public boolean isFlagEnabled() {
        return isFlagEnabled;
    }

    public void setFlagEnabled(boolean flagEnabled) {
        isFlagEnabled = flagEnabled;
    }

    public String getFoundationalTopicId() {
        return foundationalTopicId;
    }

    public void setFoundationalTopicId(String foundationalTopicId) {
        this.foundationalTopicId = foundationalTopicId;
    }

    public String getLastAttemptedDate() {
        return lastAttemptedDate;
    }

    public void setLastAttemptedDate(String lastAttemptedDate) {
        this.lastAttemptedDate = lastAttemptedDate;
    }


    // Gloabl declaration of variable to use in whole app

    public String getType() {
        return type;
    }
    // current activity state

    public void setType(String type) {
        this.type = type;
    }

    public int getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }

    public int getBoard() {
        return board;
    }

    public void setBoard(int board) {
        this.board = board;
    }

    public int getsClass() {
        return sClass;
    }

    public void setsClass(int sClass) {
        this.sClass = sClass;
    }

    public String getAllThreeValue() {
        return allThreeValue;
    }

    public void setAllThreeValue(String allThreeValue) {
        this.allThreeValue = allThreeValue;
    }

    public ArrayList<String> getAllFourValuesStudents() {
        return allFourValuesStudents;
    }

    public void setAllFourValuesStudents(ArrayList<String> allFourValuesStudents) {
        this.allFourValuesStudents = allFourValuesStudents;
    }

    public GoogleSignInAccount getGoogleAccount() {
        return googleAccount;
    }

    public void setGoogleAccount(GoogleSignInAccount googleAccount) {
        this.googleAccount = googleAccount;
    }

    public ArrayList<HashMap<String, HashMap<String, String>>> getTopicArrayList() {
        return topicArrayList;
    }

    public void setTopicArrayList(ArrayList<HashMap<String, HashMap<String, String>>> topicArrayList) {
        this.topicArrayList = topicArrayList;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public ArrayList<HashMap<String, String>> getVideoArrayList() {
        return videoArrayList;
    }

    public void setVideoArrayList(ArrayList<HashMap<String, String>> videoArrayList) {
        this.videoArrayList = videoArrayList;
    }

    public HashMap<String, String> getBookMap() {
        return bookMap;
    }

    public void setBookMap(HashMap<String, String> bookMap) {
        this.bookMap = bookMap;
    }

    public ArrayList<HashMap<String, String>> getSortedBoardArraylist() {
        return sortedBoardArraylist;
    }

    public void setSortedBoardArraylist(ArrayList<HashMap<String, String>> sortedBoardArraylist) {
        this.sortedBoardArraylist = sortedBoardArraylist;
    }

    public ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> getvList() {
        return vList;
    }

    public void setvList(ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> vList) {
        this.vList = vList;
    }

    public ArrayList<String> getTopicArrayList_() {
        return topicArrayList_;
    }

    public void setTopicArrayList_(ArrayList<String> topicArrayList_) {
        this.topicArrayList_ = topicArrayList_;
    }


    public ContentDatabase getRoomDatabase() {

        return contentDatabase;
    }


    private boolean isEncryptedContent;

    public boolean isEncryptedContent() {
        return isEncryptedContent;
    }

    public void setEncryptedContent(boolean encryptedContent) {
        isEncryptedContent = encryptedContent;
    }


    public ArrayList<String> getTopicIdArrayList() {
        return topicIdArrayList;
    }

    public void setTopicIdArrayList(ArrayList<String> topicIdArrayList) {
        this.topicIdArrayList = topicIdArrayList;
    }

    private ArrayList<String> topicIdArrayList;

    public String getChatID() {
        return chatID;
    }

    private String chatID ;

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    private String chatName ;
    private String batchCode ;

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public void setBatchID(String batchID) {
        this.batchID = batchID;
    }

    private String batchID ;

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    private String batchName ;

    public String getColor() {
        if(color==null) return Util.getColour(context);
        else return color;
    }

    public void setColor(String color) {
        Util.setColour(context,color);
        this.color = color;
    }

    private String color;


    public String getBatchID() {
        return batchID;
    }

}
