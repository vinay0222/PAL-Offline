package com.idreameducation.ipreppal.roomdatabase.dao;



import com.idreameducation.ipreppal.roomdatabase.model.ActivationModel;
import com.idreameducation.ipreppal.roomdatabase.model.BoardsModel;
import com.idreameducation.ipreppal.roomdatabase.model.BooksModel;
import com.idreameducation.ipreppal.roomdatabase.model.BooksReportsModel;
import com.idreameducation.ipreppal.roomdatabase.model.CategoryModel;
import com.idreameducation.ipreppal.roomdatabase.model.ClassesModel;
import com.idreameducation.ipreppal.roomdatabase.model.FinalTestCompleteModel;
import com.idreameducation.ipreppal.roomdatabase.model.FoundationalTopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.LanguageModel;
import com.idreameducation.ipreppal.roomdatabase.model.LastTopicDetailsModel;
import com.idreameducation.ipreppal.roomdatabase.model.PracticeModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsCountModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDateWisePracticeModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDateWiseTestModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDateWiseVideoModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsDiagnosticCompleteModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataPracticeModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataTestModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsLatestDataVideoModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsPathModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTimeSpentModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicPathModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWisePracticeModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWiseTestModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWiseVideoModel;
import com.idreameducation.ipreppal.roomdatabase.model.StudentDetailsModel;
import com.idreameducation.ipreppal.roomdatabase.model.TestModel;
import com.idreameducation.ipreppal.roomdatabase.model.TopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.TopicPositionWiseModel;
import com.idreameducation.ipreppal.roomdatabase.model.TopicSubjectWiseModel;
import com.idreameducation.ipreppal.roomdatabase.model.TrackTopicModel;
import com.idreameducation.ipreppal.roomdatabase.model.VideoLessonModel;
import com.idreameducation.ipreppal.roomdatabase.model.VideoModel;
import com.idreameducation.ipreppal.roomdatabase.model.VideoReportsModel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoAccess {

    @Insert
    Long insertTask(CategoryModel categoryModel);

    @Insert
    Long insertVideoLessons(VideoLessonModel videoLessonModel);

    @Insert
    Long insertBooks(BooksModel booksModel);

    @Insert
    Long insertPractice(PracticeModel practiceModel);

    @Insert
    Long insertClasses(ClassesModel classesModel);

    @Insert
    Long insertBoards(BoardsModel boardsModel);

    @Insert
    Long insertLanguages(LanguageModel languageModel);

    @Insert
    Long insertVideoReports(VideoReportsModel videoReportsModel);

    @Insert
    Long insertBookReports(BooksReportsModel booksReportsModel);

    @Insert
    Long insertActivationDetails(ActivationModel activationModel);

    @Insert
    Long insertVideoDetails(VideoModel videoModel);

    @Insert
    Long insertFoundationalTopicDetails(FoundationalTopicModel foundationalTopicModel);

    @Insert
    Long insertTestDetails(TestModel testModel);

    @Insert
    Long insertTopicDetails(TopicModel topicModel);

    @Insert
    Long insertTopicSubjectWiseDetails(TopicSubjectWiseModel topicSubjectWiseModel);

    @Insert
    Long insertTopicPositionWiseDetails(TopicPositionWiseModel topicPositionWiseModel);

    @Insert
    Long insertStudentDetails(StudentDetailsModel studentDetailsModel);

    @Insert
    Long insertTopicDetailsForTracking(TrackTopicModel trackTopicModel);

    @Insert
    Long insertLastTopicDetails(LastTopicDetailsModel lastTopicDetailsModel);

    @Insert
    Long insertFinalTestCompleteDetails(FinalTestCompleteModel finalTestCompleteModel);

    /**** FOR REPORTS ****/

    /**** FOR REPORTS COUNT CONTENT WISE ****/
    @Insert
    Long insertReportsCount(ReportsCountModel reportsCountModel);
    /**** FOR REPORTS COUNT CONTENT WISE END ****/

    /**** FOR REPORTS DATE CONTENT WISE ****/
    @Insert
    Long insertReportsDateWisePractice(ReportsDateWisePracticeModel reportsDateWisePracticeModel);

    @Insert
    Long insertReportsDateWiseTest(ReportsDateWiseTestModel reportsDateWiseTestModel);

    @Insert
    Long insertReportsDateWiseVideo(ReportsDateWiseVideoModel reportsDateWiseVideoModel);
    /**** FOR REPORTS DATE CONTENT WISE END ****/

    /**** FOR REPORTS DIAGNOSTIC TEST COMPLETE ****/
    @Insert
    Long insertReportsDiagnosticComplete(ReportsDiagnosticCompleteModel reportsDiagnosticCompleteModel);
    /**** FOR REPORTS DIAGNOSTIC TEST COMPLETE END ****/

    /**** FOR REPORTS LATEST DATA CONTENT ****/
    @Insert
    Long insertReportsLatestDataTest(ReportsLatestDataTestModel reportsLatestDataTestModel);

    @Insert
    Long insertReportsLatestDataPractice(ReportsLatestDataPracticeModel reportsLatestDataPracticeModel);

    @Insert
    Long insertReportsLatestDataVideo(ReportsLatestDataVideoModel reportsLatestDataVideoModel);
    /**** FOR REPORTS LATEST DATA CONTENT END ****/

    /**** FOR REPORTS SAVING LEARNING PATH 1 ****/
    @Insert
    Long insertReportsPath(ReportsPathModel reportsPathModel);
    /**** FOR REPORTS SAVING LEARNING PATH 1 END ****/

    /**** FOR REPORTS TIME SPENT ON CONTENT ****/
    @Insert
    Long insertReportsTimeSpent(ReportsTimeSpentModel reportsTimeSpentModel);
    /**** FOR REPORTS TIME SPENT ON CONTENT END ****/

    /**** FOR REPORTS SAVING LEARNING PATH 2 ****/
    @Insert
    Long insertReportsTopicPath(ReportsTopicPathModel reportsTopicPathModel);
    /**** FOR REPORTS SAVING LEARNING PATH 2 END ****/

    /**** FOR REPORTS TOPIC CONTENT WISE DATA ****/
    @Insert
    Long insertReportsTopicWisePractice(ReportsTopicWisePracticeModel reportsTopicWisePracticeModel);

    @Insert
    Long insertReportsTopicWiseTest(ReportsTopicWiseTestModel reportsTopicWiseTestModel);

    @Insert
    Long insertReportsTopicWiseVideo(ReportsTopicWiseVideoModel reportsTopicWiseVideoModel);
    /**** FOR REPORTS TOPIC CONTENT WISE DATA END ****/

    /**** FOR REPORTS END ****/

    @Query("SELECT * FROM CategoryModel WHERE studentClass =:studentClass AND language =:language AND board =:board")
    LiveData<List<CategoryModel>> fetchAllTasks(String studentClass, String language, String board);


    @Query("SELECT * FROM VideoLessonModel WHERE studentClass =:studentClass AND language =:language AND board =:board AND categoryID =:categoryID")
    LiveData<List<VideoLessonModel>> fetchAllVideoLessons(String studentClass, String language, String board, String categoryID);


    @Query("SELECT * FROM BooksModel WHERE studentClass =:studentClass AND language =:language AND board =:board AND categoryID =:categoryID")
    LiveData<List<BooksModel>> fetchBooks(String studentClass, String language, String board, String categoryID);

//    @Query("SELECT * FROM PracticeModel WHERE studentClass =:studentClass AND language =:language AND board =:board AND categoryID =:categoryID")
//    LiveData<List<PracticeModel>> fetchPractice(String studentClass, String language, String board, String categoryID);


    @Query("SELECT * FROM PracticeModel WHERE studentClass =:studentClass AND language =:language AND board =:board AND categoryID =:categoryID AND subject =:subject")
    LiveData<List<PracticeModel>> fetchPractice(String studentClass, String language, String board, String categoryID , String subject);


    @Query("SELECT * FROM PracticeModel WHERE studentClass =:studentClass AND language =:language AND board =:board AND categoryID =:categoryID")
    LiveData<List<PracticeModel>> fetchPracticeReport(String studentClass, String language, String board, String categoryID);


    @Query("SELECT * FROM ClassesModel WHERE language =:language AND board =:board")
    LiveData<List<ClassesModel>> fetchClasses(String language, String board);


    @Query("SELECT * FROM BoardsModel WHERE language =:language")
    LiveData<List<BoardsModel>> fetchBoards(String language);

    @Query("SELECT * FROM LanguageModel")
    LiveData<List<LanguageModel>> fetchLanguage();


    @Query("SELECT * FROM VideoReportsModel WHERE userID =:userID AND category=:category AND videoID =:videoID")
    LiveData<List<VideoReportsModel>> fetchVideoReports(String userID, String category, String videoID);

    @Query("SELECT * FROM BooksReportsModel WHERE userID =:userID AND category=:category AND bookID =:bookID")
    LiveData<List<BooksReportsModel>> fetchBookReports(String userID, String category, String bookID);


    @Query("SELECT * FROM ActivationModel")
    LiveData<List<ActivationModel>> fetchActivationDetail();

    @Query("SELECT * FROM VideoModel WHERE video_name in(select distinct video_name FROM VideoModel) AND user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject")
    List<VideoModel> fetchVideoDetail(String userId, String board, String sClass, String subject);

    @Query("UPDATE VideoModel SET time=:time WHERE video_name=:video_name AND user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND lang=:lang")
    void updateVideoDetail(String userId, String board, String sClass, String subject,String video_name,String time,String lang);

    @Query("SELECT EXISTS(SELECT * FROM VideoModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND topic_id=:topicId AND video_name=:videoName AND lang=:lang)")
    boolean isVideoExist(String userId, String board, String sClass, String subject, String topicId, String videoName, String lang);

    @Query("SELECT * FROM FoundationalTopicModel WHERE user_id=:userId AND subject_id=:subjectId AND senior_topic_id=:seniorTopicId AND topic_id=:topicId ORDER BY topic_id DESC LIMIT 1")
    LiveData<List<FoundationalTopicModel>> fetchTopicDetail(String userId, String subjectId, String seniorTopicId, String topicId);

//    @Query("SELECT * FROM FoundationalTopicModel WHERE senior_topic_id=:seniorTopicId AND senior_class=:seniorClass")
    @Query("SELECT * FROM FoundationalTopicModel WHERE user_id=:userId")
    List<FoundationalTopicModel> fetchAllTopicDetails(String userId);

    @Query("SELECT EXISTS(SELECT * FROM FoundationalTopicModel WHERE user_id=:userId AND subject_id=:subjectId AND senior_topic_id=:seniorTopicId AND senior_class=:seniorClass AND topic_id=:topicId AND lang=:lang)")
    boolean isRowExist(String userId, String subjectId, String seniorTopicId, String seniorClass, String topicId, String lang);

    @Query("UPDATE FoundationalTopicModel SET senior_topic_name=:seniorTopicName, topic_name=:topicName, sClass=:sClass, streak_progress=:streakProgress, streak_count=:streakCount, incorrect_streak=:incorrectStreak, practice_type=:practiceType, show=:show, video_level=:videoLevel, test_percentage_achieved=:percentage WHERE user_id=:userId AND subject_id=:subjectId AND senior_topic_id=:seniorTopicId AND senior_class=:seniorClass AND topic_id=:topicId AND lang=:lang")
    void updateFields(String userId, String subjectId, String seniorTopicId, String seniorClass, String seniorTopicName, String topicId, String topicName, String sClass, String streakProgress, String streakCount, String incorrectStreak, String practiceType, boolean show, int videoLevel, int percentage,String lang);

    @Query("UPDATE FoundationalTopicModel SET show=:show WHERE user_id=:userId AND subject_id=:subjectId AND topic_id=:topicId AND senior_topic_id=:seniorTopicId")
    void updateShowField(String userId, String subjectId, String topicId, String seniorTopicId, boolean show);

    @Query("SELECT * FROM TestModel WHERE user_id=:userId AND subject_id=:subjectId AND topic_id=:topicId")
    LiveData<List<TestModel>> fetchDetail(String userId, String subjectId, String topicId);

    @Query("UPDATE TestModel SET date=:date WHERE user_id=:userId AND subject_id=:subjectId AND topic_id=:topicId")
    void updateTestDateField(String userId, String subjectId, String topicId, String date);

    @Query("SELECT EXISTS(SELECT * FROM TestModel WHERE user_id=:userId AND subject_id=:subjectId AND topic_id=:topicId)")
    boolean isTopicIdExist(String userId, String subjectId, String topicId);

    @Query("SELECT EXISTS(SELECT * FROM TopicModel WHERE topic_id=:topicId AND user_id=:userId)")
    boolean isTopicExist(String userId, String topicId);

    @Query("SELECT * FROM TopicModel WHERE user_id=:userId")
    List<TopicModel> getTopicDetail(String userId);

    @Query("UPDATE TopicSubjectWiseModel SET topic_id=:topicId WHERE user_id=:userId AND subject_id=:subjectId")
    void updateTopicIdFieldSubjectWise(String userId, String subjectId, String topicId);

    @Query("SELECT EXISTS(SELECT * FROM TopicSubjectWiseModel WHERE user_id=:userId AND subject_id=:subjectId)")
    boolean isTopicIdSubjectWiseExist(String userId, String subjectId);

    @Query("SELECT * FROM TopicSubjectWiseModel WHERE user_id=:userId AND subject_id=:subjectId")
    List<TopicSubjectWiseModel> fetchTopicSubjectWiseDetail(String userId, String subjectId);

    @Query("UPDATE TopicPositionWiseModel SET topic_id=:topicId, topic_position=:topicPosition WHERE user_id=:userId AND subject_id=:subjectId")
    void updateTopicIdFieldPositionWise(String userId, String subjectId, int topicPosition, String topicId);

    @Query("SELECT EXISTS(SELECT * FROM TopicPositionWiseModel WHERE user_id=:userId AND subject_id=:subjectId)")
    boolean isTopicIdPositionWiseExist(String userId, String subjectId);

    @Query("SELECT * FROM TopicPositionWiseModel WHERE user_id=:userId AND subject_id=:subjectId")
    List<TopicPositionWiseModel> fetchTopicPositionWiseDetail(String userId, String subjectId);

    @Query("UPDATE StudentDetailsModel SET student_name=:studentName, student_password=:studentPassword, username=:userName, sClass=:sClass, board=:board, language=:language WHERE ngo_id=:ngoId AND user_id=:userId")
    void updateStudentDetails(String ngoId, String userId, String studentName, String studentPassword, String userName, String sClass, String board, String language);

    @Query("SELECT EXISTS(SELECT * FROM StudentDetailsModel WHERE ngo_id=:ngoId AND user_id=:userId)")
    boolean isStudentExist(String ngoId, String userId);

    @Query("SELECT * FROM StudentDetailsModel WHERE user_id=:userId LIMIT 1")
    List<StudentDetailsModel> fetchStudentDetails(String userId);

    @Query("SELECT * FROM StudentDetailsModel WHERE ngo_id=:ngoId")
    List<StudentDetailsModel> fetchStudentDetailsData(String ngoId);

    @Query("UPDATE TrackTopicModel SET senior_topic_id=:seniorTopicId, senior_class=:seniorClass, senior_topic_name=:seniorTopicName WHERE user_id=:userId AND subject=:subject AND board=:board AND language=:language AND foundational_topic_id=:foundationalTopicId")
    void updateTopicDetailsForTracking(String userId, String subject, String board, String seniorTopicId, String seniorClass, String seniorTopicName, String foundationalTopicId, String language);

    @Query("SELECT EXISTS(SELECT * FROM TrackTopicModel WHERE user_id=:userId AND subject=:subject AND board=:board AND language=:language AND foundational_topic_id=:foundationalTopicId)")
    boolean isTopicExistForTracking(String userId, String subject, String board, String language, String foundationalTopicId);

    @Query("SELECT * FROM TrackTopicModel WHERE user_id=:userId AND subject=:subject AND board=:board AND language=:language AND foundational_topic_id=:foundationalTopicId")
    List<TrackTopicModel> fetchTopicDetailsForTracking(String userId, String subject, String board, String language, String foundationalTopicId);

    @Query("DELETE FROM TrackTopicModel WHERE user_id =:userId AND subject=:subject AND board=:board AND language=:language AND foundational_topic_id=:foundationalTopicId")
    void deleteTopic(String userId, String subject, String board, String language, String foundationalTopicId);

    @Query("SELECT * FROM LastTopicDetailsModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND language=:language")
    List<LastTopicDetailsModel> fetchLastTopicDetail(String userId, String board, String sClass, String subject, String language);

    @Query("UPDATE LastTopicDetailsModel SET topic_id=:topicId, topic_name=:topicName, subject_name=:subjectName, icon_url=:iconUrl, color=:color WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND language=:language")
    void updateLastTopic(String userId, String board, String sClass, String subject, String topicId, String topicName, String subjectName, String iconUrl, String color, String language);

    @Query("SELECT EXISTS(SELECT * FROM LastTopicDetailsModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND language=:language)")
    boolean isLastTopicExist(String userId, String board, String sClass, String subject, String language);

    @Query("SELECT * FROM FinalTestCompleteModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject")
    List<FinalTestCompleteModel> fetchFinalTestCompleteDetail(String userId, String board, String sClass, String subject);

    @Query("UPDATE FinalTestCompleteModel SET topic_id=:topicId WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject")
    void updateFinalTestCompleteData(String userId, String board, String sClass, String subject, String topicId);

    @Query("SELECT EXISTS(SELECT * FROM FinalTestCompleteModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND topic_id=:topicId AND lang=:lang)")
    boolean isFinalTestTopicIdExist(String userId, String board, String sClass, String subject, String topicId, String lang);

    /**** FOR REPORTS ****/

    /**** FOR REPORTS COUNT CONTENT WISE ****/
    @Query("SELECT * FROM ReportsCountModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND date=:date AND type=:type")
    List<ReportsCountModel> fetchReportsCountDetail(String userId, String board, String sClass, String date, String type);

    @Query("SELECT * FROM ReportsCountModel WHERE user_id=:userId AND board=:board AND sClass=:sClass")
    List<ReportsCountModel> fetchReportsCountDetailData(String userId, String board, String sClass);

    @Query("UPDATE ReportsCountModel SET count=:count WHERE user_id=:userId AND board=:board AND sClass=:sClass AND date=:date AND type=:type")
    void updateCount(String userId, String board, String sClass, String date, String type, long count);

    @Query("SELECT EXISTS(SELECT * FROM ReportsCountModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND date=:date AND type=:type)")
    boolean isCountFieldExist(String userId, String board, String sClass, String date, String type);
    /**** FOR REPORTS COUNT CONTENT WISE END ****/

    /**** FOR REPORTS TIME SPENT ON CONTENT ****/
    @Query("SELECT * FROM ReportsTimeSpentModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND date=:date AND subject=:subject AND lang=:lang")
    List<ReportsTimeSpentModel> fetchReportsTimeSpentDetail(String userId, String board, String sClass, String date, String subject,String lang);

    @Query("SELECT * FROM ReportsTimeSpentModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND lang=:lang")
    List<ReportsTimeSpentModel> fetchReportsTimeSpentDetailData(String userId, String board, String sClass, String lang);

    @Query("UPDATE ReportsTimeSpentModel SET time=:time WHERE user_id=:userId AND board=:board AND sClass=:sClass AND date=:date AND subject=:subject AND lang=:lang")
    void updateTime(String userId, String board, String sClass, String date, String subject, long time,String lang);

    @Query("SELECT EXISTS(SELECT * FROM ReportsTimeSpentModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND date=:date AND subject=:subject AND lang=:lang)")
    boolean isTimeFieldExist(String userId, String board, String sClass, String date, String subject,String lang);
    /**** FOR REPORTS TIME SPENT ON CONTENT END ****/

    /**** FOR REPORTS DIAGNOSTIC TEST COMPLETE ****/
    @Query("SELECT * FROM ReportsDiagnosticCompleteModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND topic_id=:topicId")
    List<ReportsDiagnosticCompleteModel> fetchReportsDiagnosticTestTopicWiseDetail(String userId, String board, String sClass, String subject, String topicId);

    @Query("SELECT * FROM ReportsDiagnosticCompleteModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject")
    List<ReportsDiagnosticCompleteModel> fetchReportsDiagnosticTestDetail(String userId, String board, String sClass, String subject);

    @Query("UPDATE ReportsDiagnosticCompleteModel SET d_complete=:complete WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND topic_id=:topicId AND lang=:lang")
    void updateCompleteField(String userId, String board, String sClass, String subject, String topicId, boolean complete,String lang);

    @Query("SELECT EXISTS(SELECT * FROM ReportsDiagnosticCompleteModel WHERE user_id=:userId AND board=:board AND lang=:lang AND sClass=:sClass AND subject=:subject AND topic_id=:topicId)")
    boolean isCompleteFieldExist(String userId, String board, String sClass, String subject, String topicId, String lang);
    /**** FOR REPORTS DIAGNOSTIC TEST COMPLETE END ****/

    /**** FOR REPORTS PATH FOR TOPIC ID ****/
    @Query("SELECT * FROM ReportsTopicPathModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject")
    List<ReportsTopicPathModel> fetchReportsTopicPathDetail(String userId, String board, String sClass, String subject);

    @Query("SELECT * FROM ReportsTopicPathModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND topic_id=:topicId")
    List<ReportsTopicPathModel> fetchReportsTopicWisePathDetail(String userId, String board, String sClass, String subject, String topicId);

    @Query("UPDATE ReportsTopicPathModel SET type_v=:typeV WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND topic_id=:topicId")
    void updatePath(String userId, String board, String sClass, String subject, String topicId, String typeV);

    @Query("SELECT EXISTS(SELECT * FROM ReportsTopicPathModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND topic_id=:topicId)")
    boolean isPathExist(String userId, String board, String sClass, String subject, String topicId);
    /**** FOR REPORTS PATH FOR TOPIC ID END ****/

    /**** FOR REPORTS LATEST DATA CONTENT ****/
    @Query("SELECT * FROM ReportsLatestDataTestModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND topic_id=:topicId")
    List<ReportsLatestDataTestModel> fetchReportsLatestDataTopicDetail(String userId, String board, String sClass, String subject, String topicId);

    @Query("SELECT * FROM ReportsLatestDataTestModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject")
    List<ReportsLatestDataTestModel> fetchReportsLatestDataDetail(String userId, String board, String sClass, String subject);

    @Query("UPDATE ReportsLatestDataTestModel SET list_data=:list, date=:date, questions_attempted=:questionsAttempted, scores=:scores, topic_name=:topicName, total_questions=:totalQuestions, total_scores=:totalScores, percentage_scored=:percentageScored WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId")
    void updateReportsLatestData(String userId, String board, String sClass, String subject, String type, String topicId, String list, String date, String questionsAttempted, String scores, String topicName, String totalQuestions, String totalScores, String percentageScored);

    @Query("SELECT EXISTS(SELECT * FROM ReportsLatestDataTestModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId)")
    boolean isReportsLatestDataExist(String userId, String board, String sClass, String subject, String type, String topicId);

    @Query("SELECT * FROM ReportsLatestDataPracticeModel WHERE user_id=:userId AND board=:board AND subject=:subject AND type=:type AND topic_id=:topicId")
    List<ReportsLatestDataPracticeModel> fetchReportsLatestPracticeDataTopicDetail(String userId, String board, String subject, String type, String topicId);

    @Query("SELECT * FROM ReportsLatestDataPracticeModel WHERE user_id=:userId AND board=:board AND subject=:subject AND type=:type")
    List<ReportsLatestDataPracticeModel> fetchReportsLatestPracticeDataDetail(String userId, String board, String subject, String type);

    @Query("UPDATE ReportsLatestDataPracticeModel SET current_level=:currentLevel, p_date=:pDate, mastery=:mastery, streak_progress=:streakProgress, topic_name=:topicName WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId")
    void updateReportsLatestDataPractice(String userId, String board, String sClass, String subject, String type, String topicId, String currentLevel, String pDate, String mastery, String streakProgress, String topicName);

    @Query("DELETE FROM ReportsLatestDataPracticeModel  WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId")
    void DeleteReportsLatestDataPractice(String userId, String board, String sClass, String subject, String type, String topicId);


    @Query("SELECT EXISTS(SELECT * FROM ReportsLatestDataPracticeModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId AND lang=:lang)")
    boolean isReportsLatestPracticeDataExist(String userId, String board, String sClass, String subject, String type, String topicId, String lang);

    @Query("SELECT * FROM ReportsLatestDataVideoModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId")
    List<ReportsLatestDataVideoModel> fetchReportsLatestVideoDataTopicDetail(String userId, String board, String sClass, String subject, String type, String topicId);

    @Query("SELECT * FROM ReportsLatestDataVideoModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type")
    List<ReportsLatestDataVideoModel> fetchReportsLatestVideoDataDetail(String userId, String board, String sClass, String subject, String type);

    @Query("UPDATE ReportsLatestDataVideoModel SET v_id=:vId, url=:url, v_time=:vTime, topic_name=:topicName, total_time=:totalTime, video_name=:videoName WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId AND lang=:lang")
    void updateReportsLatestDataVideo(String userId, String board, String sClass, String subject, String type, String topicId, String vId, String url, String vTime, String topicName, String totalTime, String videoName, String lang);

    @Query("SELECT EXISTS(SELECT * FROM ReportsLatestDataVideoModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId AND lang=:lang)")
    boolean isReportsLatestVideoDataExist(String userId, String board, String sClass, String subject, String type, String topicId, String lang);
    /**** FOR REPORTS LATEST DATA CONTENT END****/

    /**** FOR REPORTS DATE WISE CONTENT ****/
    @Query("SELECT * FROM ReportsDateWiseTestModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type")
    List<ReportsDateWiseTestModel> fetchReportsDateWiseTestDetail(String userId, String board, String sClass, String subject, String type);

    @Query("SELECT * FROM ReportsDateWiseTestModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND type=:type")
    List<ReportsDateWiseTestModel> fetchReportsDateWiseTestDetailData(String userId, String board, String sClass, String type);

    @Query("UPDATE ReportsDateWiseTestModel SET list_data=:list, date=:date, questions_attempted=:questionsAttempted, scores=:scores, topic_name=:topicName, total_questions=:totalQuestions, total_scores=:totalScores, percentage_scored=:percentageScored WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND test_date=:testDate AND time=:time")
    void updateReportsDateWiseTest(String userId, String board, String sClass, String subject, String type, String testDate, long time, String list, String date, String questionsAttempted, String scores, String topicName, String totalQuestions, String totalScores, String percentageScored);

    @Query("SELECT EXISTS(SELECT * FROM ReportsDateWiseTestModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND test_date=:date AND time=:time)")
    boolean isReportsDateWiseTestExist(String userId, String board, String sClass, String subject, String type, String date, long time);

    @Query("SELECT * FROM ReportsDateWisePracticeModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type")
    List<ReportsDateWisePracticeModel> fetchReportsDateWisePracticeDetail(String userId, String board, String sClass, String subject, String type);

    @Query("SELECT * FROM ReportsDateWisePracticeModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND type=:type")
    List<ReportsDateWisePracticeModel> fetchReportsDateWisePracticeDetailData(String userId, String board, String sClass, String type);

    @Query("UPDATE ReportsDateWisePracticeModel SET current_level=:currentLevel, p_date=:pDate, mastery=:mastery, streak_progress=:streakProgress, topic_name=:topicName WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND date=:date AND time=:time")
    void updateReportsDataWisePractice(String userId, String board, String sClass, String subject, String type, String date, long time, String currentLevel, String pDate, String mastery, String streakProgress, String topicName);

    @Query("SELECT EXISTS(SELECT * FROM ReportsDateWisePracticeModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND date=:date AND time=:time)")
    boolean isReportsDateWisePracticeDataExist(String userId, String board, String sClass, String subject, String type, String date, long time);

    @Query("SELECT * FROM ReportsDateWiseVideoModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type")
    List<ReportsDateWiseVideoModel> fetchReportsDateWiseVideoDataDetail(String userId, String board, String sClass, String subject, String type);

    @Query("SELECT * FROM ReportsDateWiseVideoModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND type=:type")
    List<ReportsDateWiseVideoModel> fetchReportsDateWiseVideoDataDetailData(String userId, String board, String sClass, String type);

    @Query("UPDATE ReportsDateWiseVideoModel SET v_time=:vTime, topic_name=:topicName, total_time=:totalTime, video_name=:videoName WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND date=:date AND time=:time")
    void updateReportsDateWiseDataVideo(String userId, String board, String sClass, String subject, String type, String date, long time, String vTime, String topicName, String totalTime, String videoName);

    @Query("SELECT EXISTS(SELECT * FROM ReportsDateWiseVideoModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND date=:date AND time=:time)")
    boolean isReportsDateWiseVideoDataExist(String userId, String board, String sClass, String subject, String type, String date, long time);
    /**** FOR REPORTS DATE WISE CONTENT END****/

    /**** FOR REPORTS TOPIC WISE CONTENT ****/
    @Query("SELECT * FROM ReportsTopicWiseTestModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId AND lang=:lang")
    List<ReportsTopicWiseTestModel> fetchReportsTopicWiseTestDetail(String userId, String board, String sClass, String subject, String type, String topicId, String lang);

    @Query("SELECT * FROM ReportsTopicWiseTestModel WHERE user_id=:userId AND type=:type AND lang=:lang")
    List<ReportsTopicWiseTestModel> fetchReportsTopicWiseTestDetailData(String userId, String type,String lang);

    @Query("UPDATE ReportsTopicWiseTestModel SET name=:name, list_data=:list, date=:date, questions_attempted=:questionsAttempted, scores=:scores, topic_name=:topicName, total_questions=:totalQuestions, total_scores=:totalScores, percentage_scored=:percentageScored WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId AND test_date=:testDate AND time=:time AND lang=:lang")
    void updateReportsTopicWiseTest(String userId, String board, String sClass, String subject, String type, String topicId, String testDate, long time, String name, String list, String date, String questionsAttempted, String scores, String topicName, String totalQuestions, String totalScores, String percentageScored, String lang);

    @Query("SELECT EXISTS(SELECT * FROM ReportsTopicWiseTestModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId AND test_date=:date AND time=:time AND lang=:lang)")
    boolean isReportsTopicWiseTestExist(String userId, String board, String sClass, String subject, String type, String topicId, String date, long time,String lang);

    @Query("SELECT * FROM ReportsTopicWisePracticeModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId AND lang=:lang")
    List<ReportsTopicWisePracticeModel> fetchReportsTopicWisePracticeDetail(String userId, String board, String sClass, String subject, String type, String topicId, String lang);

    @Query("SELECT * FROM ReportsTopicWisePracticeModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND type=:type AND lang=:lang")
    List<ReportsTopicWisePracticeModel> fetchReportsTopicWisePracticeDetailData(String userId, String board, String sClass, String type, String lang);

    @Query("UPDATE ReportsTopicWisePracticeModel SET name=:name, current_level=:currentLevel, p_date=:pDate, mastery=:mastery, streak_progress=:streakProgress, topic_name=:topicName WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId AND date=:date AND time=:time AND lang=:lang")
    void updateReportsTopicWisePractice(String userId, String board, String sClass, String subject, String type, String topicId, String date, long time, String name, String currentLevel, String pDate, String mastery, String streakProgress, String topicName, String lang);

    @Query("SELECT EXISTS(SELECT * FROM ReportsTopicWisePracticeModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId AND date=:date AND time=:time AND lang=:lang)")
    boolean isReportsTopicWisePracticeDataExist(String userId, String board, String sClass, String subject, String type, String topicId, String date, long time,String lang);

    @Query("SELECT * FROM ReportsTopicWiseVideoModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId AND lang=:lang")
    List<ReportsTopicWiseVideoModel> fetchReportsTopicWiseVideoDataDetail(String userId, String board, String sClass, String subject, String type, String topicId, String lang);

    @Query("SELECT * FROM ReportsTopicWiseVideoModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND type=:type AND lang=:lang")
    List<ReportsTopicWiseVideoModel> fetchReportsTopicWiseVideoDataDetailData(String userId, String board, String sClass, String type, String lang);

    @Query("UPDATE ReportsTopicWiseVideoModel SET name=:name, v_time=:vTime, topic_name=:topicName, total_time=:totalTime, video_name=:videoName WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId AND date=:date AND time=:time AND lang=:lang")
    void updateReportsTopicWiseDataVideo(String userId, String board, String sClass, String subject, String type, String topicId, String date, long time, String name, String vTime, String topicName, String totalTime, String videoName, String lang);

    @Query("SELECT EXISTS(SELECT * FROM ReportsTopicWiseVideoModel WHERE user_id=:userId AND board=:board AND sClass=:sClass AND subject=:subject AND type=:type AND topic_id=:topicId AND date=:date AND time=:time AND lang=:lang)")
    boolean isReportsTopicWiseVideoDataExist(String userId, String board, String sClass, String subject, String type, String topicId, String date, long time,String lang);
    /**** FOR REPORTS TOPIC WISE CONTENT END ****/

    /**** FOR REPORTS END ****/

    @Update
    void updateTask(CategoryModel note);


    @Delete
    void deleteTask(CategoryModel note);
}
