package com.babcock.tictactoe.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by kevinbabcock on 7/17/16.
 */

public class DpUtils {

    private Resources resources;

    public DpUtils(Context context) {
        resources = context.getResources();
    }

    public float dpToPx(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
