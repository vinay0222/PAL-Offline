package com.idreameducation.ipreppal.pal.activity;

import static com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity.hideNavigationBar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.TextViewHeadingFont;
import com.idreameducation.ipreppal.util.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class HowTheAppWorksPdf extends AppCompatActivity {

    PDFView pdfView;
    String pdfurl = "https://www.idreameducation.org/how_the_app_works/english/how_the_app_works.pdf";
    private final URL url = null;
    private final String fileName ="how_iprep_works_";
    private Context context;
    File file;
    private DownloadManager downloadManager;
    private ImageView imageViewBack;
    private TextViewHeadingFont titleBooks;
    private String filename,bookName,offlinePath,topicId_new,topic_name_main,bookId_new;
    private Global global;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_how_the_app_works_pdf);
        context = this;
        imageViewBack = findViewById(R.id.imageViewBack);
        titleBooks = findViewById(R.id.textViewTitle);

        filename = getIntent().getStringExtra("onlineLink");
        bookName = getIntent().getStringExtra("topicName");
        titleBooks.setText(getIntent().getStringExtra("topicName"));
        offlinePath = getIntent().getStringExtra("offlineLink");
        topicId_new = getIntent().getStringExtra("topicId");
        topic_name_main = getIntent().getStringExtra("topic_name_main");
        bookId_new = getIntent().getStringExtra("bookId");

        pdfView = findViewById(R.id.idPDFView);
        isloading=true;
        checkConnection(false);

        if(Util.isOfflineMode(context)) pdfView.fromFile(new File(Util.getSDCardPath(context) + "/.iDream_content/Books/"+offlinePath)).load();
        else new RetrivePDFfromUrl().execute(filename);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.preventTwoClick(view);
                onBackPressed();
            }
        });
        hideNavigationBar(getWindow());
    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();

            hideconnection_layout();
        }
    }

    private void openPdf(String filename) {
    final ProgressDialog pDialog = new ProgressDialog(context);
    pDialog.setTitle(context.getString(R.string.app_name));
    pDialog.setMessage("Loading...");
    pDialog.setIndeterminate(false);
    pDialog.setCancelable(false);
    WebView webView = findViewById(R.id.webview);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.clearCache(true);
    webView.setWebViewClient(new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pDialog.dismiss();
        }
    });
    String pdf = "http://www.adobe.com/devnet/acrobat/pdfs/pdf_open_parameters.pdf";
    webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + filename);
}


    private boolean isloading=true;
    ImageView imageViewCrossVideo2;
    TextView slow_internet_Text;
    Handler handler=new Handler();
    ProgressBar mProgressBar;

    private void checkConnection(boolean first) {

        mProgressBar = findViewById(R.id.progressBar);
        slow_internet_Text = findViewById(R.id.slow_internet_Text);
        imageViewCrossVideo2 = findViewById(R.id.imageViewCrossVideo2);


        if(!Util.isOfflineMode(context)) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isloading) {
                        if (first) {
                            mProgressBar.setVisibility(View.GONE);
                            slow_internet_Text.setText("We are unable to load Data\nDue to Slow Internet Connection");
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
            hideconnection_layout();
        }
    }

    private void hideconnection_layout() {
        isloading=false;
        mProgressBar.setVisibility(View.GONE);
        slow_internet_Text.setVisibility(View.GONE);
        imageViewCrossVideo2.setVisibility(View.GONE);
    }


    @Override
    protected void onPause() {

        Util.preventPause(context,getTaskId());
        super.onPause();
    }


}