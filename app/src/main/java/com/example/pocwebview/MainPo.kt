package com.example.pocwebview

import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.app.Service
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast

import org.w3c.dom.Text

import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.net.URLEncoder
import java.util.Calendar
import java.util.Date

import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.JsonWriter
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.*

class MainPo : AppCompatActivity(), MainViewListener {

    private var webview: WebView? = null
    private val button: Button? = null
    private val isHidden = true
    private val stringButtonClickPerform = "javascript:{document.getElementById('pay-button-getnet').click()};"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentTime = Calendar.getInstance().time


        // String htmlCode = ("http://10.0.2.2:80/localFrame.html");

        // HttpURLConnection connection = new HttpURLConnection();
        //Uri.parse("https://acabreu.websiteseguro.com/alexlab/_testes/iframe/teste_getnet.php");

        webview = findViewById(R.id.webview)


        //Toast.makeText(applicationContext, permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).toString(),Toast.LENGTH_LONG).show()

      webview!!.webViewClient = WebViewClientImpl(this, webview!!)

        val settings = webview!!.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowContentAccess = true
        settings.defaultTextEncodingName = "utf-8"

        var htmlCode = "https://acabreu.websiteseguro.com/alexlab/getnet/getnet_estatico.php"

        //post
        htmlCode = "http://10.0.2.2:8082/generate-html"


        var jsonInput = CheckoutParams("80.00", "12", "Bearer 3c6e14de-8786-47e3-9ccd-733e2d50d66c", "eyJlbmMiOiJBMTI4Q0JDLUhTMjU2IiwiYWxnIjoiUlNBLU9BRVAiLCJraWQiOiJnZXRuZXQta2V5LWNoZWNrb3V0In0.ZhzNTQVX4QJq9TJEhsH_O4fQjtdRDEnQlS-Q-uyibm0ZejqHxOBeAa2-GeLpXLGj8of607ncKyJWCNFacFOauwxh0WMOlA0d6OT4Cf2Da4YdHyLEQQJOugoSiFR1aBQqKEeyhJfre6xA_tUI2jSk9Vw_8Fxsw-X9_5qwWFSHvYsM_tT-moIL1J7Hg-8b2GxL4CxlQozrcoRm0-DlOI48urZlp4ltU2OBNY77e2PZJW54ngKvw2KBrHcxbb0CKEpoi3qWrmNxFBsPr1YQtil8iwAdVmcMZvb3t3grZ_acb5ZpW2ES6dn9er1DfTyg_3pFukno-igZ8Hf9g97Zgs8TCQ.7n5W2d7nXLG7_tKcVRG0yQ.lQy0irTizwWjP5KGDOrlWMj2OkjMAV110YAF5KYPqLS6xPOmPsMZqXGXirZk_JYzXtuNZjciLT0FxvuQOHjriadiX_zkNIqEKCDJumgCr0bcoO7T74R3VwHmW3dMBV9Ph8yEmRfwM8V47-MVsv1OQRhtWrsOTWS0mjkU2qUrmsSsxYK4SUs96Tot4bwBMjYsba7m0O0E3YjJ6wuPUqSg6b35_ym8wU-fdo5sCcaom6seRW_uhV89B2GAQrcwsHA-hgUBhWfLnYc8Gd6RrUQRiTE2ECPEMf44EtFGE5CWhDAId7IBct0pJ7C_ZG9WdpUs9c3vzWPgGIsY6p9iPBeVW7y5P6Us3X8k7cWq4OSwvxyKU1CzYD0bfrsEvqOhZq8HuEz6LQox-ZRndEcqLKsPkbKKE3wemhGdSRfurJgu2227-YuzbSHnmjT4mazEQr-pYwVUpfF4TH3FO8wMsyiiWW63RxSYVKWo7CTB7B2VTU3bm-_-hPaDtXEK_30VpoacbA-nYLP92Pkt6I9ijyEbcdCNqhujJ4pBd05ESB5pCJbsng01NhZyApY3boj3Mwz6NRefa5NeNEF3MX-gy716Bz4jjoupZyvwVe57epCSQXA1_UQkfDBC48ng4JqTivpzSGghpgHKNj7o1nVYihisthQkV3Vzr5Dp52UK-z0OfSbhVDO7iZxbfxpeYbTZFKuvtXP_TSpBeD12xr86QQ4B1bTBn1wHnAj9EOvR8masLcphhIY65z3_ZplYY0QSB3HdKNxlz1zvZaN1fcwuCagdzTO2nizecuBgFUalIrJgVVfzxKmn2AUpSlykxE5Vocd6owG8As6TRcBYOuwgfcilxQ.wD-NppjGJ-y3PgOcaOmDwA")

        var gson = Gson()

        var input = gson.toJson(jsonInput)

       // webview!!.postUrl(htmlCode, input.toByteArray())

        CustomButton.setOnClickListener {
          // webview!!.postUrl(htmlCode, input.toByteArray())

            webview!!.postURLHarder(htmlCode,input.toByteArray())
            permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)


        }


    }
    inline fun <reified T:Any>  String.convertToObject(): T{

        val gson = Gson()

        return gson.fromJson(this,T::class.java)

    }

    class CheckoutParams(@SerializedName("amount") var amount: String? = "", @SerializedName("installments") var installments: String? = "", @SerializedName("token") var accessToken: String? = "", @SerializedName("encrypt") var encrypt: String? = "" )

}

internal class WebViewClientImpl(activity: Activity, wv: WebView) : WebViewClient() {
    private var activity: Activity? = null
    private var webView: WebView? = null
    private var boletoUrl = ""

    init {
        this.activity = activity
        this.webView = wv
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        boletoUrl = url
        if (url.contains("boleto")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Permissions.validaPermissoes(1, activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                } else {
                    // Permissions.validaPermissoes(1,activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    Toast.makeText(activity!!.applicationContext, "Você precisa dar permissão para o download", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.shouldOverrideUrlLoading(view, url)
    }

    override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
/*
        if (url?.contains(MainActivity.boletoParameter)) {
            view?.loadData(url, "text/html", "utf-8")
        } else*/
            super.onPageStarted(view, url, favicon)

    }



    fun openPDF(activity: Activity) {

        val pdfFile = File(Environment.getExternalStorageDirectory().toString() + "/" + MainActivity.dirName + "/" + MainActivity.fileName)
        val path = Uri.fromFile(pdfFile)
        val pdfIntent = Intent(Intent.ACTION_VIEW)
        pdfIntent.setDataAndType(path, "application/pdf")
        pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        try {
            activity.startActivity(pdfIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "No Application available to view PDF", Toast.LENGTH_SHORT).show()
        }

    }


}

data class Input(@SerializedName("token") var _val: String?, @SerializedName("amount") var amount: String?) : Object() {

    constructor() : this(_val = "Bearer 82d94f65-a187-43a4-b15a-073e2c5b5ee4", amount = "30.00")
}


internal fun Activity.permissions(permissionToValidate: String, recursiveCall: (() -> Boolean) ?= null)  : Boolean {
    val permission = ContextCompat.checkSelfPermission(this,
            permissionToValidate)


    //todo test
    if (permission != PackageManager.PERMISSION_GRANTED) {
        if (recursiveCall != null) {
            if(recursiveCall()){
                return false
            }
        }

        ActivityCompat.requestPermissions(this,
                arrayOf(permissionToValidate),
                110)
        return this.permissions(permissionToValidate) { true }


    }

    return true


}



