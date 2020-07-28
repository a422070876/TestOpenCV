package com.hyq.hm.test.opencv;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.List;

public class CvImageCompute {

    static {
        System.loadLibrary("native-lib");
    }

    public static Mat cvMultiply(Mat base, Mat src){
        if(base.channels() != src.channels()){
            return null;
        }
        int baseType = base.type();
        int srcType = base.type();
        base.convertTo(base, CvType.CV_64FC4, 1.0 / 255);
        src.convertTo(src, CvType.CV_64FC4, 1.0 / 255);
        Mat dst = new Mat();
        Core.multiply(base,src,dst);
        dst.convertTo(dst, baseType, 255);
        base.convertTo(base, baseType, 255);
        src.convertTo(src, srcType, 255);
        return dst;
    }
}
