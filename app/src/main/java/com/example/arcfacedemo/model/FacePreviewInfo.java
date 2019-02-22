package com.example.arcfacedemo.model;

import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.LivenessInfo;

/**
 * @author 周旭
 * @company 义金健康
 * @e-mail 374952705@qq.com
 * @time 2019/2/19
 * @descripe
 */


public class FacePreviewInfo {
    private FaceInfo faceInfo;
    private LivenessInfo livenessInfo;
    private int trackId;

    public FacePreviewInfo(FaceInfo faceInfo, LivenessInfo livenessInfo, int trackId) {
        this.faceInfo = faceInfo;
        this.livenessInfo = livenessInfo;
        this.trackId = trackId;
    }

    public FaceInfo getFaceInfo() {
        return faceInfo;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        this.faceInfo = faceInfo;
    }

    public LivenessInfo getLivenessInfo() {
        return livenessInfo;
    }

    public void setLivenessInfo(LivenessInfo livenessInfo) {
        this.livenessInfo = livenessInfo;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }
}
