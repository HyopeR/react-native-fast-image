package com.dylanvann.fastimage;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;

import java.util.Map;

class FastImageBlurHelper {

    static RequestOptions transform(Context context, Map<String, Object> imageOptions, RequestOptions options) {
        if (imageOptions == null) return options;

        ImageView view = getImageView(imageOptions);
        int blurRadius = getInt(imageOptions.get("blurRadius"));
        boolean blurShouldClean = getBoolean(imageOptions.get("blurRadiusShouldClean"));

        if (blurRadius > 0) {
            return options.transform(new FastImageBlurTransformation(context, blurRadius, view));
        }

        if (blurShouldClean) {
            FastImageBlurTransformation.clean(view);
        }

        return options;
    }

    private static ImageView getImageView(Map<String, Object> options) {
        Object value = options.get("view");
        return (value instanceof ImageView) ? (ImageView) value : null;
    }

    private static int getInt(Object value) {
        return (value instanceof Number) ? ((Number) value).intValue() : 0;
    }

    private static boolean getBoolean(Object value) {
        return (value instanceof Boolean) && (Boolean) value;
    }
}

