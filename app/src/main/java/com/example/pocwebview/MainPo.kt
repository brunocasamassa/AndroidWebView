package com.example.pocwebview

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast

import java.util.Calendar

import androidx.core.app.ActivityCompat
import com.example.pocwebview.extensions.CheckoutParams
import com.example.pocwebview.extensions.permissions
import com.example.pocwebview.extensions.postURLHarder
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_main.*
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

        webview = findViewById(R.id.webview)

        webview!!.webViewClient = WebViewClientImpl(this, webview!!)

        val settings = webview!!.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowContentAccess = true
        settings.defaultTextEncodingName = "utf-8"

        var htmlCode = "https://acabreu.websiteseguro.com/alexlab/getnet/getnet_estatico.php"

        //post
        htmlCode = "http://10.0.2.2:8082/generate-html"

        var jsonInput = CheckoutParams("80.00", "12", "Bearer 3c6e14de-8786-47e3-9ccd-733e2d50d66c")

        var gson = Gson()

        var input = gson.toJson(jsonInput)

        // webview!!.postUrl(htmlCode, input.toByteArray())

        CustomButton.setOnClickListener {
            // webview!!.postUrl(htmlCode, input.toByteArray())

            webview!!.postURLHarder(htmlCode, input.toByteArray())
            permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)


        }


    }


}



