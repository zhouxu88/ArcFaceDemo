package com.example.arcfacedemo.arc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.example.arcfacedemo.Constants;
import com.example.arcfacedemo.utils.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 周旭
 * @company 义金健康
 * @e-mail 374952705@qq.com
 * @time 2019/2/14
 * @descripe
 */


public class ArcFaceManage {

    private static final String TAG = "ArcFaceManage";
    /**
     * 最大检测人数
     */
    private static final int detectFaceMaxNum = 5;
    private static ArcFaceManage instance;
    public FaceEngine mFaceEngine;
    public List<FaceInfo> faceInfoList = new ArrayList<>();

    public interface IOnFaceDetectResult {
        /**
         * 检测人脸性别，年龄等结果
         *
         * @param faceProcessCode
         * @param faceDetectInfoList
         */
        void detectFaceInfos(int faceProcessCode, String msg, List<FaceDetectInfo> faceDetectInfoList);
    }

    public static ArcFaceManage getInstance() {
        if (instance == null) {
            instance = new ArcFaceManage();
        }
        return instance;
    }

    /**
     * Step1：调用FaceEngine的active方法激活设备，一个设备安装后仅需激活一次，卸载重新安装后需要重新激活。
     *
     * @param mContext
     */
    public boolean active(Context mContext) {
        boolean isActive = SharedPrefUtils.getBoolean(mContext, Constants.IS_ACTIVE, false);
        if (!isActive) {
            mFaceEngine = new FaceEngine();
            int activeCode = mFaceEngine.active(mContext,
                    Constants.APP_ID,
                    Constants.SDK_KEY);
            if (activeCode == ErrorInfo.MOK) {
                Log.i(TAG, "人脸引擎激活成功");
                SharedPrefUtils.writeBoolean(mContext, Constants.IS_ACTIVE, true);
            } else {
                Log.i(TAG, "人脸引擎激活失败 " + activeCode);
            }
            return activeCode == ErrorInfo.MOK;

        } else {
            return true;
        }
    }


    /**
     * Step2：
     *
     * @param mContext
     * @return
     */
    public boolean init(Context mContext) {
        mFaceEngine = new FaceEngine();
        int engineCode = mFaceEngine.init(mContext,
                FaceEngine.ASF_DETECT_MODE_VIDEO,
                FaceEngine.ASF_OP_270_ONLY,
                16,
                1,
                FaceEngine.ASF_FACE_RECOGNITION |
                        FaceEngine.ASF_FACE_DETECT);
        if (engineCode == ErrorInfo.MOK) {
            Log.i(TAG, "人脸引擎初始化成功");
        } else {
            Log.i(TAG, "人脸引擎初始化失败 " + engineCode);
        }
        return engineCode == ErrorInfo.MOK;
    }


    /**
     * 初始化图片识别引擎(图片)
     * 调用FaceEngine的init方法初始化SDK，初始化成功后才能进一步使用SDK的功能。
     *
     * @param context
     */
    public boolean initImgEngine(Context context, int detectFaceMaxNum) {
        mFaceEngine = new FaceEngine();
        int faceEngineCode = mFaceEngine.init(context,
                FaceEngine.ASF_DETECT_MODE_IMAGE,
                FaceEngine.ASF_OP_0_HIGHER_EXT,
                16,
                detectFaceMaxNum,
                FaceEngine.ASF_FACE_RECOGNITION | FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_AGE | FaceEngine.ASF_GENDER | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_LIVENESS);
        if (faceEngineCode == ErrorInfo.MOK) {
            Log.i(TAG, "人脸引擎初始化成功");
        } else {
            Log.i(TAG, "人脸引擎初始化失败 " + faceEngineCode);
        }
        return faceEngineCode == ErrorInfo.MOK;
    }


    /**
     * 初始化图片识别引擎（视频、拍照）
     * 调用FaceEngine的init方法初始化SDK，初始化成功后才能进一步使用SDK的功能。
     *
     * @param context
     * @param detectFaceMaxNum 最大检测人数
     */
    public boolean initVideoEngine(Context context, int detectFaceMaxNum) {
        mFaceEngine = new FaceEngine();
        int faceEngineCode = mFaceEngine.init(context,
                FaceEngine.ASF_DETECT_MODE_VIDEO,
                FaceEngine.ASF_OP_270_ONLY,
                16,
                detectFaceMaxNum,
                FaceEngine.ASF_FACE_RECOGNITION | FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS);
        if (faceEngineCode == ErrorInfo.MOK) {
            Log.i(TAG, "人脸引擎初始化成功");
        } else {
            Log.i(TAG, "人脸引擎初始化失败 " + faceEngineCode);
        }
        return faceEngineCode == ErrorInfo.MOK;
    }


