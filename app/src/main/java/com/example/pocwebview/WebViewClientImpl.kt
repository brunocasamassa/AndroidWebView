package com.example.pocwebview

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import java.io.File

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
