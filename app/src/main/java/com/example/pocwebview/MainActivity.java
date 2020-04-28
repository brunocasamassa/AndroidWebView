package com.example.pocwebview;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

@Deprecated
public class MainActivity extends AppCompatActivity implements MainViewListener {

    public static String dirName = "SantanderEmpresas";
    private WebView webview;
    private Button button;
    public static String boletoParameter = "boleto";
    private boolean isHidden = true;
    public static String fileName = "boletin";
    private String stringButtonClickPerform = "javascript:{document.getElementById('pay-button-getnet').click()};";

    @SuppressLint("JavascriptInterface")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date currentTime = Calendar.getInstance().getTime();

        fileName = "boleto_" + currentTime.toString().replace(" ", "_");

        // String htmlCode = ("http://10.0.2.2:80/localFrame.html");

        // HttpURLConnection connection = new HttpURLConnection();
        //Uri.parse("https://acabreu.websiteseguro.com/alexlab/_testes/iframe/teste_getnet.php");

        webview = findViewById(R.id.webview);

      //  webview.setWebChromeClient(new WebChromeClient());


        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowContentAccess(true);
        /*webview.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");


                IframeAssync iframeAssync = new IframeAssync();

        // iframeAssync.execute();

        webview.evaluateJavascript(
                "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        Log.d("HTML", html);
                        // code here
                    }
                });*/


        String htmlCode = "https://acabreu.websiteseguro.com/alexlab/getnet/getnet_estatico.php";

        htmlCode = ("http://10.0.2.2:8082/generate-html");

//        htmlCode = "https://acabreu.websiteseguro.com/alexlab/getnet/teste_getnet.php";

        String jsonInputString = "{token: Bearer 319587bf-dc30-4da8-a9ac-54f7ce7f9b55}";

        // jsonInputString = "{token: Bearer 183f42ce-3dca-41ae-aa8c-2e9f4a3abe88, amount: 358.90, encrypt: eyJlbmMiOiJBMTI4Q0JDLUhTMjU2IiwiYWxnIjoiUlNBLU9BRVAiLCJraWQiOiJnZXRuZXQta2V5LWNoZWNrb3V0In0.S8ioIFKYzEAC1HViY3MrlKI8lcrEIvMdIfm8mcN3EaL2pTPsIJdRNJZaQ-kp3hTV0Fb5ERZjt0lgButl5xJsi25fMZi968i69dc0ZQYpLEaV2eWpiXG111lnOVjTEqsK3z3BiUviDI14V1N9suuygOKwo_PKKLtb4GG9kXP5zy7Y4ZYdRqRb6uwkTYRWazGdrRQB0MiDcRAmfoP7_FI1edywzr-GJUe22KXLgm1xhRfslL2xtbhrSP0uP8N6OUJ7hEJr2pMQLhLXpSF3sXkWP35S9CtqA15Ty2WwUvE8tcKo2YGpT9ci48dAlWwLrEzoeS2ePSes5By7_yszuEPa0w.eaxo_r27qSBxED7sY2Chtg.rnzZDhv8X7qi07RFPgcHRjekM4mDMDBJFkJjZPucrOzcPSRUDn4RyXx56MKXUyyBuS3FlETZsroWxbbgaoUSdYl6BuPWi9QGm5m9WY0SzfPZU_HUNKDbLdlSBHRecvDAWoacgd0-x6y0aiVH3nTzkllHeOJ12G3KW1I1KntX5QIp0a0gnnkhbzh4H1NxFm-6t8lGzA4waDtqm87_iOJgDx3cqTmv2Ud0gfNT6jECqsN0a1Q7qK5HDrN7QjKaJpfiJ5Rf75brYHpYcaRDhaENPsKyA1FD-lJ2xCApfhqFE4s5edZMLaZ0RZsN1fY3840Yi98PgKtnEA5Wm-_xFvN17X87MWRT_0rj9_dZ4fWqQUERMGKNU8EPIc54joBA9x1bJLQTZfIftygYpfGkLFb7ujr8uT8QcDy7eRkzHwES7z9PxK6jxIVWnWTyxUhXGbKmCk1kY-juxp1T5pYdqs0iMtwOypocnpqPRxi7jbbArEhysZFiwT5xGgDTNvAGWR3NtBzE4UXFesrsPzv1Kt0_v42hAYP0vGcpoLohHK5P93AU-PBVjmQfbzd-bb5DdWkh9EgYYrLsFGvg5TOk636M-5MHjKvoTcgXb5EUyI-zFH0TirnCBSaoVkX6XNktKtkvhMscGKc16sBJL__Y-I-F32MJSoT7WJzZsoHRTEKSShj1j9CqbJF4bh8okLfwm3RpCVEyWCyy9uuQtsp48t5zmLbaZuA0sIwHxiWzfvImAw-6-wT2xfZmh-Ho-3yfaJ_6EF60CPtXXtFnQGmc3pAgEEbz5nq_qKbXAcaT8U9quMw.K7vtHfraYD4WswGE88XZqQ}";

