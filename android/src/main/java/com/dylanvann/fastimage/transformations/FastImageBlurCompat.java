package com.dylanvann.fastimage.transformations;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.ImageView;

public class FastImageBlurCompat {
    /**
     * For Android API >= 31, blur is applied using RenderEffect directly on the View.
     * For Android API < 31, blur is applied using RenderScript on the Bitmap.
     * This ensures compatibility with both legacy and future Android versions.
     */
    public static Bitmap blur(Context context, Bitmap bitmap, float radius, ImageView view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (radius <= 0) {
                FastImageBlurEffectEngine.reset(view);
                return bitmap;
            }

            return FastImageBlurEffectEngine.apply(bitmap, radius, view);
        } else {
            if (radius <= 0) {
                FastImageBlurScriptEngine.reset();
                return bitmap;
            }

            return FastImageBlurScriptEngine.apply(bitmap, radius, context);
        }
    }
}
