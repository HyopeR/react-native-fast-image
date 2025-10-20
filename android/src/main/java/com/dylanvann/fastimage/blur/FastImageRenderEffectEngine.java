package com.dylanvann.fastimage.blur;

import android.graphics.Bitmap;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

@RequiresApi(31)
public class FastImageRenderEffectEngine {
    private static final float BLUR_REFERENCE_SIZE = 540f;
    private static final float BLUR_MIN_INPUT = 0.1F;
    private static final float BLUR_MAX_INPUT = 200f;
    private static final int LISTENER_TAG_ID = 0xdeadbeef;
    private static final int BITMAP_TAG_ID = 0xcafebabe;

    /**
     * Scales the image and blurs it with RenderEffect.
     */
    public static Bitmap apply(Bitmap src, float radius, ImageView view) {
        ensureDynamicApply(src, radius, view);

        float scaleFactorX = src.getWidth() / BLUR_REFERENCE_SIZE;
        float scaleFactorY = src.getHeight() / BLUR_REFERENCE_SIZE;
        float scaleX = view.getWidth() * scaleFactorX / src.getWidth();
        float scaleY = view.getHeight() * scaleFactorY / src.getHeight();
        float scale = (scaleX + scaleY) / 2f;

        float radiusScaled = radius * scale;
        float radiusNormalized = Math.max(BLUR_MIN_INPUT, Math.min(BLUR_MAX_INPUT, radiusScaled));
        return blur(src, radiusNormalized, view);
    }

    /**
     * Used to create blur with RenderEffect.
     */
    static Bitmap blur(Bitmap src, float radius, ImageView view) {
        RenderEffect blur = RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.CLAMP);
        view.setRenderEffect(blur);
        view.invalidate();
        return src;
    }

    /**
     * RenderEffect only applies the blur effect to the View layer.
     * It must be reapplied when the dimensions change.
     */
    private static void ensureDynamicApply(Bitmap src, float radius, ImageView view) {
        Object tag = view.getTag(LISTENER_TAG_ID);
        if (tag instanceof Boolean && (Boolean) tag) return;

        view.setTag(BITMAP_TAG_ID, src);
        view.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (right - left != oldRight - oldLeft || bottom - top != oldBottom - oldTop) {
                Object bitmapTag = view.getTag(BITMAP_TAG_ID);
                if (bitmapTag instanceof Bitmap originalBitmap) {
                    apply(originalBitmap, radius, view);
                }
            }
        });

        view.setTag(LISTENER_TAG_ID, true);
    }
}
