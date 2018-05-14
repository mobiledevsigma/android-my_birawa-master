package id.co.gsd.mybirawa.controller.sc;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import id.co.gsd.mybirawa.R;


public class ServiceWebActivity extends AppCompatActivity {

    WebView myWebView;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_services_webview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        myWebView = (WebView) findViewById(R.id.service_webview);
        pb = (ProgressBar)findViewById(R.id.pbar);
        //myWebView.loadUrl("http://180.250.242.69:8031/brooms/home/complaint/");
        pb.setVisibility(View.VISIBLE);
        myWebView.loadUrl(getIntent().getStringExtra("url"));



        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);

        myWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Do something
                Toast.makeText(ServiceWebActivity.this,description, Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                pb.setVisibility(View.GONE);
            }



            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // handle different requests for different type of files
                // this example handles downloads requests for .apk and .mp3 files
                // everything else the webview can handle normally
                //pb.setVisibility(View.VISIBLE);
                if (url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".xlsx") || url.endsWith(".xls") || url.endsWith(".docx") || url.endsWith(".doc")) {
                    Uri source = Uri.parse(url);
                    // Make a new request pointing to the .apk url
                    DownloadManager.Request request = new DownloadManager.Request(source);
                    // appears the same in Notification bar while downloading
                    request.setDescription("Downloading file");
                    String title = url.split("/")[url.split("/").length-1];
                    request.setTitle(title);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }
                    // save the file in the "Downloads" folder of SDCARD
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
                    // get download service and enqueue file
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                }
                else if(url.endsWith(".mp3")) {
                    // if the link points to an .mp3 resource do something else
                }
                // if there is a link to anything else than .apk or .mp3 load the URL in the webview
                else //pb.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.VISIBLE);
                    view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        if (myWebView.canGoBack()) {
//            myWebView.goBack();
//        } else {
//            finish();
//        }
    }
}
