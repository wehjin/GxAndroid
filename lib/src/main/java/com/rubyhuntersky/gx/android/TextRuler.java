package com.rubyhuntersky.gx.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rubyhuntersky.gx.basics.TextHeight;
import com.rubyhuntersky.gx.basics.TextSize;
import com.rubyhuntersky.gx.basics.TextStyle;

import java.util.HashMap;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author wehjin
 * @since 2/11/16.
 */

public class TextRuler {

    public static final String TAG = TextRuler.class.getSimpleName();
    private final HashMap<Pair<Typeface, Integer>, TextHeight> textHeightCache = new HashMap<>();
    private TextView textView;

    public TextRuler(Context context) {
        textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        textView.setMinHeight(0);
        textView.setGravity(Gravity.TOP);
        textView.setBackgroundColor(Color.BLACK);
        textView.setTextColor(Color.BLUE);
        textView.setIncludeFontPadding(false);
    }

    public TextSize measure(String text, TextStyle textStyle) {
        final float textWidth = getTextWidth(text, textStyle);
        final TextHeight textHeight = getTextHeight(textStyle.typeface, textStyle.typeheight);
        return new TextSize(textWidth, textHeight);
    }

    private float getTextWidth(String textString, TextStyle textStyle) {
        TextView ruler = textView;
        ruler.setTypeface(textStyle.typeface);
        ruler.setTextSize(TypedValue.COMPLEX_UNIT_PX, textStyle.typeheight);
        ruler.setText(textString);
        ruler.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return ruler.getMeasuredWidth();
    }

    @NonNull
    private TextHeight getTextHeight(Typeface typeface, int typeheight) {
        final Pair<Typeface, Integer> typePair = new Pair<>(typeface, typeheight);
        if (textHeightCache.containsKey(typePair)) {
            return textHeightCache.get(typePair);
        }

        TextView textView = this.textView;
        textView.setText("E");
        textView.setTypeface(typeface);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, typeheight);
        textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final Bitmap bitmap = Bitmap.createBitmap(textView.getMeasuredWidth(), textView.getMeasuredHeight(),
                                                  Bitmap.Config.ARGB_8888);
        Log.d(TAG, "Bitmap: " + bitmap.getWidth() + ", " + bitmap.getHeight());
        textView.layout(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        textView.draw(canvas);
        final int rowCount = bitmap.getHeight();
        final int columnCount = bitmap.getWidth();
        int topRow = rowCount;
        int bottomRow = 0;
        int col = columnCount / 2;
        for (int row = 0; row < rowCount; row++) {
            final int pixel = bitmap.getPixel(col, row);
            if ((pixel & 0xff) > 128) {
                topRow = Math.min(topRow, row);
                bottomRow = Math.max(bottomRow, row);
            }
        }
        Log.d(TAG, "E limits: " + topRow + ", " + bottomRow);
        final float topPadding = topRow;
        final float height = Math.max(0, bottomRow - topRow + 1);
        final TextHeight textHeight = new TextHeight(height, topPadding);
        textHeightCache.put(typePair, textHeight);
        return textHeight;
    }
}
