package com.Natuo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class CheckPermission {


    private AppCompatActivity MainActivity;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NOTIFICATION_POLICY};
    private List<String> mPermission = new ArrayList<>();
    private boolean mShowRequestPermission = true;

    public CheckPermission(){
        // empty constructor
    }

    public CheckPermission(AppCompatActivity MainActivity){
        this.MainActivity = MainActivity;
    }

    public void check(){
        if (Build.VERSION.SDK_INT >= 23){
            for (int i =0; i < permissions.length; i++){
                if (ContextCompat.checkSelfPermission(this.MainActivity, permissions[i]) != PackageManager.PERMISSION_GRANTED){
                    mPermission.add(permissions[i]);
                }
            }

            if (mPermission.isEmpty()){
                mShowRequestPermission = true;
            }else {
                String[] permissionArray = mPermission.toArray(new String[mPermission.size()]);
                ActivityCompat.requestPermissions(this.MainActivity, permissionArray, 101);
            }
        }
    }
}

