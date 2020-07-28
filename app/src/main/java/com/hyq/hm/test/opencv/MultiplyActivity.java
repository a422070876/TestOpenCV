package com.hyq.hm.test.opencv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.IOException;
import java.io.InputStream;

public class MultiplyActivity extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap catBitmap = decodeResource(getResources(),R.drawable.ic_car);
        Bitmap pkqBitmap = Bitmap.createBitmap( catBitmap.getWidth(), catBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap bitmap = decodeResource(getResources(),R.drawable.ic_pkq);
        Canvas canvas = new Canvas(pkqBitmap);
        int left = 1100;
        int top = 420;
        canvas.drawARGB(255,255,255,255);
        canvas.drawBitmap(bitmap,new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),new RectF(left,top,left+bitmap.getWidth(),top+bitmap.getHeight()),null);

        Mat catMat = new Mat();
        Mat pkqMat = new Mat();
        Utils.bitmapToMat(catBitmap, catMat);
        Utils.bitmapToMat(pkqBitmap, pkqMat);
        Mat dst = CvImageCompute.cvMultiply(catMat,pkqMat);
//        Mat dst = new Mat();
//        Core.multiply(catMat,pkqMat,dst);
        Bitmap dstBitmap = Bitmap.createBitmap(catBitmap.getWidth(), catBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,dstBitmap);

        ImageView imageView = findViewById(R.id.image_view);
        imageView.setImageBitmap(dstBitmap);
    }

    public Bitmap decodeResource(Resources res, int id) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = null;
        InputStream is = null;
        try {
            final TypedValue value = new TypedValue();
            is = res.openRawResource(id, value);
            opts.inTargetDensity = value.density;
            opts.inScaled = false;
            bm = BitmapFactory.decodeResourceStream(res, value, is, null, opts);
        } catch (Exception e) {
            /*  do nothing.
                If the exception happened on open, bm will be null.
                If it happened on close, bm is still valid.
            */
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                // Ignore
            }
        }

        if (bm == null && opts.inBitmap != null) {
            throw new IllegalArgumentException("Problem decoding into existing bitmap");
        }
        return bm;
    }

}
