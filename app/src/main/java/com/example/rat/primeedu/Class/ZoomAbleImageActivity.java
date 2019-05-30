package com.example.rat.primeedu.Class;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rat.primeedu.R;

import java.net.URI;

public class ZoomAbleImageActivity extends AppCompatActivity implements View.OnClickListener {

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;
    String Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_able_image);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.texTcolor));


        getIntentData();
        init();
    }
    private void getIntentData(){
        Url = getIntent().getStringExtra("url").toString();
    }

    private void init(){
        findViewById(R.id.back).setOnClickListener(this);
        mImageView = findViewById(R.id.imageView);
        Glide.with(this).
                load(Url)
                .into(mImageView);

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScaleGestureDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            finish();
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        // when a scale gesture is detected, use it to resize the image
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mImageView.setScaleX(mScaleFactor);
            mImageView.setScaleY(mScaleFactor);
            return true;
        }
    }
}
