package com.rubyhuntersky.gx.android;

import android.content.Context;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.R;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class AndroidHuman extends Human {
    public AndroidHuman(Context context) {
        super(context.getResources().getDimensionPixelSize(R.dimen.fingerTip),
              context.getResources().getDimensionPixelSize(R.dimen.readingText));
    }

    public AndroidHuman(float fingerPixels, float textPixels) {
        super(fingerPixels, textPixels);
    }
}
