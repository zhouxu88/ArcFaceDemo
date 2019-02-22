package com.example.arcfacedemo.faceserver;

import android.support.annotation.Nullable;

import com.arcsoft.face.FaceFeature;

/**
 * @author 周旭
 * @company 义金健康
 * @e-mail 374952705@qq.com
 * @time 2019/2/19
 * @descripe
 */


public interface FaceListener {
    /**
     * 当出现异常时执行
     *
     * @param e 异常信息
     */
    void onFail(Exception e);


    /**
     * 请求人脸特征后的回调
     *
     * @param faceFeature    人脸特征数据
     * @param requestId 请求码
     */
    void  onFaceFeatureInfoGet(@Nullable FaceFeature faceFeature, Integer requestId);

}
