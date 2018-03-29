package com.example.lence.bird_hunter.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.lence.bird_hunter.R;
import com.example.lence.bird_hunter.afilechooser.utils.FileUtils;
import com.example.lence.bird_hunter.dateBase.DBManager;
import com.example.lence.bird_hunter.utils.GPSconnect;
import com.example.lence.bird_hunter.utils.NetworkUtil;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements MapInterface, MVPUpDate, MVP {

    Map mMap;
    double gpsmyX = 0;
    double gpsmyY = 0;

    @BindView(R.id.viewFlipper)
    ViewFlipper mViewFlipper;
    @BindView(R.id.addPhoto)
    FloatingActionButton mAddPhoto;
    @BindView(R.id.autoText)
    AutoCompleteTextView mAutoText;
    @BindView(R.id.send)
    FloatingActionButton mSend;
    @BindView(R.id.locat)
    TextView mLocat;
    @BindView(R.id.arrowLeft)
    ImageView mArrowLeft;
    @BindView(R.id.arrowRight)
    ImageView mArrowRight;
    @BindView(R.id.rel)
    RelativeLayout mRel;
    @BindView(R.id.nullImage)
    ImageView mNullImage;
    @BindView(R.id.del)
    FloatingActionButton mDel;
    private float fromPosition;
    Presenter mPresenter;
    List<String> birds;
    DBManager dbManager;
    ProgressDialog dialog;
    List<MultipartBody.Part> files;
    SharedPreferences mSharedPreferences;
    File directory;
    File file;
    final int TYPE_PHOTO = 1;
    final int TYPE_VIDEO = 2;
    Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        ButterKnife.bind(this);
        mSharedPreferences = getSharedPreferences("bird", MODE_PRIVATE);
        Log.e("share", mSharedPreferences.getString("id", "null"));
        dialog = new ProgressDialog(this);
        dialog.setTitle("Обновление базы");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        dbManager = new DBManager(this);
        mPresenter = new Presenter(this);
        if (NetworkUtil.isNetworkConnected(this)) {
            mPresenter.loadBirds();
        } else {
            Toast.makeText(this, "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
        }
        GPSconnect.execute(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mMap = new Map(this, mapFragment, this);
        createDirectory();
        files = new ArrayList<>();


//        mViewFlipper.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                if (mViewFlipper.getChildCount() > 1) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            fromPosition = event.getX();
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            float toPosition = event.getX();
//                            if (fromPosition > toPosition) {
//                                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_next_in));
//                                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_next_out));
//                                mViewFlipper.showNext();
//                            } else if (fromPosition < toPosition) {
//                                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_prev_in));
//                                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_prev_out));
//                                mViewFlipper.showPrevious();
//                            }
//                        default:
//                            break;
//                    }
//                }
//                return true;
//
//            }
//        });

        mViewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "click");
                if (mDel.getVisibility() == View.INVISIBLE && files.size() > 0) {
                    mDel.setVisibility(View.VISIBLE);
                } else {
                    mDel.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @OnClick(R.id.arrowLeft)
    public void onMArrowLeftClicked() {
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_prev_in));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_prev_out));
        mViewFlipper.showPrevious();
        mDel.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.arrowRight)
    public void onMArrowRightClicked() {
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_next_in));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_next_out));
        mViewFlipper.showNext();
        mDel.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.del)
    public void onViewClicked() {
        files.remove(mViewFlipper.getDisplayedChild());
        mViewFlipper.removeViewAt(mViewFlipper.getDisplayedChild());

        mDel.setVisibility(View.INVISIBLE);
        if (mViewFlipper.getChildCount() == 1) {
            mArrowLeft.setVisibility(View.INVISIBLE);
            mArrowRight.setVisibility(View.INVISIBLE);
        }
        if (mViewFlipper.getChildCount() == 0) {
            mNullImage.setVisibility(View.VISIBLE);
            mViewFlipper.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void onLocationChanged(double gpsmyX, double gpsmyY) {
        this.gpsmyX = gpsmyX;
        this.gpsmyY = gpsmyY;
        //Log.e("loc", gpsmyX + " " + gpsmyY);
        mLocat.setText(String.format("%.4f", gpsmyX) + "\n" + String.format("%.4f", gpsmyY));
    }

    @OnClick(R.id.addPhoto)
    public void onMAddPhotoClicked() {
        saveFullImage();

    }

    @OnClick(R.id.send)
    public void onMSendClicked() {
        Log.e("send", String.valueOf(files.size()));
        if (!mSharedPreferences.getString("id", "null").equals("null") && gpsmyX > 0 && gpsmyY > 0 && mAutoText.getText().length() > 0 && files.size() > 0) {
            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("id", RequestBody.create(okhttp3.MultipartBody.FORM, mSharedPreferences.getString("id", "null")));
            map.put("x", RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(gpsmyX)));
            map.put("y", RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(gpsmyY)));
            map.put("bird", RequestBody.create(okhttp3.MultipartBody.FORM, mAutoText.getText().toString()));

            dialog.setTitle("Отправка данных");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            mPresenter.sendBirds(map, files);
        } else
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
    }

    private ImageView mPhoto;
    private static int TAKE_PICTURE = 1;


    private void saveFullImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mUri = generateFileUri(TYPE_PHOTO);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent, TYPE_PHOTO);
    }

    private Uri generateFileUri(int type) {
        file = null;
        switch (type) {
            case TYPE_PHOTO:
                file = new File(directory.getPath() + "/" + "photo_"
                        + System.currentTimeMillis() + ".jpg");
                break;
            case TYPE_VIDEO:
                file = new File(directory.getPath() + "/" + "video_"
                        + System.currentTimeMillis() + ".mp4");
                break;
        }
        Log.e("fileName", "fileName = " + file);
        return Uri.fromFile(file);
    }

    private void createDirectory() {
        directory = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "BirdHunter");
        if (!directory.exists())
            directory.mkdirs();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("camera Close", "dd");
        if (requestCode == TYPE_PHOTO) {
            if (resultCode == RESULT_OK) {

                Log.e("data", "" + data);
                Log.e("file", "" + file.getPath());
                //File f = FileUtils.getFile(this, mUri);

                RequestBody requestBody = RequestBody.create(MediaType.parse("image"), file);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("file" + files.size(), file.getName(), requestBody);
                files.add(filePart);

                mPhoto = new ImageView(this);
                mPhoto.setImageURI(mUri);
                mNullImage.setVisibility(View.INVISIBLE);
                mViewFlipper.setVisibility(View.VISIBLE);
                mViewFlipper.addView(mPhoto);

                if (mViewFlipper.getChildCount() > 1) {
                    mArrowLeft.setVisibility(View.VISIBLE);
                    mArrowRight.setVisibility(View.VISIBLE);
                }


            } else if (resultCode == RESULT_CANCELED) {
                Log.e("data", "Canceled");
            }
        }




    }


    @Override
    public void addBirds(List<String> birds) {
        this.birds = new ArrayList<>(birds);
        mAutoText.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, birds));
        dbManager.upDate(birds);
        // Log.e("db", dbManager.getBirds().toString());
        dialog.dismiss();
    }

    @Override
    public void showError() {
        if (!dbManager.getBirds().isEmpty()) {
            this.birds = new ArrayList<>(dbManager.getBirds());
            mAutoText.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, birds));
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }

    @SuppressLint("ShowToast")
    @Override
    public void show(String res) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void clear() {
        mAutoText.setText("");
        //file.get(0).
        files = new ArrayList<>();
        mViewFlipper.removeAllViews();
        mNullImage.setVisibility(View.VISIBLE);
        mViewFlipper.setVisibility(View.INVISIBLE);
        mArrowLeft.setVisibility(View.INVISIBLE);
        mArrowRight.setVisibility(View.INVISIBLE);
    }
}
