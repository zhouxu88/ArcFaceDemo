package com.example.arcfacedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.example.arcfacedemo.model.ItemShowInfo;
import com.example.arcfacedemo.R;
import com.example.arcfacedemo.adapter.ShowInfoAdapter;
import com.example.arcfacedemo.arc.ArcFaceManage;
import com.example.arcfacedemo.arc.FaceDetectInfo;
import com.example.arcfacedemo.utils.ImageUtil;
import com.example.arcfacedemo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MultiImageActivity extends AppCompatActivity implements ArcFaceManage.IOnFaceDetectResult {

    private static final String TAG = "MultiImageActivity";

    private static final int ACTION_CHOOSE_MAIN_IMAGE = 0x201;
    private static final int ACTION_ADD_RECYCLER_ITEM_IMAGE = 0x202;

    private Context mContext;
    private ImageView ivMainImage;
    private TextView tvMainImageInfo;
    private RecyclerView faceRv;
    /**
     * 选择图片时的类型
     */
    private int TYPE_MAIN = 0;
    private int TYPE_ITEM = 1;
    private int mPicType = 0;

    /**
     * 主图的第0张人脸的特征数据
     */
    private FaceFeature mainFeature;

    private ShowInfoAdapter showInfoAdapter;
    private List<ItemShowInfo> showInfoList = new ArrayList<>();

    private FaceEngine faceEngine;
    private boolean isInit = false;
    private Bitmap mainBitmap;
    private Bitmap itemBitmap;
    private int width;
    private int height;
    private byte[] nv21;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_image);
        mContext = this;
        initView();
        faceEngine = new FaceEngine();
        isInit = ArcFaceManage.getInstance().initImgEngine(mContext, 6);
    }


    private void initView() {
        ivMainImage = findViewById(R.id.iv_main_image);
        tvMainImageInfo = findViewById(R.id.tv_main_image_info);
        faceRv = findViewById(R.id.recycler_faces);
        showInfoAdapter = new ShowInfoAdapter(this, showInfoList);
        faceRv.setAdapter(showInfoAdapter);
        faceRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        faceRv.setLayoutManager(new LinearLayoutManager(this));
    }


    public void processImage(Bitmap bitmap, int type) {
        if (bitmap == null || faceEngine == null) {
            return;
        }


        //NV21宽度必须为4的倍数,高度为2的倍数
        bitmap = ImageUtil.alignBitmapForNv21(bitmap);

        if (bitmap == null) {
            return;
        }
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        //bitmap转NV21
        nv21 = ImageUtil.bitmapToNv21(bitmap, width, height);
        if (nv21 == null) {
            Log.i(TAG, "processImage: can not get nv21 from bitmap!");
            return;
        }


        boolean result = ArcFaceManage.getInstance().detectFaces(nv21, width, height, FaceEngine.CP_PAF_NV21);
        if (!result) {
            ToastUtils.showToast(mContext, "人脸检测失败");
            return;
        }

        if (ArcFaceManage.getInstance().faceInfoList.size() > 1) {
            ToastUtils.showToast(mContext, "只能选择单人照片");
            return;
        }


        //绘制bitmap
        bitmap = ArcFaceManage.getInstance().drawFaceRect(bitmap);
        if (type == TYPE_ITEM) {
            itemBitmap = bitmap;
        }

        ArcFaceManage.getInstance().detectFaceInfo(nv21, width, height, FaceEngine.CP_PAF_NV21, this);


        //人脸比对数据显示
        if (type == TYPE_MAIN) {
            int size = showInfoList.size();
            showInfoList.clear();
            showInfoAdapter.notifyItemRangeRemoved(0, size);
            ivMainImage.setImageBitmap(mainBitmap);
//            mainFeature = new FaceFeature();
//            int res = faceEngine.extractFaceFeature(nv21, width, height, FaceEngine.CP_PAF_NV21, faceInfoList.get(0), mainFeature);
//            if (res != ErrorInfo.MOK) {
//                mainFeature = null;
//            }
            mainFeature = ArcFaceManage.getInstance().extractFaceFeature(nv21, width, height, FaceEngine.CP_PAF_NV21, ArcFaceManage.getInstance().faceInfoList.get(0));
            ivMainImage.setImageBitmap(bitmap);
        } else if (type == TYPE_ITEM) {
//            ItemShowInfo showInfo = new ItemShowInfo(itemBitmap, 25, 1, 1f);
//            showInfoList.add(showInfo);
//            showInfoAdapter.notifyItemInserted(showInfoList.size() - 1);
        }
    }


    /**
     * 从本地选择文件
     *
     * @param action 可为选择主图{@link #ACTION_CHOOSE_MAIN_IMAGE}或者选择item图{@link #ACTION_ADD_RECYCLER_ITEM_IMAGE}
     */
    public void chooseLocalImage(int action) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, action);
    }

    public void chooseMainImage(View view) {
        if (!isInit) {
            return;
        }
        chooseLocalImage(ACTION_CHOOSE_MAIN_IMAGE);
    }

    public void addItemFace(View view) {
        if (mainBitmap == null) {
            ToastUtils.showToast(mContext, "请先选择主图");
            return;
        }
        chooseLocalImage(ACTION_ADD_RECYCLER_ITEM_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null || data.getData() == null) {
            ToastUtils.showToast(mContext, "获取图片失败");
            return;
        }
        if (requestCode == ACTION_CHOOSE_MAIN_IMAGE) {
            mainBitmap = ImageUtil.getBitmapFromUri(data.getData(), this);
            if (mainBitmap == null) {
                ToastUtils.showToast(mContext, "获取图片失败");
                return;
            }
            mPicType = TYPE_MAIN;
            processImage(mainBitmap, TYPE_MAIN);

        } else if (requestCode == ACTION_ADD_RECYCLER_ITEM_IMAGE) {
            itemBitmap = ImageUtil.getBitmapFromUri(data.getData(), this);
            if (itemBitmap == null) {
                ToastUtils.showToast(mContext, "获取图片失败");
                return;
            }
            if (mainFeature == null) {
                return;
            }
            mPicType = TYPE_ITEM;
            processImage(itemBitmap, TYPE_ITEM);
        }
    }


    @Override
    public void detectFaceInfos(int faceProcessCode, String msg, List<FaceDetectInfo> faceDetectInfoList) {
        if (faceDetectInfoList == null || faceDetectInfoList.size() < 1) {
            return;
        }
        if (mPicType == TYPE_MAIN) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("face info:\n\n");
            FaceDetectInfo faceDetectInfo = faceDetectInfoList.get(0);
            String gender = ArcFaceManage.getInstance().genderToStr(faceDetectInfo.getFaceGender());
            stringBuilder.append("\nage:")
                    .append(faceDetectInfo.getFaceAge())
                    .append("\ngender:")
                    .append(gender)
                    .append("\nface3DAngle:")
                    .append(faceDetectInfo.getFace3DAngle())
                    .append("\n\n");
            tvMainImageInfo.setText(stringBuilder);
        } else if (mPicType == TYPE_ITEM) {
            FaceFeature itemFaceFeature = ArcFaceManage.getInstance().extractFaceFeature(nv21, width, height, FaceEngine.CP_PAF_NV21, ArcFaceManage.getInstance().faceInfoList.get(0));
            if (itemFaceFeature != null) {
                float similar = ArcFaceManage.getInstance().compare(mainFeature, itemFaceFeature);
                FaceDetectInfo faceDetectInfo = faceDetectInfoList.get(0);
                ItemShowInfo showInfo = new ItemShowInfo(itemBitmap, faceDetectInfo.getFaceAge(), faceDetectInfo.getFaceGender(), similar);
                showInfoList.add(showInfo);
                showInfoAdapter.notifyItemInserted(showInfoList.size() - 1);
            }
        }
    }

}
