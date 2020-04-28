package com.example.pocwebview.extensions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

internal fun Activity.permissions(permissionToValidate: String, recursiveCall: (() -> Boolean)? = null): Boolean {
    val permission = ContextCompat.checkSelfPermission(this,
            permissionToValidate)


    //todo test
    if (permission != PackageManager.PERMISSION_GRANTED) {
        if (recursiveCall != null) {
            if (recursiveCall()) {
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