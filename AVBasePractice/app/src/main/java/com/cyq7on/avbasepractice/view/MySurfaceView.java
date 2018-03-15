package com.cyq7on.avbasepractice.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cyq7on.avbasepractice.R;

import java.io.InputStream;

/**
 * Created by cyq7on on 18-3-13.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;

    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        //更改背景为透明
        setZOrderOnTop(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClickable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);

        canvas = holder.lockCanvas();
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android_logo);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        InputStream is = getResources().openRawResource(+R.drawable.android_logo);
        Bitmap bitmap =  BitmapFactory.decodeStream(is, null, opt);

        canvas.drawBitmap(bitmap, 0, 0, paint);
        surfaceHolder.unlockCanvasAndPost(canvas);

        bitmap = null;

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        holder.removeCallback(this);
    }
}
