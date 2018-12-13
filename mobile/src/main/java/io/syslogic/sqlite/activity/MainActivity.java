package io.syslogic.sqlite.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;

import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import io.syslogic.sqlite.BuildConfig;
import io.syslogic.sqlite.R;
import io.syslogic.sqlite.database.SqliteBaseHelper;
import io.syslogic.sqlite.interfaces.ILogReceiver;

public class MainActivity extends AppCompatActivity implements ILogReceiver {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static int sequence = 0;

    private WebView mLogView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_main);
        this.setWebView(R.id.sqlite_logs);
    }

    @Override
    public void onMessage(final String message) {
        Log.d(LOG_TAG, message);
        if(this.mLogView != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    evalScript(mLogView, getJavaScript(MainActivity.sequence, message));
                    MainActivity.sequence ++;
                }
            });
        }
    }

    @Override
    public
    void onException(final Exception e) {
        Log.d(LOG_TAG, e.getMessage());
        if(this.mLogView != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    evalScript(mLogView, getJavaScript(MainActivity.sequence, e.getMessage()));
                    MainActivity.sequence ++;
                }
            });
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void setWebView(@IdRes int resId) {

        /* creating a WebView with JavaScript enabled */
        this.mLogView = this.findViewById(resId);
        this.mLogView.getSettings().setAllowFileAccessFromFileURLs(true);
        this.mLogView.getSettings().setJavaScriptEnabled(true);

        String html = "<html><head>" + this.getStyles() + "</head><body><ul id=\"logs\" class=\"logs\"></ul></body><html>";
        this.mLogView.loadDataWithBaseURL("file:///android_asset/", html, "text/HTML", "UTF-8", null);

        this.mLogView.setWebViewClient(new WebViewClient() {

            // deprecated method, kept for compatibility.
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if(BuildConfig.DEBUG) {Log.w(LOG_TAG, description);}
            }

            // redirects to the deprecated method above.
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }

            /** in debug mode, the handler shall proceed on SSL certificate errors */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if(BuildConfig.DEBUG) {handler.proceed();}
                else {Log.w(LOG_TAG, error.toString());}
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                /* when deleting the records, it works */
                performSqliteTest( true,0);
                performSqliteTest( true,1);

                /* when not deleting the records, it accumulates the ROWID */
                // performSqliteTest(false,0);
                // performSqliteTest(false,1);
            }
        });
    }

    private void performSqliteTest(boolean deleteRecords, int methodId) {

        SqliteBaseHelper db = SqliteBaseHelper.getInstance(this, this);

        db.insertSampleRecords(db.getWritableDatabase(),100);
        db.getAutoIncrement(db.getWritableDatabase(), SqliteBaseHelper.TABLE_ATTACHMENTS);

        db.resetAutoIncrement(db.getWritableDatabase(), SqliteBaseHelper.TABLE_ATTACHMENTS, deleteRecords, methodId);
        db.getAutoIncrement(db.getWritableDatabase(), SqliteBaseHelper.TABLE_ATTACHMENTS);

        db.insertSampleRecords(db.getWritableDatabase(),50);
        db.getAutoIncrement(db.getWritableDatabase(), SqliteBaseHelper.TABLE_ATTACHMENTS);

        db.resetAutoIncrement(db.getWritableDatabase(), SqliteBaseHelper.TABLE_ATTACHMENTS, deleteRecords, methodId);
        db.getAutoIncrement(db.getWritableDatabase(), SqliteBaseHelper.TABLE_ATTACHMENTS);

        db.insertSampleRecords(db.getWritableDatabase(),25);
        db.getAutoIncrement(db.getWritableDatabase(), SqliteBaseHelper.TABLE_ATTACHMENTS);

        db.resetAutoIncrement(db.getWritableDatabase(), SqliteBaseHelper.TABLE_ATTACHMENTS, deleteRecords, methodId);
        db.getAutoIncrement(db.getWritableDatabase(), SqliteBaseHelper.TABLE_ATTACHMENTS);
    }

    /** CSS Generation */
    protected String getStyles() {
        return "<style type=\"text/css\">" +
            "@font-face {font-family: 'DroidSansMono'; src: url('file://android_asset/droidsansmono.ttf');}" +
            "body {font-family: 'DroidSansMono'; font-size: 0.8em; font-weight: 400; white-space: nowrap;}" +
            "ul.logs {" +
            "list-style-type: none;" +
            "background-color: #FCFCFC;" +
            "height: 100%;" +
            "padding: 0;" +
            "margin: 0;" +
            "}" +
            "ul.logs li {" +
            "background-color: #FCFCFC;" +
            "padding: 2px;" +
            "width: 100%;" +
            "}"+
            "</style>";
    }

    /** returns an auto-invoking JS function */
    protected String getJavaScript(int sequence, String message) {

        String script = "(function(){" +
            "var ul = document.getElementById(\"logs\");" +
            "var li = document.createElement(\"li\");" +
            "li.id = \"log_entry_" + String.valueOf(sequence) + "\";" +
            "li.innerHTML = \'"  + message + "\'; ul.appendChild(li);})();";

        Log.i("chromium", script);
        return script;
    }

    /** Script Execution Wrapper */
    protected void evalScript(WebView webview, String script) {
        webview.loadUrl("javascript:" + script);
    }
}
