/**
 * Copyright 2016 smartbetter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.smartbetter.utilslibrary;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gc on 2017/2/21.
 */
public class BitmapUtils {

    private BitmapUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * bitmap to byte[]
     *
     * @param b
     * @param quality
     * @return
     */
    public static byte[] bitmapToByte(Bitmap b, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, quality, baos);
        return baos.toByteArray();
    }

    /**
     * byte[] to bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * bitmap to base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        return Base64.encodeToString(bitmapToByte(bitmap, 100), Base64.DEFAULT);
    }

    /**
     * base64  to bitmap
     *
     * @param base64Str
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Str) {
        byte[] bytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * drawable to bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }

    /**
     * bitmap to drawable
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * bitmap to uri
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Uri bitmapToUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    /**
     * 设置 inJustDecodeBounds 属性为true可以在解码的时候避免内存的分配，
     * 它会返回一个null的Bitmap，但是可以获取到 outWidth, outHeight 与 outMimeType
     *
     * @param showWidth
     * @param showHeight
     * @return
     */
    public static Bitmap getScaleBitmap(Resources res, int resId, int showWidth, int showHeight) {
        BitmapFactory.Options options = getOptions();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, showWidth, showHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap getScaleBitmap(String path, int showWidth, int showHeight) {
        BitmapFactory.Options options = getOptions();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, showWidth, showHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 对bitmap 进行缩放
     *
     * @param bm
     * @param showWidth
     * @param showHeight
     * @return
     */
    private static Bitmap getScaleBitmap(Bitmap bm, float showWidth, float showHeight) {
        if (bm == null) {
            return null;
        }
        if (showWidth <= 0 || showHeight <= 0) {
            return bm;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = showWidth / width;
        float scaleHeight = showHeight / height;
        float scale = scaleWidth >= scaleHeight ? scaleWidth : scaleHeight;
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postScale(scale, scale);
        bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return bm;
    }

    /**
     * 计算采样率
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private static BitmapFactory.Options getOptions() {
        return new BitmapFactory.Options();
    }

    /**
     * 翻转
     * (-1,1) 左右
     * (1,-1) 上下
     *
     * @param bmp
     * @param sx
     * @param sy
     * @return
     */
    public static Bitmap flipBitmap(Bitmap bmp, int sx, int sy) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);
        bmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
        return bmp;
    }

    /**
     * 旋转
     *
     * @param bitmap
     * @param degree
     * @return
     */
    public static Bitmap bitmapRotate(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }

    /**
     * 创建圆形图片
     *
     * @param source
     * @param size
     * @return
     */
    public static Bitmap createCircleImage(Bitmap source, int size) {
        source = getScaleBitmap(source, size, size);
        BitmapShader mBitmapShader = new BitmapShader(source, Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT);
        Bitmap dest = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        if (dest == null) {
            return null;
        }
        Canvas canvas = new Canvas(dest);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(mBitmapShader);
        canvas.drawCircle(size / 2, size / 2, size / 2, paint);
        return dest;
    }

    public static Bitmap getBitmapWith(InputStream inputStream, Rect rect) {
        try {
            BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = bitmapRegionDecoder.decodeRegion(rect, options);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
