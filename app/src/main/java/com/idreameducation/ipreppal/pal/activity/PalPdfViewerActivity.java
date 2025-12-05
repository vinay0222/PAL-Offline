package com.idreameducation.ipreppal.pal.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTimeSpentModel;
import com.idreameducation.ipreppal.roomdatabase.model.ReportsTopicWiseVideoModel;
import com.idreameducation.ipreppal.roomdatabase.repository.BooksReportRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTimeSpentRepository;
import com.idreameducation.ipreppal.roomdatabase.repository.ReportsTopicWiseVideoRepository;
import com.idreameducation.ipreppal.util.ApplicationConstants;
import com.idreameducation.ipreppal.util.TouchImageView;
import com.idreameducation.ipreppal.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//import static com.idream.android.pal.PracticeTopicActivity.hideNavigationBar;

public class PalPdfViewerActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    ArrayList<Long> list = new ArrayList<>();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    long count = 0;
    File file;
    //Prevent Out of memory exception
    private DownloadManager downloadManager;
    private long refid;

    private ProgressBar progressbar;
    private Global global;
    private Context context;
    private String bookName;
    private String bookId;
    private long startTime = 0L;
    private String sClass;
    private final android.os.Handler customHandler = new android.os.Handler();
    private final Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            customHandler.postDelayed(this, 1);
        }
    };
    private long totalTimee;
    private long classTime;
    private PDFView pdfView;
    private String filename;
    private int totalpages;
    private String extStorageDirectory;
    private File folder;
    private RelativeLayout reletive;
    private BooksReportRepository booksReportRepository;
    private TouchImageView pdf_image;
    private FloatingActionButton button_pre_doc;
    private FloatingActionButton button_next_doc;
    private String FILENAME_ = "";
    private int pageIndex;
    private ImageView imageViewBook;
    private ImageView imageViewBack;
    private PdfRenderer pdfRenderer;
    private TextView textTitle,pageRead;
    private TextView downloadTextView;
    private TextView titleBooks;
    private PdfRenderer.Page currentPage;
    private ProgressBar progressBar;
    String offlinePath;

    String topicId_new,bookId_new,topic_name_main;


    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {

            isloading=true;
            checkConnection(false);
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.e("IN", "" + referenceId);
            list.remove(referenceId);
            progressbar.setVisibility(View.GONE);
            File pth = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            Uri filePath = Uri.parse(pth + "/iDreamPDF/" + bookName + ".pdf");
            file = new File(filePath.toString());


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                progressbar.setVisibility(View.GONE);
                downloadTextView.setVisibility(View.GONE);
                imageViewBook.setVisibility(View.GONE);
                textTitle.setVisibility(View.GONE);
                try {


//                    openRenderer(context, file);
                    countPages(file);
                    isloading=false;
                    pdfView.fromFile(file)
                            .spacing(2)
                            .onPageChange(PalPdfViewerActivity.this)
                            .scrollHandle(new DefaultScrollHandle(context))
                            .onError(new OnErrorListener() {
                                @Override
                                public void onError(Throwable t) {
                                    t.printStackTrace();
                                    String errorWhile;
                                    if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                    {
                                        errorWhile ="कृपया प्रतीक्षा करें हम कुछ दिक्कतों का सामना कर रहे हैं!";
                                    }else {
                                        errorWhile = "Error while opening book!";
                                    }
                                    Util.openGifDialogue(context,errorWhile);
                                    //Toast.makeText(PalPdfViewerActivity.this, "Error while opening book!", Toast.LENGTH_SHORT).show();
                                    onBackPressed();

                                }
                            })
                            .load();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        pdfView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                            @Override
                            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                progressBar.setMax(totalpages); // 100 maximum value for the progress value
                                progressBar.setProgress(pdfView.getCurrentPage() + 1);
                            }
                        });
                    }


                    progressBar.setMax(totalpages); // 100 maximum value for the progress value
                    progressBar.setProgress(pdfView.getCurrentPage() + 1);
                    //Toast.makeText(global, "Page Count 2:- " + totalpages, Toast.LENGTH_SHORT).show();
                    int pageno = pdfView.getCurrentPage() + 1;
                    pageRead.setText("Page " + pageno + " of " + totalpages);
                    isloading=false;
                    mProgressBar.setVisibility(View.GONE);
                    imageViewCrossVideo2.setVisibility(View.GONE);
                    slow_internet_Text.setVisibility(View.GONE);
                    showPage(pageIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                isloading=false;
                displayFromAsset(file);
            }

        }
    };


    private WindowManager windowManager;
    private ImageView chatHead;
    WindowManager.LayoutParams params;

    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setContext(context);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_pdf_viewer_new);
        Util.handleNotch(this);
        context = this;

        extStorageDirectory = Environment.getExternalStorageDirectory().toString() + "/Download";
        folder = new File(extStorageDirectory, "iDreamPDF");
        folder.mkdir();

