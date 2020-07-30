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
        int type = CvType.CV_64FC4;
        if(base.channels() == 1){
            type = CvType.CV_64FC1;
        }else if(base.channels() == 1){
            type = CvType.CV_64FC3;
        }
        int baseType = base.type();
        int srcType = base.type();
        base.convertTo(base, type, 1.0 / 255);
        src.convertTo(src, type, 1.0 / 255);
        Mat dst = new Mat();
        Core.multiply(base,src,dst);
        dst.convertTo(dst, baseType, 255);
        base.convertTo(base, baseType, 255);
        src.convertTo(src, srcType, 255);
        return dst;
    }

    public static Mat cvMix(Mat x,Mat y,Mat a){
        if(x.channels() != y.channels()){
            return null;
        }
        Mat yGray = new Mat();
        if(a.channels() == 1){
            a.copyTo(yGray);
        }else if(a.channels() == 3){
            Imgproc.cvtColor(a,yGray, Imgproc.COLOR_BGR2GRAY);
        }else{
            Imgproc.cvtColor(a,yGray, Imgproc.COLOR_BGRA2GRAY);
        }
        Mat xGray = new Mat();
        //1−a
        Core.bitwise_not(yGray,xGray);

        Mat xA = new Mat();
        Mat yA = new Mat();
        if(x.channels() == 1){
            xGray.copyTo(xA);
            yGray.copyTo(yA);
        }else if(x.channels() == 3){
            Imgproc.cvtColor(xGray,xA, Imgproc.COLOR_GRAY2BGR);
            Imgproc.cvtColor(yGray,yA, Imgproc.COLOR_GRAY2BGR);
        }else{
            Imgproc.cvtColor(xGray,xA, Imgproc.COLOR_GRAY2BGRA);
            Imgproc.cvtColor(yGray,yA, Imgproc.COLOR_GRAY2BGRA);
        }
        //x⋅(1−a)
        Mat matX = CvImageCompute.cvMultiply(x,xA);
        //y⋅a
        Mat matY = CvImageCompute.cvMultiply(y,yA);
        //x⋅(1−a)+y⋅a
        Mat dst = new Mat();
        Core.add(matX,matY,dst);
        return dst;
    }

}
