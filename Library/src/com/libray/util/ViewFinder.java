package com.libray.util;

import android.app.Activity;
import android.view.View;

/**
 * @author t77yq @2014-07-22.
 */
public final class ViewFinder {

    private ViewFinder() {}

    @SuppressWarnings("unchecked")
    public static <T extends View> T getView(View view, int id) {
        return (T) view.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T getView(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }
}
