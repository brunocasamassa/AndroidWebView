package com.example.pocwebview.extensions

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

inline fun <reified T:Any>  String.convertToObject(): T{

    val gson = Gson()

    return gson.fromJson(this,T::class.java)

}

class CheckoutParams(@SerializedName("amount") var amount: String? = "", @SerializedName("installments") var installments: String? = "", @SerializedName("token") var accessToken: String? = "", @SerializedName("encrypt") var encrypt: String? = "" )

