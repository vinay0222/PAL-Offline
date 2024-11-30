package com.idreameducation.ipreppal.userActivities;

import android.content.Context;

public class LearningPath {

    Context context;

//    public void runBackgroundTask(String userId, String board, String sClass, String subject, String lastTopicId, String topicId, String value, String type) {
//
////        topicID = topicId;
//        if (type.equals("lastTopicIdTask")) {
//            getList(userId, board, sClass, subject, topicId, value, "lastTopicIdTask").subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new io.reactivex.Observer<Object>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            lastTopicIdDisposable = d;
//                        }
//
//                        @Override
//                        public void onNext(Object o) {
////                        updateUi((ArrayList<TopicSubjectWiseModel>) o);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            lastTopicIdDisposable.dispose();
//                        }
//                    });
//        } else if (type.equals("pathTask")) {
//            getList(userId, board, sClass, subject, topicId, value, "pathTask").subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new io.reactivex.Observer<Object>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            pathDisposable = d;
//                        }
//
//                        @Override
//                        public void onNext(Object o) {
//                            ArrayList<ReportsTopicPathModel> list = (ArrayList<ReportsTopicPathModel>) o;
//                            if (topicId == null) {
////                                if (list != null && list.size() > 0) {
////                                    data = new HashMap<>();
////                                    for (ReportsTopicPathModel item : list) {
////                                        data.put(item.getTopicId(), item.getTypeV());
////                                    }
////                                }
////                                try {
////                                    isCompleted = false;
////                                    countTopic = 0;
////                                    count = 0;
////                                    getTopicSeenVideoListing(false);
////                                    getTopics(subject);
////                                } catch (Exception e) {
////                                    e.printStackTrace();
////                                }
//                            } else {
//                                if (list != null && list.size() > 0) {
//                                    String path = list.get(0).getTypeV();
//                                    StringBuilder strBuilder = new StringBuilder();
//                                    if (!path.contains(value)) {
//                                        strBuilder.append(path).append("-").append(value);
//                                        if (reportsTopicPathRepository.isDataExist(Util.getUserId(context), board, sClass, subject, topicId)) {
//                                            reportsTopicPathRepository.updateField(Util.getUserId(context), board, sClass, subject, topicId, strBuilder.toString());
//                                        } else {
//                                            ReportsTopicPathModel reportsTopicPathModel = new ReportsTopicPathModel();
//                                            reportsTopicPathModel.setUserId(Util.getUserId(context));
//                                            reportsTopicPathModel.setBoard(board);
//                                            reportsTopicPathModel.setSClass(sClass);
//                                            reportsTopicPathModel.setSubject(subject);
//                                            reportsTopicPathModel.setTopicId(topicId);
//                                            reportsTopicPathModel.setTypeV(strBuilder.toString());
//                                            reportsTopicPathRepository.insertPathDetails(reportsTopicPathModel);
//                                        }
//                                    }
//                                } else {
//                                    topicPathData.add(topicId + "-" + value);
//                                    StringBuilder strBuilder = new StringBuilder();
//                                    int count = 0;
//                                    for (String item : topicPathData) {
//                                        String[] separated = item.split("-");
//                                        if (separated[0].equals(topicId)) {
//                                            if (count == 0) {
//                                                strBuilder.append(separated[1]);
//                                            } else {
//                                                strBuilder.append("-").append(separated[1]);
//                                            }
//                                        }
//                                        count++;
//                                    }
//                                    if (reportsTopicPathRepository.isDataExist(Util.getUserId(context), board, sClass, subject, topicId)) {
//                                        reportsTopicPathRepository.updateField(Util.getUserId(context), board, sClass, subject, topicId, strBuilder.toString());
//                                    } else {
//                                        ReportsTopicPathModel reportsTopicPathModel = new ReportsTopicPathModel();
//                                        reportsTopicPathModel.setUserId(Util.getUserId(context));
//                                        reportsTopicPathModel.setBoard(board);
//                                        reportsTopicPathModel.setSClass(sClass);
//                                        reportsTopicPathModel.setSubject(subject);
//                                        reportsTopicPathModel.setTopicId(topicId);
//                                        reportsTopicPathModel.setTypeV(strBuilder.toString());
//                                        reportsTopicPathRepository.insertPathDetails(reportsTopicPathModel);
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            pathDisposable.dispose();
//                        }
//                    });
//        } else if (type.equals("videoListingTask")) {
//            getList(userId, board, sClass, subject, topicId, value, "videoListingTask").subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new io.reactivex.Observer<Object>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            videoListingTopicDisposable = d;
//                        }
//
//                        @Override
//                        public void onNext(Object o) {
//                            topicVideoArrayList = (ArrayList<VideoModel>) o;
////                            if (refreshAdapter && practiceTopicAdapter != null) {
////                                practiceTopicAdapter.notifyDataSetChanged();
////                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            videoListingTopicDisposable.dispose();
//                        }
//                    });
//        } else if (type.equals("diagnosticTestComplete")) {
//            getList(userId, board, sClass, subject, topicId, value, "diagnosticTestComplete").subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new io.reactivex.Observer<Object>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            diagnosticTestCompleteDisposable = d;
//                        }
//
//                        @Override
//                        public void onNext(Object o) {
//                            ArrayList<ReportsDiagnosticCompleteModel> list = (ArrayList<ReportsDiagnosticCompleteModel>) o;
////                            try {
////                                String comp = "false";
////                                if (list != null && list.size() > 0) {
////                                    if (list.get(0).isComplete()) {
////                                        comp = "true";
////                                    } else {
////                                        comp = "false";
////                                    }
////                                }
////                                for (HashMap<String, String> item : topicsArrayList) {
////                                    if (item.get("TopicID").equals(topicId)) {
////                                        item.put("isCompleted", comp);
////                                        break;
////                                    }
////                                }
////                                practiceTopicAdapter.topicsArrayList = topicsArrayList;
////                                practiceTopicAdapter.setTooltipVisibility(true);
////                                practiceTopicAdapter.notifyDataSetChanged();
////                                refreshTabLayout(Util.getTopicID(Util.getContext()), true);
////
////                                boolean isCompleted = false;
////                                isCompleted = comp.equals("true");
////                                if (reportsDiagnosticTestCompleteRepository.isDataExist(Util.getUserId(Util.getContext()), board, sClass, Util.getSubject(Util.getContext()), topicId)) {
////                                    reportsDiagnosticTestCompleteRepository.updateField(Util.getUserId(Util.getContext()), board, sClass, Util.getSubject(Util.getContext()), topicId, isCompleted);
////                                } else {
////                                    ReportsDiagnosticCompleteModel reportsDiagnosticCompleteModel = new ReportsDiagnosticCompleteModel();
////                                    reportsDiagnosticCompleteModel.setUserId(Util.getUserId(Util.getContext()));
////                                    reportsDiagnosticCompleteModel.setBoard(board);
////                                    reportsDiagnosticCompleteModel.setSClass(sClass);
////                                    reportsDiagnosticCompleteModel.setSubject(Util.getSubject(Util.getContext()));
////                                    reportsDiagnosticCompleteModel.setTopicId(topicId);
////                                    reportsDiagnosticCompleteModel.setComplete(isCompleted);
////                                    reportsDiagnosticTestCompleteRepository.insertTestDetails(reportsDiagnosticCompleteModel);
////                                }
////                            } catch (Exception e) {
////                                e.printStackTrace();
////                            }
//
////                        try {
////                            String comp = "false";
////                            if (list != null && list.size() > 0) {
////                                if(list.get(0).isComplete()){
////                                    comp = "true";
////                                }else{
////                                    comp = "false";
////                                }
////                            }
////                            if(countTopic <= topicsArrayList.size() - 1){
////                                if(countTopic == 0){
////                                    if(topicsArrayList.get(0).get("TopicID").equals(topicId)){
////                                        topicsArrayList.get(countTopic).put("isCompleted", comp);
////                                    }else{
////                                        String tID = topicsArrayList.get(countTopic).get("TopicID");
////                                        getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
////                                        return;
////                                    }
////                                }else{
////                                    topicsArrayList.get(countTopic).put("isCompleted", comp);
////                                }
////                            }
////                            countTopic++;
////                            if(countTopic <= topicsArrayList.size() - 1){
////                                String tID = topicsArrayList.get(countTopic).get("TopicID");
////                                getCompletedForAllTopics(practiceTopicAdapter, topicsArrayList, tID);
////                            }
////                        }catch (Exception e) {
////                            e.printStackTrace();
////                        }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onComplete() {
////                        disposable.dispose();
//                        }
//                    });
//        } else if (type.equals("foundationalTopicData")) {
//            getList(userId, board, sClass, subject, topicId, value, "foundationalTopicData").subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new io.reactivex.Observer<Object>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            foundationalTopicDisposable = d;
//                        }
//
//                        @Override
//                        public void onNext(Object o) {
//
//                            List<FoundationalTopicModel> list = (List<FoundationalTopicModel>) o;
//                            if (list != null && list.size() > 0) {
//                                foundationalTopicData = list;
//                            }
//                            if (foundationalTopicData != null && foundationalTopicData.size() > 0) {
//
//                                for (FoundationalTopicModel item : foundationalTopicData) {
//                                    if(lastTopicId!=null)
//                                    {
//                                        if (lastTopicId.equals(item.getSeniorTopicID())) {
//                                            if (!isPracticeCompleted(item.getTopicId())) {
//                                                int position = 0;
//
////                                            completeType = "foundationalPractice";
////                                            static_completeType = "foundationalPractice";
////                                            for (int i = 0; i < topicsArrayList.size(); i++) {
////                                                if (topicsArrayList.get(i).get("TopicID").equals(lastTopicId)) {
////                                                    position = i;
////                                                    break;
////                                                }
////                                            }
////
////                                            List<Fragment> allFragments = getSupportFragmentManager().getFragments();
////                                            for (Fragment fragment : allFragments) {
////                                                if (fragment instanceof PalVideoListFragment) {
////                                                    ((PalVideoListFragment) fragment).showTestLayout(completeType, item, position);
////                                                } else if (fragment instanceof PalDikshaContentFragment) {
////                                                    ((PalDikshaContentFragment) fragment).showTestLayout(completeType, item, position);
////                                                }
////                                            }
////                                            break;
//                                            }
//                                        }
//                                    }
//
//                                }
//                            }
////                        practiceTopicAdapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            foundationalTopicDisposable.dispose();
//                        }
//                    });
//        } else if (type.equals("testScore")) {
//
//            getList(userId, board, sClass, subject, topicId, value, "testScore").subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new io.reactivex.Observer<Object>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            testScoreTopicDisposable = d;
//                        }
//
//                        @Override
//                        public void onNext(Object o) {
//                            List<ReportsLatestDataTestModel> list = (ArrayList<ReportsLatestDataTestModel>) o;
//                            for (ReportsLatestDataTestModel item : list) {
//                                if (testScoreModelArrayList.size() > 0) {
//                                    for (TestScoreModel data : testScoreModelArrayList) {
//                                        if (data.getTopicId().equals(item.getTopicId()) && data.getType().equals(item.getType())) {
//                                            testScoreModelArrayList.remove(data);
//                                            break;
//                                        }
//                                    }
//                                }
//                                testScoreModelArrayList.add(new TestScoreModel(item.getTopicId(), item.getType(), item.getReportsTestScoreModel().getScores() + "/" + item.getReportsTestScoreModel().getTotalScores(), item.getReportsTestScoreModel().getPercentageScored(), item.getTimeTaken()));
//                            }
//                            runBackgroundTask(userId, board, sClass, subject, lastTopicId,topicId, value, "practiceScore");
////                        practiceTopicAdapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            testScoreTopicDisposable.dispose();
//                        }
//                    });
//        } else if (type.equals("practiceScore")) {
//            getList(userId, board, sClass, subject, topicId, value, "practiceScore").subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new io.reactivex.Observer<Object>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            practiceScoreTopicDisposable = d;
//                        }
//
//                        @Override
//                        public void onNext(Object o) {
//                            ArrayList<ReportsLatestDataPracticeModel> list = (ArrayList<ReportsLatestDataPracticeModel>) o;
//                            for (ReportsLatestDataPracticeModel item : list) {
//                                if (practiceScoreModelArrayList.size() > 0) {
//                                    for (PracticeScoreModel data : practiceScoreModelArrayList) {
//                                        if (data.getTopicId().equals(item.getTopicId())) {
//                                            practiceScoreModelArrayList.remove(data);
//                                            break;
//                                        }
//                                    }
//                                }
//                                practiceScoreModelArrayList.add(new PracticeScoreModel(item.getTopicId(), item.getMastery(),item.getStreakProgress(),item.getCurrentLevel()));
//                            }
////                            updateUi();
////                        practiceTopicAdapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            practiceScoreTopicDisposable.dispose();
//                        }
//                    });
//        } else if (type.equals("finalTestCompletionTask")) {
//            getList(userId, board, sClass, subject, topicId, value, "finalTestCompletionTask").subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new io.reactivex.Observer<Object>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            finalTestCompleteDisposable = d;
//                        }
//
//                        @Override
//                        public void onNext(Object o) {
//                            finalTestTopicIdArrayList = new ArrayList<>();
//                            List<FinalTestCompleteModel> list = (List<FinalTestCompleteModel>) o;
//                            if (list != null && list.size() > 0) {
//                                for (FinalTestCompleteModel item : list) {
//                                    finalTestTopicIdArrayList.add(item.getTopicId());
//                                }
//                            }
////                        practiceTopicAdapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            finalTestCompleteDisposable.dispose();
//                        }
//                    });
//        }
//    }
//
//    private Observable<Object> getList(String userId, String board, String sClass, String subject, String topicId, String value, String type) {
//        if (type.equals("lastTopicIdTask")) {
//            return Observable.fromCallable(() -> {
//                //do something, get your Data object
//                return topicSubjectRepository.getDetail(userId, subject);
//            });
//        } else if (type.equals("pathTask")) {
//            if (topicId == null) {
//                return Observable.fromCallable(() -> {
//                    //do something, get your Data object
//                    return reportsTopicPathRepository.getDetail(Util.getUserId(Util.getContext()), board, sClass, subject, null);
//                });
//            } else {
//                return Observable.fromCallable(() -> {
//                    //do something, get your Data object
//                    return reportsTopicPathRepository.getDetail(Util.getUserId(Util.getContext()), board, sClass, subject, topicId);
//                });
//            }
//        } else if (type.equals("videoListingTask")) {
//            return Observable.fromCallable(() -> {
//                //do something, get your Data object
//                return videoDetailsRepository.getVideoDetails(userId, board, sClass, subject.toLowerCase());
//            });
//        } else if (type.equals("diagnosticTestComplete")) {
//            return Observable.fromCallable(() -> {
//                //do something, get your Data object
//                return reportsDiagnosticTestCompleteRepository.getDetail(Util.getUserId(Util.getContext()), board, sClass, Util.getSubject(Util.getContext()), topicId);
//            });
//        } else if (type.equals("foundationalTopicData")) {
//            return Observable.fromCallable(() -> {
//                //do something, get your Data object
//                return foundationalTopicRepository.getAllFoundationalTopicDetails(Util.getUserId(Util.getContext()));
//            });
//        } else if (type.equals("testScore")) {
//            return Observable.fromCallable(() -> {
//                //do something, get your Data object
//                return reportsLatestDataTestRepository.getDetail(userId, board, sClass, subject, topicId);
//            });
//        } else if (type.equals("practiceScore")) {
//            return Observable.fromCallable(() -> {
//                //do something, get your Data object
//                return reportsLatestDataPracticeRepository.getDetail(userId, board, sClass, subject, "practice", null);
//            });
//        } else if (type.equals("finalTestCompletionTask")) {
//            return Observable.fromCallable(() -> {
//                //do something, get your Data object
//                return finalTestCompleteRepository.getDetail(userId, board, sClass, subject);
//            });
//        }
//        return null;
//    }
//
//    public boolean isDiagnosticTestAttempted(String topicId) {
//        for (TestScoreModel item : testScoreModelArrayList) {
//            if (topicId.equals(item.getTopicId()) && item.getType().equals("diagnostic_test")) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public TestScoreModel getDiagnosticTestScore(String topicId) {
//        TestScoreModel score;
//        for (TestScoreModel item : testScoreModelArrayList) {
//            if (topicId.equals(item.getTopicId()) && item.getType().equals("diagnostic_test")) {
//                score = item;
//                return score;
//            }
//        }
//        return null;
//    }
//
//    public boolean isPracticeCompleted(String topicId) {
//        for (PracticeScoreModel item : practiceScoreModelArrayList) {
//            if (topicId.equals(item.getTopicId()) && item.getScore().equals("100")) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public PracticeScoreModel getPracticeScore(String topicId) {
//        for (PracticeScoreModel item : practiceScoreModelArrayList) {
//            if (topicId.equals(item.getTopicId())) {
//                return item;
//            }
//        }
//        return null;
//    }
//
//    public boolean isFinalTestAttempted(String topicId) {
//        for (TestScoreModel item : testScoreModelArrayList) {
//            if (topicId.equals(item.getTopicId()) && item.getType().equals("simple_test") &&
//                    item.getPercentage().equals("100")) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private void getFoundationTopicDetails() {
//
//        try
//        {
//            foundationalTopicRepository=new FoundationalTopicRepository(context);
//
//            getList().subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new io.reactivex.Observer<Object>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            foundationalTopicDisposable = d;
//                        }
//
//                        @Override
//                        public void onNext(Object o) {
//
//                            List<FoundationalTopicModel> list = (List<FoundationalTopicModel>) o;
//                            if (list != null && list.size() > 0) {
//                                foundationalTopicData = list;
//                            }
//
//
//
////                            if (foundationalTopicData != null && foundationalTopicData.size() > 0) {
////
////                                try
////                                {
////                                    for(int f=foundationalTopicData.size()-1;f>=0;f--) {
////
////                                        FoundationalTopicModel item = foundationalTopicData.get(f);
////                                        if (item.getSeniorTopicID().equals(topicID)) {
////                                            for (int p = ((PalContentListingActivity) context).practiceScoreModelArrayList.size() - 1; p >= 0; p--) {
////                                                PracticeScoreModel data = ((PalContentListingActivity) context).practiceScoreModelArrayList.get(p);
////                                                if (data.getTopicId().equals(item.getTopicId())) {
////                                                    if (!data.getScore().equals("100")) {
////
////                                                    }
////                                                }
////                                            }
////                                        }
////                                    }
////                                    for (FoundationalTopicModel item : foundationalTopicData) {
////                                        if (topicID.equals(item.getSeniorTopicID())) {
////                                            if (!PalAssignedFragment.palAssignedFragment.isPracticeCompleted(item.getTopicId())) {
////                                                int position = 0;
////
//////                                                completeType = "foundationalPractice";
//////                                                static_completeType = "foundationalPractice";
//////                                                for (int i = 0; i < topicsArrayList.size(); i++) {
//////                                                    if (topicsArrayList.get(i).get("TopicID").equals(lastTopicId)) {
//////                                                        position = i;
//////                                                        break;
//////                                                    }
//////                                                }
////
//////                                                List<Fragment> allFragments = getSupportFragmentManager().getFragments();
//////                                                for (Fragment fragment : allFragments) {
//////                                                    if (fragment instanceof PalVideoListFragment) {
//////                                                        ((PalVideoListFragment) fragment).showTestLayout(completeType, item, position);
//////                                                    } else if (fragment instanceof PalDikshaContentFragment) {
//////                                                        ((PalDikshaContentFragment) fragment).showTestLayout(completeType, item, position);
//////                                                    }
//////                                                }
//////                                                    break;
////                                            }
////                                        }
////                                    }
////                                }catch (Exception ee){
////                                    System.out.println("++++++ handled");
////                                }
////
////                            }
//
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            foundationalTopicDisposable.dispose();
//                        }
//                    });
//
//
//        }catch (Exception f){}
//    }
//
//    private Observable<Object> getList() {
//        return Observable.fromCallable(() -> {
//            //do something, get your Data object
//            return foundationalTopicRepository.getAllFoundationalTopicDetails(Util.getUserId(Util.getContext()));
//        });
//    }




}
