package com.example.pocwebview;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {


    public static boolean validaPermissoes(int requestCode, Activity activity, String permissao) {
        String[] permission = {permissao};

        if (Build.VERSION.SDK_INT >= 23) {

                Boolean permissionValidated = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;

                System.out.println("PERMISSAO " + permissao + " " + permissionValidated);

            if (permissionValidated) return true;

            else {

                /*Solicita Permissao*/
                ActivityCompat.requestPermissions(activity, permission, requestCode);

                return false ; //validaPermissoes(requestCode,activity,permissao);
            }

        }

        return false;
    }

}
