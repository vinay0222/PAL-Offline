package com.idreameducation.ipreppal.model.CoreContent;

public class PracticeDataModel {

    String Display,Foundational_Topic_ID,IsModelTestPaper,Levels,Next_topic_id,StreakCount,TName,
            TName_alt,TopicID,foundational_class,incorrectStreak,isAlternateLanguageAvailable,
            next_chapter_name,next_class,next_class_subject_name;

    String subjectID;
    public PracticeDataModel(String display, String foundational_Topic_ID, String isModelTestPaper, String levels, String next_topic_id, String streakCount, String TName, String TName_alt, String topicID, String foundational_class, String incorrectStreak, String isAlternateLanguageAvailable, String next_chapter_name, String next_class, String next_class_subject_name) {
        Display = display;
        Foundational_Topic_ID = foundational_Topic_ID;
        IsModelTestPaper = isModelTestPaper;
        Levels = levels;
        Next_topic_id = next_topic_id;
        StreakCount = streakCount;
        this.TName = TName;
        this.TName_alt = TName_alt;
        TopicID = topicID;
        this.foundational_class = foundational_class;
        this.incorrectStreak = incorrectStreak;
        this.isAlternateLanguageAvailable = isAlternateLanguageAvailable;
        this.next_chapter_name = next_chapter_name;
        this.next_class = next_class;
        this.next_class_subject_name = next_class_subject_name;
    }


    public PracticeDataModel() {
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getDisplay() {
        return Display;
    }

    public void setDisplay(String display) {
        Display = display;
    }

    public String getFoundational_Topic_ID() {
        return Foundational_Topic_ID;
    }

    public void setFoundational_Topic_ID(String foundational_Topic_ID) {
        Foundational_Topic_ID = foundational_Topic_ID;
    }

    public String getIsModelTestPaper() {
        return IsModelTestPaper;
    }

    public void setIsModelTestPaper(String isModelTestPaper) {
        IsModelTestPaper = isModelTestPaper;
    }

    public String getLevels() {
        return Levels;
    }

    public void setLevels(String levels) {
        Levels = levels;
    }

    public String getNext_topic_id() {
        return Next_topic_id;
    }

    public void setNext_topic_id(String next_topic_id) {
        Next_topic_id = next_topic_id;
    }

    public String getStreakCount() {
        return StreakCount;
    }

    public void setStreakCount(String streakCount) {
        StreakCount = streakCount;
    }

    public String getTName() {
        return TName;
    }

    public void setTName(String TName) {
        this.TName = TName;
    }

    public String getTName_alt() {
        return TName_alt;
    }

    public void setTName_alt(String TName_alt) {
        this.TName_alt = TName_alt;
    }

    public String getTopicID() {
        return TopicID;
    }

    public void setTopicID(String topicID) {
        TopicID = topicID;
    }

    public String getFoundational_class() {
        return foundational_class;
    }

    public void setFoundational_class(String foundational_class) {
        this.foundational_class = foundational_class;
    }

    public String getIncorrectStreak() {
        return incorrectStreak;
    }

    public void setIncorrectStreak(String incorrectStreak) {
        this.incorrectStreak = incorrectStreak;
    }

    public String getIsAlternateLanguageAvailable() {
        return isAlternateLanguageAvailable;
    }

    public void setIsAlternateLanguageAvailable(String isAlternateLanguageAvailable) {
        this.isAlternateLanguageAvailable = isAlternateLanguageAvailable;
    }

    public String getNext_chapter_name() {
        return next_chapter_name;
    }

    public void setNext_chapter_name(String next_chapter_name) {
        this.next_chapter_name = next_chapter_name;
    }

    public String getNext_class() {
        return next_class;
    }

    public void setNext_class(String next_class) {
        this.next_class = next_class;
    }

    public String getNext_class_subject_name() {
        return next_class_subject_name;
    }

    public void setNext_class_subject_name(String next_class_subject_name) {
        this.next_class_subject_name = next_class_subject_name;
    }
}
