package com.jia.lyricview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.commit451.nativestackblur.NativeStackBlur;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private JsLyricView lyricView;
    private RecorderView recorderView;
    private RelativeLayout rl_recorderView;
    private TextView tv_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lyricView = (JsLyricView) findViewById(R.id.lyricView);
        recorderView = (RecorderView) findViewById(R.id.recorderView);
        rl_recorderView = (RelativeLayout) findViewById(R.id.rl_recorderView);
        tv_change = (TextView) findViewById(R.id.tv_change);

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

        tv_change.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change:

                if (lyricView.getVisibility() == View.VISIBLE) {
                    lyricView.setVisibility(View.GONE);
                    rl_recorderView.setVisibility(View.VISIBLE);
                    recorderView.setPlaying(true);
                } else {
                    lyricView.setVisibility(View.VISIBLE);
                    rl_recorderView.setVisibility(View.GONE);
                    recorderView.setPlaying(false);
                }

                break;

        }
    }
}