        String postData = null;

        try {
            postData = "val=" + URLEncoder.encode("mobile", "UTF-8");
           // webview.post(htmlCode, postData.getBytes());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

   /*

        try {
            postData = "token=" + URLEncoder.encode("Bearer 0106eb87-4b03-4387-82cf-5495a0024af8", "UTF-8") + "&amount=" + URLEncoder.encode("358.80", "UTF-8") + "&encrypt=" + URLEncoder.encode("eyJlbmMiOiJBMTI4Q0JDLUhTMjU2IiwiYWxnIjoiUlNBLU9BRVAiLCJraWQiOiJnZXRuZXQta2V5LWNoZWNrb3V0In0.S8ioIFKYzEAC1HViY3MrlKI8lcrEIvMdIfm8mcN3EaL2pTPsIJdRNJZaQ-kp3hTV0Fb5ERZjt0lgButl5xJsi25fMZi968i69dc0ZQYpLEaV2eWpiXG111lnOVjTEqsK3z3BiUviDI14V1N9suuygOKwo_PKKLtb4GG9kXP5zy7Y4ZYdRqRb6uwkTYRWazGdrRQB0MiDcRAmfoP7_FI1edywzr-GJUe22KXLgm1xhRfslL2xtbhrSP0uP8N6OUJ7hEJr2pMQLhLXpSF3sXkWP35S9CtqA15Ty2WwUvE8tcKo2YGpT9ci48dAlWwLrEzoeS2ePSes5By7_yszuEPa0w.eaxo_r27qSBxED7sY2Chtg.rnzZDhv8X7qi07RFPgcHRjekM4mDMDBJFkJjZPucrOzcPSRUDn4RyXx56MKXUyyBuS3FlETZsroWxbbgaoUSdYl6BuPWi9QGm5m9WY0SzfPZU_HUNKDbLdlSBHRecvDAWoacgd0-x6y0aiVH3nTzkllHeOJ12G3KW1I1KntX5QIp0a0gnnkhbzh4H1NxFm-6t8lGzA4waDtqm87_iOJgDx3cqTmv2Ud0gfNT6jECqsN0a1Q7qK5HDrN7QjKaJpfiJ5Rf75brYHpYcaRDhaENPsKyA1FD-lJ2xCApfhqFE4s5edZMLaZ0RZsN1fY3840Yi98PgKtnEA5Wm-_xFvN17X87MWRT_0rj9_dZ4fWqQUERMGKNU8EPIc54joBA9x1bJLQTZfIftygYpfGkLFb7ujr8uT8QcDy7eRkzHwES7z9PxK6jxIVWnWTyxUhXGbKmCk1kY-juxp1T5pYdqs0iMtwOypocnpqPRxi7jbbArEhysZFiwT5xGgDTNvAGWR3NtBzE4UXFesrsPzv1Kt0_v42hAYP0vGcpoLohHK5P93AU-PBVjmQfbzd-bb5DdWkh9EgYYrLsFGvg5TOk636M-5MHjKvoTcgXb5EUyI-zFH0TirnCBSaoVkX6XNktKtkvhMscGKc16sBJL__Y-I-F32MJSoT7WJzZsoHRTEKSShj1j9CqbJF4bh8okLfwm3RpCVEyWCyy9uuQtsp48t5zmLbaZuA0sIwHxiWzfvImAw-6-wT2xfZmh-Ho-3yfaJ_6EF60CPtXXtFnQGmc3pAgEEbz5nq_qKbXAcaT8U9quMw.K7vtHfraYD4WswGE88XZqQ", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        */


        // webview.loadUrl("");//,postData.getBytes());

        //webview.loadData(postData, "text/html", "UTF-8");

/*        webview.postDelayed(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl(stringButtonClickPerform);

            }
        }, 3000);*/

    }


    class IframeAssync extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            try {
                String htmlCode = ("https://acabreu.websiteseguro.com/alexlab/_testes/iframe/teste_getnet.php");

