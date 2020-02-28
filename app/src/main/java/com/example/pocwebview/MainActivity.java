package com.example.pocwebview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity implements MainViewListener {

    public static String dirName = "SantanderEmpresas";
    private WebView webview;
    private Button button;
    public static String boletoParameter = "boleto";
    private boolean isHidden = true;
    public static String fileName = "boletin";
    private String stringButtonClickPerform = "javascript:{document.getElementById('pay-button-getnet').click()};";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String htmlCode = ("http://10.0.2.2:80/localFrame.html");

/*

        //TODO TESTAR EM FORMA DE REQUEST, VER SE CAPTURA PARAMETROS

        try {

            java.net.URL url = new URL(htmlCode);

        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("POST");

            String str =  "BODY STRING";
            byte[] outputInBytes = str.getBytes("UTF-8");
            OutputStream os = request.getOutputStream();
            os.write( outputInBytes );
            os.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        webview = findViewById(R.id.webview);

        webview.setWebViewClient(new WebViewClientImpl(this, webview));

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowContentAccess(true);

        webview.loadUrl(htmlCode);

        webview.postDelayed(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl(stringButtonClickPerform);

            }
        }, 3000);

    }

}


class WebViewClientImpl extends WebViewClient {
    private Activity activity = null;
    private WebView webView = null;
    private String boletoUrl = "";

    public WebViewClientImpl(Activity activity, WebView wv) {
        this.activity = activity;
        this.webView = wv;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        boletoUrl = url;
        if (url.contains("boleto")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Permissions.validaPermissoes(1, activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    downloadPDF(boletoUrl);
                } else {
                    Toast.makeText(activity.getApplicationContext(), "Você precisa dar permissão para o download", Toast.LENGTH_SHORT).show();
                }
            } else {

            }
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    private void downloadPDF(String boletoUrl) {
        DownloadFile downloader = new DownloadFile();
        downloader.setParameters(webView, activity);
        new DownloadFile().execute(boletoUrl, MainActivity.fileName);

    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

        if (url.contains(MainActivity.boletoParameter)) {
            Toast.makeText(activity.getApplicationContext(), url, Toast.LENGTH_LONG).show();
        } else
            super.onPageStarted(view, url, favicon);

    }


    public class DownloadFile extends AsyncTask<String, Void, Void> {

        private Activity activity = null;
        private WebView webView = null;

        void setParameters(WebView wv, Activity activity) {
            this.webView = wv;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];

            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, MainActivity.fileName);
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            webView.destroy();

            openPDF(activity);

        }
    }


    public static class FileDownloader {
        private static final int MEGABYTE = 1024 * 1024;

        public static void downloadFile(String fileUrl, File directory) {

            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void openPDF(Activity activity) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + MainActivity.dirName + "/" + MainActivity.fileName);  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            activity.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

}
