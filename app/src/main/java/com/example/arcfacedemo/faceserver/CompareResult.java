package com.example.arcfacedemo.faceserver;

/**
 * @author 周旭
 * @company 义金健康
 * @e-mail 374952705@qq.com
 * @time 2019/2/19
 * @descripe
 */


public class CompareResult {
    private String userName;
    private float similar;
    private int trackId;

    public CompareResult(String userName, float similar) {
        this.userName = userName;
        this.similar = similar;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getSimilar() {
        return similar;
    }

    public void setSimilar(float similar) {
        this.similar = similar;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }
}
