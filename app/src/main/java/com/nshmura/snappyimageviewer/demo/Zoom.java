package com.nshmura.snappyimageviewer.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import ameircom.keymedia.R;

public class Zoom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        final ImageView imageView = (ImageView) findViewById(R.id.image);
        final int resId = R.drawable.sample;
        imageView.setImageResource(resId);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewerActivity.start(Zoom.this, resId, imageView);
            }
        });
    }
}
