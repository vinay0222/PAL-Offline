package com.idreameducation.ipreppal.roomdatabase.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.idreameducation.ipreppal.roomdatabase.dao.DaoAccess;
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
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTestDetailReviewModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTestScoreModel;
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


@Database(entities ={BooksReportsModel.class, VideoReportsModel.class, CategoryModel.class, VideoLessonModel.class , BooksModel.class , PracticeModel.class , ClassesModel.class , LanguageModel.class, BoardsModel.class, ActivationModel.class, VideoModel.class, FoundationalTopicModel.class, TestModel.class, TopicModel.class, TopicSubjectWiseModel.class, TopicPositionWiseModel.class, ReportsCountModel.class, ReportsDateWisePracticeModel.class ,
        ReportsTestDetailReviewModel.class, ReportsDateWiseTestModel.class, ReportsTestScoreModel.class, ReportsDateWiseVideoModel.class, ReportsDiagnosticCompleteModel.class, ReportsLatestDataPracticeModel.class, ReportsLatestDataTestModel.class, ReportsLatestDataVideoModel.class, ReportsPathModel.class, ReportsTimeSpentModel.class, ReportsTopicPathModel.class, ReportsTopicWisePracticeModel.class, ReportsTopicWiseTestModel.class,
        ReportsTopicWiseVideoModel.class, StudentDetailsModel.class, TrackTopicModel.class, LastTopicDetailsModel.class, FinalTestCompleteModel.class},version=1,exportSchema=false)

public abstract class ContentDatabase extends RoomDatabase {

    public abstract DaoAccess daoAccess();
}

