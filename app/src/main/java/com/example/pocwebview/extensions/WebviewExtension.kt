package com.example.pocwebview.extensions

import android.content.Context
import android.webkit.WebView
import android.R.string
import android.icu.util.ULocale.getLanguage
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.telecom.Call
import android.util.AttributeSet
import android.util.Log
import android.webkit.CookieManager
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.util.*


inline fun WebView.postURLHarder(url: String, postData: ByteArray) {

    val webView = this


    val request = Request.Builder()
            .url(url)
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), postData))
            .build()


    /*starting response capture*/
    OkHttpClient().newCall(request).enqueue(object : Callback {
        override fun onResponse(call: okhttp3.Call, response: Response) {

            val htmlResponse = response.body!!.string()

            /*rendering html response on webview*/
            with(webView) {
                post{
                    Log.d("HTML_STRING", htmlResponse)
                    this.clearCache(true)
                    this.loadData(htmlResponse, "text/html", "utf-8")
                }
            }

        }

        override fun onFailure(call: okhttp3.Call, e: IOException) {
            Log.e("CHAMAAA", call.isExecuted().toString() + call.isCanceled().toString() + e.printStackTrace().toString())

        }

    })
}


