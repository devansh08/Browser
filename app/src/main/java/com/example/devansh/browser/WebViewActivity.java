package com.example.devansh.browser;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    public static final int ID_SAVE = 10;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();

        String url = intent.getStringExtra(InputActivity.EXTRA_URL);

        Log.d("registerContextMenu", "Registering ContextMenu...");
        //registerForContextMenu(webView);
        Log.d("registerContextMenu", "ContextMenu Registered");

        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(url);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setDescription("Download file...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        URLUtil.guessFileName(url, contentDisposition, mimetype));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
            }
        });

        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d("onCreateContextMenu", "Entering onCreate()...");
        super.onCreateContextMenu(menu, v, menuInfo);

        Log.d("onCreateContextMenu", "Initializing HitResult...");
        HitTestResult result = webView.getHitTestResult();
        Log.d("onCreateContextMenu", "HitResult Initialized");

        MenuItem.OnMenuItemClickListener handler = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("onCreateContextMenu", "Entering MenuItemListener...");
                Toast toast = Toast.makeText(getApplicationContext(), "MenuItem clicked !!!!",
                        Toast.LENGTH_LONG);
                toast.show();
                Log.d("onCreateContextMenu", "Leaving MenuItemListener...");
                return true;
            }
        };

        if(result.getType() == HitTestResult.IMAGE_TYPE ||
                result.getType() == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            Log.d("onCreateContextMenu", "Image Detected");
            menu.setHeaderTitle(result.getExtra());
            menu.add(0, ID_SAVE, 0, "Save Image").setOnMenuItemClickListener(handler);
            Log.d("onCreateContextMenu", "Image Saved");
        }
        Log.d("onCreateContextMenu", "Leaving onCreate()...");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, keyEvent);
    }
}