                URL imageUrl = new URL(htmlCode);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setReadTimeout(2000);
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                String jsonInputString = "{token: Bearer 0106eb87-4b03-4387-82cf-5495a0024af8, amount: 358.90, encrypt: eyJlbmMiOiJBMTI4Q0JDLUhTMjU2IiwiYWxnIjoiUlNBLU9BRVAiLCJraWQiOiJnZXRuZXQta2V5LWNoZWNrb3V0In0.S8ioIFKYzEAC1HViY3MrlKI8lcrEIvMdIfm8mcN3EaL2pTPsIJdRNJZaQ-kp3hTV0Fb5ERZjt0lgButl5xJsi25fMZi968i69dc0ZQYpLEaV2eWpiXG111lnOVjTEqsK3z3BiUviDI14V1N9suuygOKwo_PKKLtb4GG9kXP5zy7Y4ZYdRqRb6uwkTYRWazGdrRQB0MiDcRAmfoP7_FI1edywzr-GJUe22KXLgm1xhRfslL2xtbhrSP0uP8N6OUJ7hEJr2pMQLhLXpSF3sXkWP35S9CtqA15Ty2WwUvE8tcKo2YGpT9ci48dAlWwLrEzoeS2ePSes5By7_yszuEPa0w.eaxo_r27qSBxED7sY2Chtg.rnzZDhv8X7qi07RFPgcHRjekM4mDMDBJFkJjZPucrOzcPSRUDn4RyXx56MKXUyyBuS3FlETZsroWxbbgaoUSdYl6BuPWi9QGm5m9WY0SzfPZU_HUNKDbLdlSBHRecvDAWoacgd0-x6y0aiVH3nTzkllHeOJ12G3KW1I1KntX5QIp0a0gnnkhbzh4H1NxFm-6t8lGzA4waDtqm87_iOJgDx3cqTmv2Ud0gfNT6jECqsN0a1Q7qK5HDrN7QjKaJpfiJ5Rf75brYHpYcaRDhaENPsKyA1FD-lJ2xCApfhqFE4s5edZMLaZ0RZsN1fY3840Yi98PgKtnEA5Wm-_xFvN17X87MWRT_0rj9_dZ4fWqQUERMGKNU8EPIc54joBA9x1bJLQTZfIftygYpfGkLFb7ujr8uT8QcDy7eRkzHwES7z9PxK6jxIVWnWTyxUhXGbKmCk1kY-juxp1T5pYdqs0iMtwOypocnpqPRxi7jbbArEhysZFiwT5xGgDTNvAGWR3NtBzE4UXFesrsPzv1Kt0_v42hAYP0vGcpoLohHK5P93AU-PBVjmQfbzd-bb5DdWkh9EgYYrLsFGvg5TOk636M-5MHjKvoTcgXb5EUyI-zFH0TirnCBSaoVkX6XNktKtkvhMscGKc16sBJL__Y-I-F32MJSoT7WJzZsoHRTEKSShj1j9CqbJF4bh8okLfwm3RpCVEyWCyy9uuQtsp48t5zmLbaZuA0sIwHxiWzfvImAw-6-wT2xfZmh-Ho-3yfaJ_6EF60CPtXXtFnQGmc3pAgEEbz5nq_qKbXAcaT8U9quMw.K7vtHfraYD4WswGE88XZqQ}";

                webview.postUrl(htmlCode, jsonInputString.getBytes());
//
//                try (OutputStream os = conn.getOutputStream()) {
//                    byte[] input = jsonInputString.getBytes("utf-8");
//                    os.write(input, 0, input.length);
//                }
//
//                conn.connect();
//
//
//                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    InputStream inputStream = new BufferedInputStream(conn.getInputStream());
//
//                    return getStringFromInputStream(inputStream);
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String html) {
            super.onPostExecute(html);
            String htmlCode = ("https://acabreu.websiteseguro.com/alexlab/_testes/iframe/teste_getnet.php");

            // webview.loadDataWithBaseURL(htmlCode,html,"text/html; charset=utf-8", "UTF-8");
        }
    }

    public static String getStringFromInputStream(InputStream inputStream) {

        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

}

 class WebChromeCLientImpl extends WebChromeClient {

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

        Log.d("WEBCHROME_LOG", consoleMessage.message());

        return super.onConsoleMessage(consoleMessage);

    }
}



class MyJavaScriptInterface {

    private Context ctx;

    MyJavaScriptInterface(Context ctx) {
        this.ctx = ctx;
    }

    public void showHTML(String html) {
        Log.d("HTML_LOG", html);
        new AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
                .setPositiveButton(android.R.string.ok, null).setCancelable(false).create().show();
    }

}

class WebViewClientImp extends WebViewClient {
    private Activity activity = null;
    private WebView webView = null;
    private String boletoUrl = "";

    public WebViewClientImp(Activity activity, WebView wv) {
        this.activity = activity;
        this.webView = wv;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        boletoUrl = url;
        if (url.contains("boleto")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Permissions.validaPermissoes(1, activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    downloadPDF(boletoUrl);
                } else {
                    // Permissions.validaPermissoes(1,activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    Toast.makeText(activity.getApplicationContext(), "Você precisa dar permissão para o download", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    private void downloadPDF(String boletoUrl) {

        DownloadFile downloader = new DownloadFile();
        downloader.setParameters(webView, activity);
        downloader.execute(boletoUrl, MainActivity.fileName);

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
            File folder = new File(extStorageDirectory, MainActivity.dirName);
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

                urlConnection.connect();

                InputStream inputStream = new BufferedInputStream(url.openStream());

                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                // int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();
                inputStream.close();

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

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + MainActivity.dirName + "/" + MainActivity.fileName);
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