    /**
     * 开始人脸检测
     *
     * @param bgr24  图像数据
     * @param width  图像的宽度
     * @param height 图像的高度
     * @param format 图像的格式
     */
    public boolean detectFaces(byte[] bgr24, int width, int height, int format) {
        faceInfoList.clear();

        /**
         * 2.成功获取到了BGR24 数据，开始人脸检测
         */
        int detectCode = mFaceEngine.detectFaces(bgr24, width, height, format, faceInfoList);
//        if (detectCode == ErrorInfo.MOK) {
//            Log.i(TAG, "detectFaces 人脸检测成功 faceInfos: " + faceInfoList.size());
//            onFaceDetectResult.detectFacesResult(detectCode, "人脸检测成功");
//        } else {
//            Log.i(TAG, "detectFaces 人脸检测失败 : " + detectCode);
//            onFaceDetectResult.detectFacesResult(detectCode, "人脸检测失败");
//        }
//
//
//        /**
//         * 3.若检测结果人脸数量大于0，则在bitmap上绘制人脸框并且重新显示到ImageView，若人脸数量为0，则无法进行下一步操作，操作结束
//         */
//        if (faceInfoList.size() < 1) {
//            onFaceDetectResult.detectFacesResult(detectCode, "检测不到人脸数据");
//            return;
//        }

        Log.i(TAG, "detectFaces  code is : " + detectCode + " faceInfos is " + faceInfoList.size());
        if (detectCode != ErrorInfo.MOK || faceInfoList.size() < 1) {
            return false;
        } else {
            return true;
        }

//        detectFaceInfo(faceInfoList, bgr24, width, height, format, onFaceDetectResult);
    }

    /**
     * 绘制人脸框
     *
     * @param bitmap
     */
    public Bitmap drawFaceRect(Bitmap bitmap) {
        //绘制bitmap
        bitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
        paint.setColor(Color.YELLOW);

        for (int i = 0; i < faceInfoList.size(); i++) {
            //绘制人脸框
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(faceInfoList.get(i).getRect(), paint);
            //绘制人脸序号
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setTextSize(faceInfoList.get(i).getRect().width() / 2);
            canvas.drawText("" + i, faceInfoList.get(i).getRect().left, faceInfoList.get(i).getRect().top, paint);
        }
        return bitmap;
    }

