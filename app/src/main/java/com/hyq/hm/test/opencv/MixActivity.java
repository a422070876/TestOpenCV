package com.hyq.hm.test.opencv;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MixActivity extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bitmap bitmap = decodeResource(getResources(),R.drawable.ic_pkq);
        Bitmap pkqBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(pkqBitmap);
        canvas.drawBitmap(bitmap,new Rect(0,0,bitmap.getWidth()/2,bitmap.getHeight()),
                new Rect(0,0,bitmap.getWidth()/2,bitmap.getHeight()),null);
        Paint paint = new Paint();
        paint.setAlpha(180);
        canvas.drawBitmap(bitmap,new Rect(bitmap.getWidth()/2,0,bitmap.getWidth(),bitmap.getHeight()),
                new Rect(bitmap.getWidth()/2,0,bitmap.getWidth(),bitmap.getHeight()),paint);

        Bitmap catBitmap = decodeResource(getResources(),R.drawable.ic_car);
        catBitmap = Bitmap.createScaledBitmap(catBitmap,bitmap.getWidth(),bitmap.getHeight(),false);

        Mat catMat = new Mat();
        Mat pkqMat = new Mat();
        Utils.bitmapToMat(catBitmap, catMat);
        Utils.bitmapToMat(pkqBitmap, pkqMat);

        List<Mat> list = new ArrayList<>();
        Core.split(pkqMat,list);
        Mat dst = CvImageCompute.cvMix(catMat,pkqMat,list.get(3));

        Bitmap dstBitmap = Bitmap.createBitmap(catBitmap.getWidth(), catBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,dstBitmap);


        ImageView imageView = findViewById(R.id.image_view);
        imageView.setImageBitmap(dstBitmap);

    }


    private Bitmap decodeResource(Resources res, int id) {
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
