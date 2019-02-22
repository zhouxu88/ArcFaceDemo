package com.example.arcfacedemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.ParcelableSpan;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.example.arcfacedemo.R;
import com.example.arcfacedemo.arc.ArcFaceManage;
import com.example.arcfacedemo.arc.FaceDetectInfo;
import com.example.arcfacedemo.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SingleImageActivity extends AppCompatActivity implements ArcFaceManage.IOnFaceDetectResult {
    private static final String TAG = "SingleImageActivity";
    private ImageView ivShow;
    private TextView tvNotice;
    private FaceEngine faceEngine;
    private int mDetectCode = -1;
    private int mFaceProcessCode = -1;
    private boolean isInit;
    private int mFaceInfoSize = 0;
    public List<FaceFeature> mFaceFeatureList = new ArrayList<>();


    /**
     * 请求选择本地图片文件的请求码
     */
    private static final int ACTION_CHOOSE_IMAGE = 0x201;
    /**
     * 提示对话框
     */
    private AlertDialog progressDialog;
    /**
     * 被处理的图片
     */
    private Bitmap mBitmap = null;

    private SpannableStringBuilder notificationSpannableStringBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);
        initView();
        faceEngine = new FaceEngine();
        isInit = ArcFaceManage.getInstance().initImgEngine(this, 6);
    }


    private void initView() {
        tvNotice = findViewById(R.id.tv_notice);
        ivShow = findViewById(R.id.iv_show);
        ivShow.setImageResource(R.mipmap.faces);
        progressDialog = new AlertDialog.Builder(this)
                .setTitle("识别中")
                .setView(new ProgressBar(this))
                .create();
    }

    /**
     * 按钮点击响应事件
     *
     * @param view
     */
    public void process(final View view) {

        view.setClickable(false);
        if (progressDialog == null || progressDialog.isShowing()) {
            return;
        }
        progressDialog.show();
        //图像转化操作和部分引擎调用比较耗时，建议放子线程操作
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                processImage();
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        view.setClickable(true);
                    }
                });
    }


    /**
     * 主要操作逻辑部分
     */
    public void processImage() {
        /**
         * 1.准备操作（校验，显示，获取BGR）
         */
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.faces);
        }
        Bitmap bitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);

        notificationSpannableStringBuilder = new SpannableStringBuilder();
        if (!isInit) {
            addNotificationInfo(notificationSpannableStringBuilder, null, " face engine not initialized!");
            showNotificationAndFinish(notificationSpannableStringBuilder);
            return;
        }

        if (faceEngine == null) {
            addNotificationInfo(notificationSpannableStringBuilder, null, " faceEngine is null!");
            showNotificationAndFinish(notificationSpannableStringBuilder);
            return;
        }

        if (bitmap == null) {
            addNotificationInfo(notificationSpannableStringBuilder, null, " bitmap is null!");
            showNotificationAndFinish(notificationSpannableStringBuilder);
            return;
        }

        bitmap = ImageUtil.alignBitmapForBgr24(bitmap);


        if (bitmap == null) {
            addNotificationInfo(notificationSpannableStringBuilder, null, " bitmap is null!");
            showNotificationAndFinish(notificationSpannableStringBuilder);
            return;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        final Bitmap finalBitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivShow.setImageBitmap(finalBitmap);
            }
        });

        //bitmap转bgr
        byte[] bgr24 = ImageUtil.bitmapToBgr(bitmap);

        addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "start face detection,\n" + "imageWidth is " + width + ", imageHeight is " + height + "\n");

        if (bgr24 == null) {
            addNotificationInfo(notificationSpannableStringBuilder, new ForegroundColorSpan(Color.RED), "can not get bgr24 data of bitmap!\n");
            showNotificationAndFinish(notificationSpannableStringBuilder);
            return;
        }
         List<FaceInfo> faceInfoList = ArcFaceManage.getInstance().faceInfoList;


        ArcFaceManage.getInstance().detectFaces( bgr24, width, height, FaceEngine.CP_PAF_BGR24);
        ArcFaceManage.getInstance().detectFaceInfo(bgr24, width,height,FaceEngine.CP_PAF_BGR24,this);
//        mFaceInfoSize = faceInfoList.size();


        //绘制bitmap
