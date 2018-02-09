package com.jia.lyricview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.commit451.nativestackblur.NativeStackBlur;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private JsLyricView lyricView;
    private RecorderView recorderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lyricView = (JsLyricView) findViewById(R.id.lyricView);
        recorderView = (RecorderView) findViewById(R.id.recorderView);

        Resources r = this.getResources();
        Bitmap bm = NativeStackBlur.process(BitmapFactory.decodeResource(r, R.mipmap.lyric_bg), 25);
        lyricView.setBackground(new BitmapDrawable(bm));

        InputStream is = null;
        try {
            is = getAssets().open("beijing.lrc");
            lyricView.setupLyricResource(is, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        lyricView.setOnClickListener(this);
//        recorderView.setOnClickListener(this);
        lyricView.setVisibility(View.GONE);
        recorderView.setVisibility(View.VISIBLE);

        recorderView.setPlaying(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyricView:

                lyricView.setVisibility(View.GONE);
                recorderView.setVisibility(View.VISIBLE);

                break;
            case R.id.recorderView:

                lyricView.setVisibility(View.VISIBLE);
                recorderView.setVisibility(View.GONE);

                break;
        }
    }
}
