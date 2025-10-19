package com.dylanvann.fastimage.blur;

import android.graphics.Bitmap;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

@RequiresApi(31)
public class FastImageRenderEffectEngine {
    private static final float BLUR_REFERENCE_WIDTH = 1080f;
    private static final float BLUR_MIN_INPUT = 0.1F;
    private static final float BLUR_MAX_INPUT = 200f;

    /**
     * Scales the image and blurs it with RenderEffect.
     */
    public static Bitmap apply(Bitmap src, float radius, ImageView view) {
        float scale = src.getWidth() / BLUR_REFERENCE_WIDTH;
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
}