    /**
     * 检测人脸年龄、性别等信息
     *
     * @param bgr24
     * @param width
     * @param height
     * @param format             图像的格式
     * @param onFaceDetectResult
     */
    public void detectFaceInfo(byte[] bgr24, int width, int height, int format, IOnFaceDetectResult onFaceDetectResult) {
        /**
         * 4.上一步已获取到人脸位置和角度信息，传入给process函数，进行年龄、性别、三维角度检测
         */
        int faceProcessCode = mFaceEngine.process(bgr24, width, height, format, faceInfoList, FaceEngine.ASF_AGE | FaceEngine.ASF_GENDER | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_LIVENESS);
        if (faceProcessCode != ErrorInfo.MOK) {
            onFaceDetectResult.detectFaceInfos(faceProcessCode, "获取人脸年龄、性别等信息失败", null);
        } else {
            Log.i(TAG, "processImage 获取年龄、性别等信息成功 : ");
        }


        //年龄信息结果
        List<AgeInfo> ageInfoList = new ArrayList<>();
        //性别信息结果
        List<GenderInfo> genderInfoList = new ArrayList<>();
        //人脸三维角度结果
        List<Face3DAngle> face3DAngleList = new ArrayList<>();
        //活体检测结果
        List<LivenessInfo> livenessInfoList = new ArrayList<>();
        //获取年龄、性别、三维角度、活体结果
        int ageCode = mFaceEngine.getAge(ageInfoList);
        int genderCode = mFaceEngine.getGender(genderInfoList);
        int face3DAngleCode = mFaceEngine.getFace3DAngle(face3DAngleList);
        int livenessCode = mFaceEngine.getLiveness(livenessInfoList);

        if ((ageCode | genderCode | face3DAngleCode | livenessCode) != ErrorInfo.MOK) {
            Log.i(TAG, "detectFaceInfo 获取部分信息失败: ageCode: " + ageCode + " genderCode:" + genderCode + " face3DAngleCode:" + face3DAngleCode + " livenessCode:" + livenessCode);
            return;
        }


        /**
         * 5.年龄、性别、三维角度已获取成功，添加信息到提示文字中
         */
        List<FaceDetectInfo> mFaceDetectInfos = new ArrayList<>();
        for (int i = 0; i < faceInfoList.size(); i++) {
            FaceInfo faceInfo = faceInfoList.get(i);
            FaceFeature faceFeature = extractFaceFeature(bgr24, width, height, format, faceInfo);

            FaceDetectInfo faceDetectInfo = new FaceDetectInfo();
            faceDetectInfo.setFaceAge(ageInfoList.get(i).getAge());
            faceDetectInfo.setFaceGender(genderInfoList.get(i).getGender());
            faceDetectInfo.setFace3DAngle(face3DAngleList.get(i));
            faceDetectInfo.setLivenessInfo(livenessInfoList.get(i));
            faceDetectInfo.setFaceInfo(faceInfo);
            faceDetectInfo.setFaceFeature(faceFeature);
            mFaceDetectInfos.add(faceDetectInfo);
        }

        onFaceDetectResult.detectFaceInfos(faceProcessCode, "获取人脸年龄、性别等信息失败", mFaceDetectInfos);


//        //活体检测数据
//        if (livenessInfoList.size() > 0) {
//            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "liveness of each face:\n");
//            for (int i = 0; i < livenessInfoList.size(); i++) {
//                String liveness = null;
//                switch (livenessInfoList.get(i).getLiveness()) {
//                    case LivenessInfo.ALIVE:
//                        liveness = "ALIVE";
//                        break;
//                    case LivenessInfo.NOT_ALIVE:
//                        liveness = "NOT_ALIVE";
//                        break;
//                    case LivenessInfo.UNKNOWN:
//                        liveness = "UNKNOWN";
//                        break;
//                    case LivenessInfo.FACE_NUM_MORE_THAN_ONE:
//                        liveness = "FACE_NUM_MORE_THAN_ONE";
//                        break;
//                    default:
//                        liveness = "UNKNOWN";
//                        break;
//                }
//                addNotificationInfo(notificationSpannableStringBuilder, null, "face[", String.valueOf(i), "]:", liveness, "\n");
//            }
//        }
//        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");
    }


    /**
     * 提取人脸特征
     *
     * @param bgr24
     * @param width
     * @param height
     * @param faceInfo
     * @param format   图像的格式
     */
    public FaceFeature extractFaceFeature(byte[] bgr24, int width, int height, int format, FaceInfo faceInfo) {
        FaceFeature faceFeature = new FaceFeature();
        //从图片解析出人脸特征数据
        int extractFaceFeatureCode = mFaceEngine.extractFaceFeature(bgr24, width, height, format, faceInfo, faceFeature);
        if (extractFaceFeatureCode != ErrorInfo.MOK) {
            Log.i(TAG, "extractFaceFeature:extract face feature failed,code= " + extractFaceFeatureCode);
            faceFeature = null;
        } else {
            Log.i(TAG, "extractFaceFeature 提取人脸特征成功 : " + faceFeature.toString());
        }
        return faceFeature;
    }


    /**
     * 人脸比对
     *
     * @param faceFeature1 第一张人脸特征
     * @param faceFeature2 第二张人脸特征
     * @return
     */
    public float compare(FaceFeature faceFeature1, FaceFeature faceFeature2) {
        if (faceFeature1 == null || faceFeature2 == null) {
            return 0;
        }
        FaceSimilar faceSimilar = new FaceSimilar();
        //比对两个人脸特征获取相似度信息
        int compareFaceFeature = mFaceEngine.compareFaceFeature(faceFeature1, faceFeature2, faceSimilar);
        if (compareFaceFeature == ErrorInfo.MOK) {
            //获取相似度
            float score = faceSimilar.getScore();
            Log.d(TAG, "人脸比对成功 " + faceSimilar.getScore());
            return score;
        } else {
            Log.d(TAG, "人脸比对失败 " + compareFaceFeature);
        }
        return 0;
    }


    public String genderToStr(int gender) {
        String result = gender == GenderInfo.MALE ?
                "MALE" : (gender == GenderInfo.FEMALE ? "FEMALE" : "UNKNOWN");
        return result;
    }

}
