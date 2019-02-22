package com.example.arcfacedemo.arc;

import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.LivenessInfo;

/**
 * @author 周旭
 * @company 义金健康
 * @e-mail 374952705@qq.com
 * @time 2019/2/15
 * @descripe
 */


public class FaceDetectInfo {

    private int faceAge;
    private int faceGender;
    private Face3DAngle face3DAngle;
    private LivenessInfo livenessInfo;
    private FaceInfo faceInfo;
    private FaceFeature faceFeature;


    public int getFaceAge() {
        return faceAge;
    }

    public void setFaceAge(int faceAge) {
        this.faceAge = faceAge;
    }

    public int getFaceGender() {
        return faceGender;
    }

    public void setFaceGender(int faceGender) {
        this.faceGender = faceGender;
    }

    public Face3DAngle getFace3DAngle() {
        return face3DAngle;
    }

    public void setFace3DAngle(Face3DAngle face3DAngle) {
        this.face3DAngle = face3DAngle;
    }

    public LivenessInfo getLivenessInfo() {
        return livenessInfo;
    }

    public void setLivenessInfo(LivenessInfo livenessInfo) {
        this.livenessInfo = livenessInfo;
    }

    public FaceInfo getFaceInfo() {
        return faceInfo;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        this.faceInfo = faceInfo;
    }

    public FaceFeature getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(FaceFeature faceFeature) {
        this.faceFeature = faceFeature;
    }
}
