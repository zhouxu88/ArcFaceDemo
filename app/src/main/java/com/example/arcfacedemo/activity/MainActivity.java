package com.example.arcfacedemo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.example.arcfacedemo.Constants;
import com.example.arcfacedemo.R;
import com.example.arcfacedemo.faceserver.FaceServer;
import com.example.arcfacedemo.utils.SharedPrefUtils;


/**
 * 虹软人脸识别识别（离线SDK）
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    private Context mContext = this;


    private FaceEngine mFaceEngine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

    }


    /**
     * 处理单张图片，显示图片中所有人脸的信息，并且一一比对相似度
     *
     * @param view
     */
    public void jumpToSingleImageActivity(View view) {
        startActivity(new Intent(this, SingleImageActivity.class));
    }


    /**
     * 选择一张主照，显示主照中人脸的详细信息，然后选择图片和主照进行比对
     *
     * @param view
     */
    public void jumpToMultiImageActivity(View view) {
        startActivity(new Intent(this, MultiImageActivity.class));
    }


    /**
     * 打开相机，显示年龄性别
     *
     * @param view
     */
    public void jumpToPreviewActivity(View view) {
        startActivity(new Intent(this, PreviewActivity.class));
    }

    /**
     * 打开相机，人脸注册，人脸识别
     *
     * @param view
     */
    public void jumpToFaceRecognizeActivity(View view) {
        startActivity(new Intent(this, RegisterAndRecognizeActivity.class));
    }


    /**
     * 人脸注册
     *
     * @param view
     */
    public void jumpToRegisterActivity(View view) {
//        startActivity(new Intent(this, RegisterActivity.class));
        FaceServer.getInstance().clearAllFaces(this);
    }


    private void requestPermission() {
        boolean allGranted = true;
        for (String needPermission : NEEDED_PERMISSIONS) {
            allGranted &= ContextCompat.checkSelfPermission(this, needPermission) == PackageManager.PERMISSION_GRANTED;
        }
        if (!allGranted) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult size : " + permissions.length + grantResults.length);
        if (requestCode == 2 && permissions.length == 4) {
            init();
        }
    }

    /**
     * 1、初始化
     */
    private void init() {
        mFaceEngine = new FaceEngine();
        boolean isActive = SharedPrefUtils.getBoolean(mContext, Constants.IS_ACTIVE, false);
        if (!isActive) {
            int activeCode = mFaceEngine.active(this,
                    Constants.APP_ID,
                    Constants.SDK_KEY);
            if (activeCode == ErrorInfo.MOK) {
                Log.i(TAG, "人脸引擎激活成功");
                SharedPrefUtils.writeBoolean(mContext, Constants.IS_ACTIVE, true);
            } else {
                Log.i(TAG, "人脸引擎激活失败 " + activeCode);
            }
        }


//        int engineCode = mFaceEngine.init(this,
//                FaceEngine.ASF_DETECT_MODE_VIDEO,
//                FaceEngine.ASF_OP_270_ONLY,
//                16,
//                1,
//                FaceEngine.ASF_FACE_RECOGNITION |
//                        FaceEngine.ASF_FACE_DETECT);
//        if (engineCode == ErrorInfo.MOK) {
//            Log.i(TAG, "人脸引擎初始化成功");
//        } else {
//            Log.i(TAG, "人脸引擎初始化失败 " + engineCode);
//        }
    }


}
