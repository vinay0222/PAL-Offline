package com.idreameducation.ipreppal.pal.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.PalMobile.activity.PalContentListingActivity_Mobile;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.pal.adapter.BooksAdapter;
import com.idreameducation.ipreppal.pal.activity.PalContentListingActivity;
import com.idreameducation.ipreppal.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class PalBooksFragment extends Fragment {
    private Context context;
    private Global global;
    private RecyclerView recyclerView;
    private String board;
    private String sClass;
    private String subject;
    private String language = "english";
    private String categoryID;
    private LinearLayout nobookLayout;

    public ArrayList<HashMap<String, Object>> booksArrayList;

    public PalBooksFragment() {
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pal_fragment_books, container, false);
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();
        recyclerView = view.findViewById(R.id.recyclerView);
        nobookLayout = view.findViewById(R.id.nobookLayout);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        GridLayoutManager manager2 = new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(manager);
        language = Util.getSelectedLanguage(context);

        if(Util.isPortraitMode(context)) {
            board = PalContentListingActivity_Mobile.board;
            sClass = PalContentListingActivity_Mobile.sClass;
            subject = PalContentListingActivity_Mobile.subject;
        }
        else {
            board = PalContentListingActivity.board;
            sClass = PalContentListingActivity.sClass;
            subject = PalContentListingActivity.subject;
        }

        try {
            getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public PalBooksFragment(String contentType) {
        this.categoryID = contentType;
    }

    private void getContent() throws Exception {

        if(Util.isOfflineMode(context))
        {

            booksArrayList=new ArrayList<>();

            try {
                String filePath = ".iDream_content/offlinetab_PAL/Class" + sClass + "_core_content.txt";
                JSONObject jsonObject = Util.readJsonFile(context, filePath);
                JSONObject object__11 = jsonObject.getJSONObject(Util.getSelectedLanguage(context));
                JSONObject object__2 = object__11.getJSONObject("books_ncert");
                JSONArray object__ = object__2.getJSONArray(Util.getSubject(context));


                for(int i=0;i<object__.length();i++)
                {
                    JSONObject object__1 = object__.getJSONObject(i);

                    String name = object__1.getString("name");

                    JSONArray object___2 = object__1.getJSONArray("topics");
                    JSONObject object___ = object___2.getJSONObject(0);

                    HashMap<String,String> data=new HashMap<>();
                    data.put("class",String.valueOf(object___.get("class")));
                    data.put("detail",String.valueOf(object___.get("detail")));
                    data.put("id",String.valueOf(object___.get("id")));
                    data.put("name",String.valueOf(object___.get("name")));
                    data.put("offlineLink",String.valueOf(object___.get("offlineLink")));
                    data.put("offlineThumbnail",String.valueOf(object___.get("offlineThumbnail")));
                    data.put("onlineLink",String.valueOf(object___.get("onlineLink")));
                    data.put("subjectID",String.valueOf(object___.get("subjectID")));
                    data.put("subjectName",String.valueOf(object___.get("subjectName")));
                    data.put("thumbnail",String.valueOf(object___.get("thumbnail")));
                    data.put("topicName",String.valueOf(object___.get("topicName")));

                    ArrayList<HashMap<String,String>> dataList=new ArrayList<>();
                    dataList.add(data);
                    HashMap<String,Object> map1=new HashMap<>(); // contain topic name & list
                    map1.put("topics",dataList);
                    map1.put("name",name);

                    booksArrayList.add(map1);
                }


                BooksAdapter booksAdapter = new BooksAdapter(context, booksArrayList);
                recyclerView.setAdapter(booksAdapter);

                if(booksArrayList.size()!=0) nobookLayout.setVisibility(View.GONE);
                else nobookLayout.setVisibility(View.VISIBLE);


            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else global.getDatabaseReference().child("core_content").child(board).child(sClass).child(language).child(categoryID).child(subject).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            booksArrayList = (ArrayList<HashMap<String, Object>>) snapshot.getValue();
                            BooksAdapter booksAdapter = new BooksAdapter(context, booksArrayList);
                            recyclerView.setAdapter(booksAdapter);
                            nobookLayout.setVisibility(View.GONE);
                        }
                        else {
                            nobookLayout.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }


        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = Resources.getSystem();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