//        Bitmap bitmapForDraw = bitmap.copy(Bitmap.Config.RGB_565, true);
//        Canvas canvas = new Canvas(bitmapForDraw);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setStrokeWidth(5);
//        paint.setColor(Color.YELLOW);
//        for (int i = 0; i < faceInfoList.size(); i++) {
//            //绘制人脸框
//            paint.setStyle(Paint.Style.STROKE);
//            canvas.drawRect(faceInfoList.get(i).getRect(), paint);
//            //绘制人脸序号
//            paint.setStyle(Paint.Style.FILL_AND_STROKE);
//            int textSize = faceInfoList.get(i).getRect().width() / 2;
//            paint.setTextSize(textSize);
//            canvas.drawText(String.valueOf(i), faceInfoList.get(i).getRect().left, faceInfoList.get(i).getRect().top, paint);
//        }

        bitmap = ArcFaceManage.getInstance().drawFaceRect(bitmap);

        //显示
        final Bitmap finalBitmapForDraw = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivShow.setImageBitmap(finalBitmapForDraw);
            }
        });

        addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "faceFeatureExtract:\n");

        for (int i = 0; i < faceInfoList.size(); i++) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "face[" + i + "]:");
            FaceInfo faceInfo = faceInfoList.get(i);
            FaceFeature faceFeature = ArcFaceManage.getInstance().extractFaceFeature(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfo);
            if (faceFeature != null) {
                mFaceFeatureList.add(faceFeature);
            }
        }

        compare();

        showNotificationAndFinish(notificationSpannableStringBuilder);
    }


    //比对
    public void compare() {
        /**
         * 6.最后将图片内的所有人脸进行一一比对并添加到提示文字中
         */

        //人脸特征的数量大于2，将所有特征进行比较
        if (mFaceFeatureList.size() < 2) {
            return;
        }

        addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "人脸相似度:\n");
        for (int i = 0; i < mFaceFeatureList.size(); i++) {
            for (int j = i + 1; j < mFaceFeatureList.size(); j++) {
                addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD_ITALIC), "compare face[", String.valueOf(i), "] and  face["
                        , String.valueOf(j), "]:\n");
                //若其中一个特征提取失败，则不进行比对
                float similarScore = ArcFaceManage.getInstance().compare(mFaceFeatureList.get(i), mFaceFeatureList.get(j));

                //新增相似度比对结果信息
                addNotificationInfo(notificationSpannableStringBuilder, null, "similar of face[", String.valueOf(i), "] and  face[",
                        String.valueOf(j), "] is:", String.valueOf(similarScore), "\n");
            }
        }
    }


    /**
     * 展示提示信息并且关闭提示框
     *
     * @param stringBuilder 带格式的提示文字
     */
    private void showNotificationAndFinish(final SpannableStringBuilder stringBuilder) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tvNotice != null) {
                    tvNotice.setText(stringBuilder);
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 追加提示信息
     *
     * @param stringBuilder 提示的字符串的存放对象
     * @param styleSpan     添加的字符串的格式
     * @param strings       字符串数组
     */
    private void addNotificationInfo(SpannableStringBuilder stringBuilder, ParcelableSpan styleSpan, String... strings) {
        if (stringBuilder == null || strings == null || strings.length == 0) {
            return;
        }
        int startLength = stringBuilder.length();
        for (String string : strings) {
            stringBuilder.append(string);
        }
        int endLength = stringBuilder.length();
        if (styleSpan != null) {
            stringBuilder.setSpan(styleSpan, startLength, endLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }


    /**
     * 从本地选择文件
     *
     * @param view
     */
    public void chooseLocalImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, ACTION_CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_CHOOSE_IMAGE) {
            if (data == null || data.getData() == null) {
                Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
                return;
            }
            mBitmap = ImageUtil.getBitmapFromUri(data.getData(), this);
            if (mBitmap == null) {
                Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
                return;
            }
            ivShow.setImageBitmap(mBitmap);
        }
    }


    @Override
    public void detectFaceInfos(int faceProcessCode, String msg, List<FaceDetectInfo> faceDetectInfoList) {
        mFaceProcessCode = faceProcessCode;
        if (faceProcessCode != ErrorInfo.MOK || faceDetectInfoList == null) {
            addNotificationInfo(notificationSpannableStringBuilder, null, msg + "\n");
            showNotificationAndFinish(notificationSpannableStringBuilder);
            return;
        }

        for (int i = 0; i < faceDetectInfoList.size(); i++) {
            FaceDetectInfo faceDetectInfo = faceDetectInfoList.get(i);
            String gender = faceDetectInfo.getFaceGender() == GenderInfo.MALE ?
                    "MALE" : (faceDetectInfo.getFaceGender() == GenderInfo.FEMALE ? "FEMALE" : "UNKNOWN");

            String liveness = "";
            switch (faceDetectInfo.getLivenessInfo().getLiveness()) {
                case LivenessInfo.ALIVE:
                    liveness = "ALIVE";
                    break;
                case LivenessInfo.NOT_ALIVE:
                    liveness = "NOT_ALIVE";
                    break;
                case LivenessInfo.UNKNOWN:
                    liveness = "UNKNOWN";
                    break;
                case LivenessInfo.FACE_NUM_MORE_THAN_ONE:
                    liveness = "FACE_NUM_MORE_THAN_ONE";
                    break;
                default:
                    liveness = "UNKNOWN";
                    break;
            }

            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "face[", String.valueOf(i), "]:\n");

            addNotificationInfo(notificationSpannableStringBuilder, null, "age:", String.valueOf(faceDetectInfo.getFaceAge()), "\n",
                    "gender: ", gender, "\n", "face3DAngle:", faceDetectInfo.getFace3DAngle().toString(), "\n", "liveness:", liveness, "\n");
            addNotificationInfo(notificationSpannableStringBuilder, null, "\n");
            showNotificationAndFinish(notificationSpannableStringBuilder);
        }
    }
}