//        if(!folder.canRead()) {
//            if (Build.VERSION.SDK_INT >= 30){
//                if (!Environment.isExternalStorageManager()){
//                    Intent getpermission = new Intent();
//                    getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                    startActivity(getpermission);
//                }
//            }
//        }

        progressBar = findViewById(R.id.progressBar);
        pdfView = findViewById(R.id.pdfView);
        imageViewBook = findViewById(R.id.imageViewBook);
        imageViewBack = findViewById(R.id.imageViewBack);
        progressbar = findViewById(R.id.progressbar);
        reletive = findViewById(R.id.reletive);
        pdf_image = findViewById(R.id.pdf_image);
        textTitle = findViewById(R.id.textTitle);
        pageRead = findViewById(R.id.pageRead);
        downloadTextView = findViewById(R.id.downloadTextView);

        button_pre_doc = findViewById(R.id.button_pre_doc);
        button_next_doc = findViewById(R.id.button_next_doc);
        button_pre_doc.setVisibility(View.GONE);
        button_next_doc.setVisibility(View.GONE);
        //hideNavigationBar(getWindow());
        titleBooks = findViewById(R.id.titleBooks);
        global = (Global) getApplicationContext();
        global.sendData("Books", this.getClass().getName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.placeholder(R.mipmap.books);

        filename = getIntent().getStringExtra("onlineLink");
        bookName = getIntent().getStringExtra("topicName");
        titleBooks.setText(getIntent().getStringExtra("topicName"));
        offlinePath = getIntent().getStringExtra("offlineLink");
        topicId_new = getIntent().getStringExtra("topicId");
        topic_name_main = getIntent().getStringExtra("topic_name_main");
        bookId_new = getIntent().getStringExtra("bookId");
        try {
            type = getIntent().getStringExtra("categoryID");
            if(type==null) type="";
        } catch (Exception e) {
            type="";
            e.printStackTrace();
        }

        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.setVisibility(View.VISIBLE);
        imageViewBack.setVisibility(View.VISIBLE);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.preventTwoClick(v);
                onBackPressed();
            }
        });
        pdfView.fitToWidth();
        pdfView.setSwipeVertical(true);
        pdfView.setVerticalScrollBarEnabled(true);
        pdfView.setHorizontalScrollBarEnabled(true);
        pdfView.computeScroll();
        pdfView.enableDoubletap(true);
        pdfView.getCurrentPage();
        pdfView.getPageCount();

        getTimeforfirebase();
        checkConnection(false);
        if (offlinePath != null) {


            try {

                getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Uri filePath = Uri.parse(Util.getSDCardPath(context) + "/.iDream_content/books/" + offlinePath);
            file = new File(filePath.toString());

            if (file.exists()) {
                imageViewBook.setVisibility(View.GONE);
                textTitle.setVisibility(View.GONE);
                downloadTextView.setVisibility(View.GONE);
                if (Util.isPermissionGranted(context)) {
                    Util.setPermissonGranted(context, true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        progressbar.setVisibility(View.GONE);
//                            openRenderer(context, file);
                        try {
                            countPages(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        pdfView.fromFile(file)
                                .spacing(2)
                                .onPageChange(this)
                                .scrollHandle(new DefaultScrollHandle(this))
                                .onError(new OnErrorListener() {
                                    @Override
                                    public void onError(Throwable t) {
                                        t.printStackTrace();
                                        String errorWhile;
                                        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
                                        {
                                            errorWhile ="कृपया प्रतीक्षा करें हम कुछ दिक्कतों का सामना कर रहे हैं!";
                                        }else {
                                            errorWhile = "Error while opening book!";
                                        }
                                        Util.openGifDialogue(context,errorWhile);
                                        //Toast.makeText(PalPdfViewerActivity.this, "Error while opening book!", Toast.LENGTH_SHORT).show();
                                        onBackPressed();

                                    }
                                })
                                .load();
                        //Util.setBooksTotalCount(context,Util.getBooksTotalCount(context)+1);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            pdfView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                                @Override
                                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                    progressBar.setMax(totalpages); // 100 maximum value for the progress value
                                    progressBar.setProgress(pdfView.getCurrentPage() + 1);
                                    int pageno = pdfView.getCurrentPage() + 1;
                                    pageRead.setText("Page " + pageno + " of " + totalpages);
                                    isloading=false;
                                    mProgressBar.setVisibility(View.GONE);
                                    imageViewCrossVideo2.setVisibility(View.GONE);
                                    slow_internet_Text.setVisibility(View.GONE);
                                }
                            });
                        }
                        progressBar.setMax(totalpages); // 100 maximum value for the progress value
                        progressBar.setProgress(pdfView.getCurrentPage() + 1);
                        // Toast.makeText(global, "Page Count 3:- " + totalpages, Toast.LENGTH_SHORT).show();
                        int pageno = pdfView.getCurrentPage() + 1;
                        pageRead.setText("Page " + pageno + " of " + totalpages);
                        isloading=false;
                        mProgressBar.setVisibility(View.GONE);
                        imageViewCrossVideo2.setVisibility(View.GONE);
                        slow_internet_Text.setVisibility(View.GONE);
                        try {
                            showPage(pageIndex);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        displayFromAsset(file);
                    }
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                }
            } else {
                imageViewBook.setVisibility(View.VISIBLE);
                textTitle.setVisibility(View.VISIBLE);
                downloadTextView.setVisibility(View.VISIBLE);
                pdfView.useBestQuality(false);
                Constants.Cache.CACHE_SIZE = 40;
                downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_EXPORTED);

                if (Util.isPermissionGranted(context)) {
                    Util.setPermissonGranted(context, true);

                    File pth = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    Uri filePath_ = Uri.parse(pth + "/iDreamPDF/" + bookName + ".pdf");
                    file = new File(filePath_.toString());
                    if (file.exists()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            progressbar.setVisibility(View.GONE);
                            downloadTextView.setVisibility(View.GONE);
                            imageViewBook.setVisibility(View.GONE);
                            textTitle.setVisibility(View.GONE);


                            try {
                                countPages(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            pdfView.fromFile(file)
                                    .defaultPage(0)
                                    .onPageChange(this)
                                    .enableAnnotationRendering(true)
                                    .onLoad(this)
                                    .onPageChange(this)
                                    .scrollHandle(new DefaultScrollHandle(this))
                                    .spacing(10) // in dp
                                    .onPageError(this)
                                    .load();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                pdfView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                                    @Override
                                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                        progressBar.setMax(totalpages); // 100 maximum value for the progress value
                                        progressBar.setProgress(pdfView.getCurrentPage() + 1);
                                    }
                                });
                            }

                            progressBar.setMax(totalpages); // 100 maximum value for the progress value
                            progressBar.setProgress(pdfView.getCurrentPage() + 1);

//                            try {
//
//                                countPages(file);
//                                pdfView.fromFile(file)
//                                        .spacing(2)
//                                        .onPageChange(PalPdfViewerActivity.this)
//                                        .scrollHandle(new DefaultScrollHandle(context))
//                                        .onError(new OnErrorListener() {
//                                            @Override
//                                            public void onError(Throwable t) {
//                                                t.printStackTrace();
////                                                String errorWhile;
////                                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
////                                                {
////                                                    errorWhile ="कृपया प्रतीक्षा करें हम कुछ दिक्कतों का सामना कर रहे हैं!";
////                                                }else {
////                                                    errorWhile = "Error while opening book!";
////                                                }
////                                                Util.openGifDialogue(context,errorWhile);
//                                                Toast.makeText(PalPdfViewerActivity.this, "Error while opening book!", Toast.LENGTH_SHORT).show();
//                                              //  onBackPressed();
//
//                                            }
//                                        })
//                                        .load();
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    pdfView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                                        @Override
//                                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                                            progressBar.setMax(totalpages); // 100 maximum value for the progress value
//                                            progressBar.setProgress(pdfView.getCurrentPage() + 1);
//                                            int pageno = pdfView.getCurrentPage() + 1;
//                                            pageRead.setText("Page " + pageno + " of " + totalpages);
//                                        }
//                                    });
//                                }
//
//
//                                progressBar.setMax(totalpages); // 100 maximum value for the progress value
//                                progressBar.setProgress(pdfView.getCurrentPage() + 1);
//                                //Toast.makeText(global, "Page Count 2:- " + totalpages, Toast.LENGTH_SHORT).show();
//                                int pageno = pdfView.getCurrentPage() + 1;
//                                pageRead.setText("Page " + pageno + " of " + totalpages);
//
//                                showPage(pageIndex);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

                        } else {
                            displayFromAsset(file);
                        }
                    } else {
                        downloadURL(bookName + ".pdf");
                    }


                    //new DownloadFile().execute(filename, bookName + ".pdf");
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                }
            }

        } else {

            findViewById(R.id.pdf_image).setOnTouchListener(new OnSwipeTouchListener(PalPdfViewerActivity.this) {
                public void onSwipeTop() {
                    onNextDocClick();

                }

                public void onSwipeRight() {
                    onPreviousDocClick();
                }

                public void onSwipeLeft() {
                    onNextDocClick();

                }

                public void onSwipeBottom() {
                    onPreviousDocClick();
                }
            });

            button_pre_doc.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    Util.preventTwoClick(view);
                    try {
                        onPreviousDocClick();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            button_next_doc.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    Util.preventTwoClick(view);
                    onNextDocClick();
                }
            });

            try {
//            filename = "https://firebasestorage.googleapis.com/v0/b/iprep-cad1e.appspot.com/o/Digital%20Books%2FPratham%20Books%2FBooks%2FEnglish%20Books%2FA%20Butterfly%20Smile.pdf?alt=media&token=613acb4a-8432-4c59-8a00-ecd67d759015";

//            bookName = "test";
                textTitle.setText(bookName);
                try {
                    Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");
                    if (regex.matcher(bookName).find()) {
                        bookName = bookName.replaceAll("[$&+,:;=\\\\?@#|/'<>.^*()%!-]", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                bookId = filename.split("token=")[1];
                file = new File(folder, bookName + ".pdf");
                FILENAME_ = bookName + ".pdf";

                booksReportRepository = new BooksReportRepository(getApplicationContext());
                if (!Util.checkInternetConnection(context)) {
                    try {
                        Util.showToast(context, "Internet Not Available");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    onBackPressed();
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
                finish();
                Util.showToast(context, "Link not available");

            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }

    public long getTime() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 1);
        return updatedTime;
    }

    public void stopTimer() {
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }


    boolean isloading=true;
    ProgressBar mProgressBar;

    ImageView imageViewCrossVideo2;
    TextView slow_internet_Text;
    private void checkConnection(boolean first) {


        mProgressBar=findViewById(R.id.progress);
        slow_internet_Text=findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2=findViewById(R.id.imageViewCrossVideo2);

        if(!Util.isOfflineMode(context)) {
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isloading) {
                        if (first) {
                            mProgressBar.setVisibility(View.GONE);
                            slow_internet_Text.setText("We are unable to load PDF\nDue to Slow Internet Connection");
                            slow_internet_Text.setVisibility(View.VISIBLE);
                            imageViewCrossVideo2.setVisibility(View.VISIBLE);
                        } else {
                            checkConnection(true);
                            slow_internet_Text.setVisibility(View.VISIBLE);
                        }

                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        slow_internet_Text.setVisibility(View.GONE);
                        imageViewCrossVideo2.setVisibility(View.GONE);
                    }
                }
            }, 10000);//time in milisecond

            imageViewCrossVideo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
        else
        {
            mProgressBar.setVisibility(View.GONE);
            slow_internet_Text.setVisibility(View.GONE);
            imageViewCrossVideo2.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Util.setLogoutSelection(context, false);

    }


    @Override
    public void loadComplete(int nbPages) {
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onPageError(int page, Throwable t) {

    }

    private void displayFromAsset(File assetFileName) {
        pdfView.setVisibility(View.VISIBLE);
        imageViewBack.setVisibility(View.VISIBLE);
        reletive.setVisibility(View.GONE);

        try {
            countPages(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfView.fromFile(assetFileName)
                .defaultPage(0)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .onPageChange(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .load();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pdfView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    progressBar.setMax(totalpages); // 100 maximum value for the progress value
                    progressBar.setProgress(pdfView.getCurrentPage() + 1);
                }
            });
        }

        progressBar.setMax(totalpages); // 100 maximum value for the progress value
        progressBar.setProgress(pdfView.getCurrentPage() + 1);
        //Toast.makeText(global, "Page Count 4:- " + totalpages, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(onComplete);
            try {
                syncClassTime();
                stopTimer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                syncClassTime();
                stopTimer();
            } catch (Exception ef) {
                ef.printStackTrace();
            }
        }
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        progressBar.setMax(totalpages);
        progressBar.setProgress(pdfView.getCurrentPage() + 1);
        int pageno = pdfView.getCurrentPage() + 1;
        pageRead.setText("Page " + pageno + " of " + totalpages);
        isloading=false;
        mProgressBar.setVisibility(View.GONE);
        imageViewCrossVideo2.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    File file = new File(folder, bookName + ".pdf");
                    if (file.exists()) {
                        // displayFromAsset(file);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            progressbar.setVisibility(View.GONE);
                            try {

                                countPages(file);

//                                openRenderer(context, file);
                                pdfView.fromFile(file)
                                        .spacing(2)
                                        .onPageChange(this)
                                        .scrollHandle(new DefaultScrollHandle(this))
                                        .onError(new OnErrorListener() {
                                            @Override
                                            public void onError(Throwable t) {
                                                t.printStackTrace();
                                                String errorWhile;
                                                if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi")) {
                                                    errorWhile = "कृपया प्रतीक्षा करें हम कुछ दिक्कतों का सामना कर रहे हैं!";
                                                } else {
                                                    errorWhile = "Error while opening book!";
                                                }
                                                Util.openGifDialogue(context, errorWhile);
                                                // Toast.makeText(PalPdfViewerActivity.this, "Error while opening book!", Toast.LENGTH_SHORT).show();
//                                                onBackPressed();

                                            }
                                        })
                                        .load();
                                // Util.setBooksTotalCount(context,Util.getBooksTotalCount(context)+1);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    pdfView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                                        @Override
                                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                            progressBar.setMax(totalpages); // 100 maximum value for the progress value
                                            progressBar.setProgress(pdfView.getCurrentPage() + 1);
                                            int pageno = pdfView.getCurrentPage() + 1;
                                            pageRead.setText("Page " + pageno + " of " + totalpages);
                                            isloading=false;
                                            mProgressBar.setVisibility(View.GONE);
                                            imageViewCrossVideo2.setVisibility(View.GONE);
                                            slow_internet_Text.setVisibility(View.GONE);
                                        }
                                    });
                                }

                                progressBar.setMax(totalpages); // 100 maximum value for the progress value
                                progressBar.setProgress(pdfView.getCurrentPage() + 1);
                                //Toast.makeText(global, "Page Count 1:- " + totalpages, Toast.LENGTH_SHORT).show();

                                int pageno = pdfView.getCurrentPage() + 1;
                                pageRead.setText("Page " + pageno + " of " + totalpages);
                                isloading=false;
                                mProgressBar.setVisibility(View.GONE);
                                imageViewCrossVideo2.setVisibility(View.GONE);
                                slow_internet_Text.setVisibility(View.GONE);
                                showPage(pageIndex);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            displayFromAsset(file);
                        }
                    } else {
                        try {
                            downloadURL(bookName + ".pdf");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(PalPdfViewerActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void downloadURL(String path) {
        list.clear();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(filename));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("iDream Learning App");
        request.setDescription("Book downloading...");
        request.setVisibleInDownloadsUi(true);
//        request.setDestinationUri(Uri.fromFile(path));
//        Environment.getExternalStoragePublicDirectory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/iDreamPDF/" + path);
        refid = downloadManager.enqueue(request);

        Log.e("OUT", "" + refid);
        list.add(refid);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showPage(int index) throws Exception {
        //button_pre_doc.setVisibility(View.VISIBLE);
        //button_next_doc.setVisibility(View.VISIBLE);
        try {
            if (pdfRenderer.getPageCount() <= index) {

                return;
            }
            // Make sure to close the current page before opening another one.
            if (null != currentPage) {
                currentPage.close();
            }
            // Use `openPage` to open a specific page in PDF.
            currentPage = pdfRenderer.openPage(index);
            // Important: the destination bitmap must be ARGB (not RGB).
            Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(),
                    Bitmap.Config.ARGB_8888);
            // Here, we render the page onto the Bitmap.
            // To render a portion of the page, use the second and third parameter. Pass nulls to get
            // the default result.
            // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
            currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            // We are ready to show the Bitmap to user.
            pdf_image.setImageBitmap(bitmap);
            try {
                updateUi();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Updates the state of 2 control buttons in response to the current page index.
     */
    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateUi() throws Exception {
        try {
            int index = currentPage.getIndex();
            int pageCount = pdfRenderer.getPageCount();
            if (0 != index) {
                button_pre_doc.setVisibility(View.VISIBLE);
                button_pre_doc.setEnabled(true);
            } else {
                button_pre_doc.setVisibility(View.GONE);
                button_pre_doc.setEnabled(false);
            }
            if (index + 1 < pageCount) {
                button_next_doc.setEnabled(true);
                button_next_doc.setVisibility(View.VISIBLE);
            } else {
                button_next_doc.setVisibility(View.GONE);
                button_next_doc.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int getPageCount() {
        return pdfRenderer.getPageCount();
    }


    private void countPages(File pdfFile) throws IOException {
        try {

            ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfRenderer pdfRenderer = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                totalpages = pdfRenderer.getPageCount();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onPreviousDocClick() {
        try {
            showPage(currentPage.getIndex() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onNextDocClick() {
        try {
            showPage(currentPage.getIndex() + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setContext(context);
    }

    @Override
    protected void onPause() {

        Util.preventPause(context,getTaskId());
        super.onPause();
    }



    String date;
    long timeget;

    private void getTimeforfirebase() {
        reportsTimeSpentRepository = new ReportsTimeSpentRepository(context);
    date = Util.getCurrentDateWithDifferentFormat();
    if (Util.isOfflineMode(context)) {
        getList(Util.getUserId(context), Util.getSelectedBoard(context), Util.getSelectedClass(context), type, date).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        timeTaskDisposable=d;
                    }

                    @Override
                    public void onNext(Object o) {
                        ArrayList<ReportsTimeSpentModel> list = (ArrayList<ReportsTimeSpentModel>) o;
                        if (list != null && list.size() > 0) {
                            for (ReportsTimeSpentModel item : list) {
                                timeget = item.getTime();
                            }
                        } else {
                            timeget = 0;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        timeTaskDisposable.dispose();
                    }
                });
    } else {
        if(type.equals("books_ncert"))
        {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context))
                    .child(Util.getSelectedBoard(context))
                    .child(Util.getSelectedClass(context)).child("time_spent")
                    .child(date)
                    .child(Util.getSubjectName(context).toLowerCase().replace(" ","_"))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if (snapshot.getValue() != null) {
                                    if (snapshot.getValue() != null) {
                                        timeget = (long) snapshot.getValue();
                                    } else {
                                        timeget = 0;
                                    }
                                }
                                else {
                                    timeget = 0;
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
        else
        {
            String name="Books";
            if(Util.getSelectedLanguage(context).equals("hindi")) {
                name="पुस्तकें";
            }
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context))
                    .child(Util.getSelectedBoard(context))
                    .child(Util.getSelectedClass(context)).child("time_spent")
                    .child(date)
                    .child(name)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if (snapshot.getValue() != null) {
                                    if (snapshot.getValue() != null) {
                                        timeget = (long) snapshot.getValue();
                                    } else {
                                        timeget = 0;
                                    }
                                }
                                else {
                                    timeget = 0;
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

    }
}

    String type="";
    boolean once=true;
    String assignedData,assignedKey,batchID;
    String assignmentKey,assignmentName,teacherID;

    private void syncClassTime() {

        if(mProgressBar.getVisibility()==View.VISIBLE) return;

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        int pageReadByUser = pdfView.getCurrentPage()+1;


        long syncClassTime = updatedTime + classTime;
        long timeTosync=timeget+syncClassTime;
        saveAssignedReport();

        String category="books_stories", categoryID="books_stories", category_name="Story Books";

        if(type.equals("books_ncert")) {
            category="books_ncert";
            categoryID="books_ncert";
            category_name="Ncert Books";
        }

        HashMap<String, String> mapToSync = new HashMap<>();
        mapToSync.put("name", bookName);
        mapToSync.put("time", "" + syncClassTime);
        mapToSync.put("finaltime", "" + syncClassTime);
        mapToSync.put("watchedTime", ""+getTime());
        mapToSync.put("pageRead", ""+pageReadByUser+"/"+totalpages);
        mapToSync.put("username", Util.getUsername(context));
        mapToSync.put("userclass", Util.getSelectedClass(context));
        mapToSync.put("date", formattedDate);
        mapToSync.put("subject", topic_name_main);
        mapToSync.put("class", Util.getSelectedClass(context));
        mapToSync.put("category",category);
        mapToSync.put("topicName", topic_name_main);
        mapToSync.put("topicId", topicId_new);
        mapToSync.put("bookId", bookId_new);
        mapToSync.put("offlineLink",offlinePath);
        mapToSync.put("onlineLink",filename);
        mapToSync.put("categoryID",categoryID);

        String topicName_new = topicId_new.substring(0, 1).toUpperCase() + topicId_new.substring(1);

        HashMap<String, String> row_usage = new HashMap<>();
        row_usage.put("content_name", bookName.trim());
        row_usage.put("time_spent", "" + syncClassTime);
        row_usage.put("pageRead", ""+pageReadByUser+"/"+totalpages);
        row_usage.put("username", Util.getUsernameShowable(context));
        row_usage.put("subject", topicId_new);
        row_usage.put("subject_name", topicName_new);
        row_usage.put("class", Util.getSelectedClass(context));
        row_usage.put("category",category);
        row_usage.put("topic_name", topic_name_main.trim());
        row_usage.put("topic", topicId_new);
        row_usage.put("userId", Util.getUserId(context));
        row_usage.put("language", Util.getSelectedLanguage(context));
        row_usage.put("projectId", Util.getProjectId(context));
        row_usage.put("schoolID", Util.getSchoolId(context));
        row_usage.put("schoolName", Util.getSchoolName(context));
        row_usage.put("state", Util.getSelectedState(context));
        row_usage.put("district", Util.getDistrict(context));
        row_usage.put("board",Util.getSelectedBoard(context));
        row_usage.put("category_name",category_name);
        row_usage.put("userType", "students");
        row_usage.put("app_id", Util.getAPPID(context));

        String date = Util.getCurrentDateWithDifferentFormat();
        global.getDatabaseReference().child(Util.rawUsageNode).child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);
        global.getDatabaseReference().child(Util.segmentedRawUsageNode).child(Util.getSchoolId(context)).child("" + Util.getCurrentDate()).setValue(row_usage);
        global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("topic_wise").child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child(Util.getSelectedClass(context)).child(Util.getSelectedLanguage(context)).child(category).child(categoryID).child("" + formattedDate).child(topicId_new).child("detail").child(bookId_new).child("" + System.currentTimeMillis()).setValue(mapToSync);

        if(type.equals("books_ncert")) {
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child(Util.getSelectedClass(context)).child("time_spent")
                    .child(date)
                    .child(Util.getSubjectName(context).toLowerCase().replace(" ","_"))
                    .setValue(timeTosync);
        }
        else {
            String name="Books";
            if(Util.getSelectedLanguage(context).equals("hindi")) {
                name="पुस्तकें";
            }
            global.getDatabaseReference().child(ApplicationConstants.REPORTS).child("user_time_spent").child(Util.getUserId(context)).child(Util.getSelectedBoard(context)).child(Util.getSelectedClass(context)).child("time_spent")
                    .child(date)
                    .child(name)
                    .setValue(timeTosync);
        }

        ReportsTopicWiseVideoRepository reportsTopicWiseVideoRepository;

        reportsTopicWiseVideoRepository=new ReportsTopicWiseVideoRepository(context);

        if(once)
        {

            String board=Util.getSelectedBoard(context),subject=bookId_new,topicName=topic_name_main,time=syncClassTime+"",videoName=bookName,id=topic_name_main+"_"+bookName;

            if(type.equals("books_ncert")) topicName = Util.getSubjectName(context);

            if(reportsTopicWiseVideoRepository.isDataExist(Util.getUserId(context), board, Util.getSelectedClass(context), videoName, categoryID, topicName, date, System.currentTimeMillis(),Util.getSelectedLanguage(context))){
                reportsTopicWiseVideoRepository.updateField(Util.getUserId(context), board, Util.getSelectedClass(context), videoName, categoryID, topicName,
                        date, System.currentTimeMillis(), topicName, time + "", topicName, timeTosync + "", topicName,Util.getSelectedLanguage(context));
                once=false;
            }else {
                ReportsTopicWiseVideoModel reportsTopicWiseVideoModel = new ReportsTopicWiseVideoModel();
                reportsTopicWiseVideoModel.setUserId(Util.getUserId(context));
                reportsTopicWiseVideoModel.setBoard(board);
                reportsTopicWiseVideoModel.setSClass(Util.getSelectedClass(context));
                reportsTopicWiseVideoModel.setSubject(topicName);
                reportsTopicWiseVideoModel.setDate(date);
                reportsTopicWiseVideoModel.setName(topicName);
                reportsTopicWiseVideoModel.setTime(System.currentTimeMillis());
                reportsTopicWiseVideoModel.setType(categoryID);
                reportsTopicWiseVideoModel.setTopicId(topicName);
                reportsTopicWiseVideoModel.setTopicName(topicName);
                reportsTopicWiseVideoModel.setTotalTime(syncClassTime + "");
                reportsTopicWiseVideoModel.setVTime(timeTosync + "");
                reportsTopicWiseVideoModel.setVideoName(videoName);
                reportsTopicWiseVideoModel.setVideoId(id);
                reportsTopicWiseVideoModel.setLang(Util.getSelectedLanguage(context));
                reportsTopicWiseVideoModel.setSubjectName(topicName);
                reportsTopicWiseVideoRepository.insertVideoDetails(reportsTopicWiseVideoModel);
                once=false;
            }

            sClass= Util.getSelectedClass(context);

            //For Time Spent
            if (reportsTimeSpentRepository.isDataExist(Util.getUserId(context), board, sClass, date, type,Util.getSelectedLanguage(context))) {
                reportsTimeSpentRepository.updateField(Util.getUserId(context), board, sClass, date, type, timeTosync,Util.getSelectedLanguage(context));
            } else {
                ReportsTimeSpentModel reportsTimeSpentModel = new ReportsTimeSpentModel();
                reportsTimeSpentModel.setUserId(Util.getUserId(context));
                reportsTimeSpentModel.setBoard(board);
                reportsTimeSpentModel.setSClass(sClass);
                reportsTimeSpentModel.setSubject(type);
                reportsTimeSpentModel.setDate(date);
                reportsTimeSpentModel.setTime(timeTosync);
                reportsTimeSpentModel.setLang(Util.getSelectedLanguage(context));
                reportsTimeSpentRepository.insertTimeSpentDetails(reportsTimeSpentModel);
            }

        }


    }
    private ReportsTimeSpentRepository reportsTimeSpentRepository;

    private Disposable timeTaskDisposable;
    private Observable<Object> getList(String userId, String board, String sClass, String subject, String date) {
        return Observable.fromCallable(() -> {
            //do something, get your Data object
            return reportsTimeSpentRepository.getDetail(userId, board, sClass, date, subject,Util.getSelectedLanguage(context));
        });

    }

    private void saveAssignedReport() {
        assignedData = getIntent().getStringExtra("assignedDate");
        assignedKey = getIntent().getStringExtra("assignedKey");
        assignmentKey = getIntent().getStringExtra("assignmentKey");
        assignmentName = getIntent().getStringExtra("assignmentName");
        teacherID = getIntent().getStringExtra("teacherID");

        batchID=global.getBatchID();
        if(assignedKey==null) return;
        int pageReadByUser = pdfView.getCurrentPage()+1;
        HashMap<String,String> report=new HashMap<>();
        report.put("pages", pageReadByUser + "/" + totalpages);
        report.put("time",String.valueOf(updatedTime + classTime));
        report.put("timestamp", Util.getCurrentDateWithDifferentFormat());
        report.put("userName",Util.getUsername(context));

        global.getDatabaseReference().child("content_assignment_batch_student").child(Util.getUserId(context)).child(batchID)
                .child(assignedData).child(assignedKey).child("report").setValue(report);

        /** add reports in teacher recent assignments */
        global.getDatabaseReference().child("content_assignment_batch_student").child(teacherID)
                .child("assignments").child(assignedKey).child("st_list").child(Util.getUserId(context)).child("report").setValue(report);


        /** add reports in assigned content info in batch node */
        if(assignmentKey ==null) global.getDatabaseReference().child("batches").child(batchID).child("assigned_content").child(assignedKey)
                .child("info").child("st_list").child(Util.getUserId(context)).child("report").setValue(report);

        else global.getDatabaseReference().child("batches").child(batchID).child("assigned_content").child(assignmentKey).child(assignmentName).child(assignedKey)
                .child("info").child("st_list").child(Util.getUserId(context)).child("report").setValue(report);


    }

}
