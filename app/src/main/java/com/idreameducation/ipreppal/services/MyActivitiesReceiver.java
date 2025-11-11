package com.idreameducation.ipreppal.services;


import static com.idreameducation.ipreppal.userActivities.UserActivities.LASTOPENED_KEY;
import static com.idreameducation.ipreppal.userActivities.UserActivities.WEEKLY_NOTIFICATION_KEY;
import static com.idreameducation.ipreppal.userActivities.UserActivities.clearWeeklyReport;
import static com.idreameducation.ipreppal.userActivities.UserActivities.clearWeeklyTestReport;
import static com.idreameducation.ipreppal.userActivities.UserActivities.getSubjectInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.idreameducation.ipreppal.pal.activity.PalSplashActivity;
import com.idreameducation.ipreppal.userActivities.SubjectInfo_Model;
import com.idreameducation.ipreppal.userActivities.UserActivities;
import com.idreameducation.ipreppal.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MyActivitiesReceiver extends BroadcastReceiver {

    private Context context;
    private UserActivities userActivities;
    private SharedPreferences.Editor sharedPreferences;
    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        System.out.println("MyReceiver: here! "+ new Date());
//        userActivities = new UserActivities(context,false);
//        sharedPreferences = context.getSharedPreferences(SHAREDPREF_KEY, MODE_PRIVATE).edit();
//        preferences = context.getSharedPreferences(SHAREDPREF_KEY, MODE_PRIVATE);
//
//        UserActivities.syncDataToFirebase();
//
//        checkLastOpened();
//        checkSubjectWeeklyUsage();
//
////        Util.showNotification(context,"checking","checking",new Intent(context,PalSplashActivity.class),0);
//
//
//        Calendar calendar = Calendar.getInstance();
//        int day = calendar.get(Calendar.DAY_OF_WEEK);
//
//        boolean weeklyNotificationDone= preferences.getBoolean(WEEKLY_NOTIFICATION_KEY,false);
//        if(day==Calendar.SATURDAY) {
//            if (!weeklyNotificationDone) checkTestUsage();
//        }
//        else if(weeklyNotificationDone){
//            if (weeklyNotificationDone){
//                sharedPreferences.putBoolean(WEEKLY_NOTIFICATION_KEY,false);
//                sharedPreferences.commit();
//            }
//
//        }
    }

    private void checkLastOpened() {

        /** get current timestamp */
        long currentTimeMillis = System.currentTimeMillis();

        /** get last opened timestamp */
        long lastOpenedTimeMillis=preferences.getLong(LASTOPENED_KEY,0);             // last opened timestamp
//        long lastOpenedTimeMillis = Long.parseLong("1673511034000");               // testing timestamp

        /** calculate current & last time difference */
        long timeDiff = currentTimeMillis-lastOpenedTimeMillis;
        long daysLeft = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);

        /** notification message info
         * @param userName => current user Name
         * @param title => Notification title */
        String userName= Util.getUsername(context);
        String title = "Hii "+ userName;

        /** check conditions */
        if(daysLeft==1) {
            /** When the user has not opened the app even once in a whole day */
            String Message;
            if(Util.getSelectedLanguage(context).equals("hindi")) Message = " क्या आपने आज iPrep PAL पर कुछ नहीं सीखा? केवल कुछ मिनटों के लिए ही सही, अपना iPrep PAL app खोलें और प्रतिदिन की अपनी पढ़ने की गति को जारी रखें!    ";
            else Message = "Regularity is the biggest growth hack!  Dont miss your learning routine on iPrep PAL. Open the app and learn something";
            Util.showNotification(context, title,Message,new Intent(context, PalSplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),022201);
        }
        else if(daysLeft>1 && daysLeft<6) {
            /** When the user has not opened the app from more then 1 day */
            String Message = userName+", iPrep Pal app is missing you because you did not remember us from "+daysLeft +" days";
            Util.showNotification(context, title,Message,new Intent(context, PalSplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),022202);
        }


    }

    private void checkSubjectWeeklyUsage() {
        HashMap<String, SubjectInfo_Model> subjectInfo = getSubjectInfo();
        long totalTime=0;

        ArrayList<String> dateList=new ArrayList<>();

        for(String subjectID: subjectInfo.keySet()) {
            SubjectInfo_Model subjectInfo_model = subjectInfo.get(subjectID);
            for(String dateKey : subjectInfo_model.getDailyUsageReport().keySet()) if(!dateList.contains(dateKey))dateList.add(dateKey);
            totalTime=totalTime+(Long.parseLong(subjectInfo_model.getCurrentWeekTimeUsage()));
        }

        if(dateList.size()>=7) {
            String userName = Util.getUsername(context);
            String Title = "Hii " + userName;
            String Message = " ";
            int ss = 0;
            long seconds = (totalTime / 1000);
            ss = (int) (ss + seconds);

            int sec = 0;
            int min = 0;
            int hours = 0;
            int day = 0;

            sec = (int) seconds % 60;
            min = (int) (seconds / 60) % 60;
            hours = (int) (seconds / (60 * 60)) % 24;
            day = (int) (seconds / (60 * 60 * 24)) % 7;

            String totalTimeSpent;
            if (min == 0) totalTimeSpent = sec + " s";
            else totalTimeSpent = hours + " h " + min + " m " + sec + " s";

            if (sec <= 10800){
                if (Util.getSelectedLanguage(context).equals("hindi")) Message = "iPrep PAL app पर आपका साप्ताहिक उपयोग समय जितना होना चाहिए, उससे कम रहा है। क्या आप इस सप्ताह में अपनी गति बढ़ा कर अपने दोस्तों की तरह सीख सकते हैं?  ";
                else Message = "Your weekly usage of iPrep PAL app has been lower than what it shoud be. Can you pick up pace in this week and learn as your friends do? ";
            }
            else if(sec>=36000) {
                if(Util.getSelectedLanguage(context).equals("hindi")) Message ="मुबारक़ हो ! आप बहुत अच्छा कर रहे हैं, आपने इस सप्ताह iPrep PAL app पर "+totalTimeSpent+" से अधिक समय बिताया है। अपने पढ़ने और लगातार सीखने के संकल्प को बनाए रखें ";
                else Message = "Great going champion! You've used iPrep PAL app for more than "+totalTimeSpent+" this week. Keep the momentum of Learning and Growth going!";
            }
            else Message = userName+", Your usage time of iPrep PAL in this week is "+totalTimeSpent+". Keep learning and growing with PAL. ";

            HashMap<String,String> data =new HashMap<>();
            data.put("message","Weekly report");
            data.put("messageTime","");
            data.put("notificationMessage",Message);
            data.put("sender","iPrep UserActivities");
            data.put("studentId",Util.getUserId(context));
            data.put("type","logical notification");

            Util.showNotification(context,Title,Message,new Intent(context, PalSplashActivity.class),022201);
            userActivities.addNotificationMessage(data);
            clearWeeklyTestReport();
            clearWeeklyReport();
        }
    }

    private void checkTestUsage() {

        /** Diagnostic test NotAttemptedList */
        ArrayList<String> diagnosticNotAttemptedList=new ArrayList<>();

        /** Final test NotAttemptedList */
        ArrayList<String> finalTestNotAttemptedList=new ArrayList<>();

        /** get all subject info in subjectInfo  */
        HashMap<String, SubjectInfo_Model> subjectInfo = UserActivities.getSubjectInfo();
        for(String subject : subjectInfo.keySet()) {
            /** get subject info in SubjectInfo_Model of subject */
            SubjectInfo_Model subjectInfoModel=subjectInfo.get(subject);
            /** check final & diagnostic test of subject */
            if(!subjectInfoModel.isCurrentWeekDiagnostic()) diagnosticNotAttemptedList.add(subject);
            else if(!subjectInfoModel.isCurrentWeekFinalTest()) finalTestNotAttemptedList.add(subject);
        }

        String userName= Util.getUsername(context);
        String Title= "Hii "+ userName;
        String Message = " ";

        String d=diagnosticNotAttemptedList.toString().replace("[","")
                .replace("]","")
                .replace("_"," ");

        HashMap<String,String> data =new HashMap<>();

        if(diagnosticNotAttemptedList.size()!=0) {
            if(Util.getSelectedLanguage(context).equals("hindi")) Message = userName+", प्रत्येक विषय को पढ़ना आवश्यक है! आपको iPrep PAL पर "+d+" को पढ़े हुए एक सप्ताह हो गया है। कृप्या कुछ समय निकालें और अपनी पढ़ने की यात्रा जारी रखें। ";
            else Message = userName+", learning every subject is important! It's been a week since you learnt "+d+" on the iPrep PAL app. Pls find some time and continue your learning journey";
            data.put("message","Diagnostic Test report");
        } else if(finalTestNotAttemptedList.size()!=0) {
            Message = userName+", you started but not complete any topic of "+d +" this week";
            if(Util.getSelectedLanguage(context).equals("hindi")) Message= userName+", You havent attempted any final test this week. Don't forget to complete your final tests after you learn any chapter! Finishing with over 80% will earn you a completion badge!";
            else Message= userName+", आपने इस सप्ताह किसी final test का प्रयास नहीं किया है। कोई भी पाठ पढ़ने के बाद अपने final test को पूरा करना न भूलें | 80% से अधिक अंकों के साथ final test समाप्त करने पर आपको बैज प्राप्त होगा। ";
            data.put("message","Final Test report");
        }

        data.put("messageTime","");
        data.put("notificationMessage",Message);
        data.put("sender","iPrep UserActivities");
        data.put("studentId",Util.getUserId(context));
        data.put("type","test notification");

        Util.showNotification(context,Title,Message,new Intent(context, PalSplashActivity.class),022202);
        userActivities.addNotificationMessage(data);
        clearWeeklyTestReport();
        clearWeeklyReport();
        sharedPreferences.putBoolean(WEEKLY_NOTIFICATION_KEY,true);
        sharedPreferences.commit();

    }
}
