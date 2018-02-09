package com.jia.lyricview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.commit451.nativestackblur.NativeStackBlur;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private JsLyricView lyricView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lyricView = (JsLyricView) findViewById(R.id.lyricView);

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



    }
}
