package com.example.arcfacedemo.activity;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceInfo;
import com.example.arcfacedemo.R;
import com.example.arcfacedemo.arc.ArcFaceManage;
import com.example.arcfacedemo.arc.FaceDetectInfo;
import com.example.arcfacedemo.camera.CameraHelper;
import com.example.arcfacedemo.camera.CameraListener;
import com.example.arcfacedemo.utils.ToastUtils;
import com.example.arcfacedemo.widget.DrawHelper;
import com.example.arcfacedemo.widget.DrawInfo;
import com.example.arcfacedemo.widget.FaceRectView;

import java.util.ArrayList;
import java.util.List;

public class PreviewActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener, ArcFaceManage.IOnFaceDetectResult {

    private static final String TAG = "PreviewActivity";
    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;
    private Camera.Size previewSize;
    private Integer cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
    /**
     * 相机预览显示的控件，可为SurfaceView或TextureView
     */
    private View previewView;
    private FaceRectView faceRectView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);


        previewView = findViewById(R.id.texture_preview);
        faceRectView = findViewById(R.id.face_rect_view);
        //在布局结束后才做初始化操作
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        initEngine();
        initCamera();
        if (cameraHelper != null) {
            cameraHelper.start();
        }
    }

    private void initEngine() {
        ArcFaceManage.getInstance().initVideoEngine(this, 6);
    }


    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                Log.i(TAG, "onCameraOpened: " + cameraId + "  " + displayOrientation + " " + isMirror);
                previewSize = camera.getParameters().getPreviewSize();
                drawHelper = new DrawHelper(previewSize.width, previewSize.height, previewView.getWidth(), previewView.getHeight(), displayOrientation
                        , cameraId, isMirror);
            }


            @Override
            public void onPreview(byte[] nv21, Camera camera) {
                Log.i(TAG, "onPreview: ");
                if (faceRectView != null) {
                    faceRectView.clearFaceInfo();
                }

                boolean detectResult = ArcFaceManage.getInstance().detectFaces(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21);
                if (detectResult) {
                    ArcFaceManage.getInstance().detectFaceInfo(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, PreviewActivity.this);
                }
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
                ToastUtils.showToast(PreviewActivity.this, "相机异常");
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelper != null) {
                    drawHelper.setCameraDisplayOrientation(displayOrientation);
                }
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };

        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(previewView.getMeasuredWidth(), previewView.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(cameraID != null ? cameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(previewView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
    }


    /**
     * 在{@link #previewView}第一次布局完成后，去除该监听，并且进行引擎和相机的初始化
     */
    @Override
    public void onGlobalLayout() {
        previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        initCamera();
    }


    @Override
    public void detectFaceInfos(int faceProcessCode, String msg, List<FaceDetectInfo> faceDetectInfoList) {
        if (faceRectView != null && drawHelper != null) {
            List<DrawInfo> drawInfoList = new ArrayList<>();
            List<FaceInfo> faceInfoList = ArcFaceManage.getInstance().faceInfoList;
            for (int i = 0; i < faceInfoList.size(); i++) {
                drawInfoList.add(new DrawInfo(faceInfoList.get(i).getRect(), faceDetectInfoList.get(i).getFaceGender(), faceDetectInfoList.get(i).getFaceAge(), faceDetectInfoList.get(i).getLivenessInfo().getLiveness(), null));
            }
            //绘制人脸框
            drawHelper.draw(faceRectView, drawInfoList);
        }
    }
}
