package com.example.arcfacedemo.model;

/**
 * @author 周旭
 * @company 义金健康
 * @e-mail 374952705@qq.com
 * @time 2019/2/19
 * @descripe
 */


public class FaceRegisterInfo {
    private byte[] featureData;
    private String name;

    public FaceRegisterInfo(byte[] faceFeature, String name) {
        this.featureData = faceFeature;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFeatureData() {
        return featureData;
    }

    public void setFeatureData(byte[] featureData) {
        this.featureData = featureData;
    }

}
