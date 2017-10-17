package com.security.news.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.security.news.R;
import com.security.news.util.Constants;

/**
 * 展示图片大图界面;
 */
public class PhotoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        PhotoView iv_image= (PhotoView) findViewById(R.id.iv_photo);
        String photo = getIntent().getStringExtra("photo");
        Glide.with(PhotoDetailActivity.this).load(Constants.BASE_URL+photo)
                .into(iv_image);
    }
}
