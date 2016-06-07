package com.rubyhuntersky.gx.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rubyhuntersky.gx.R;
import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.basics.ShapeSize;
import com.rubyhuntersky.gx.basics.TextHeight;
import com.rubyhuntersky.gx.basics.TextSize;
import com.rubyhuntersky.gx.basics.TextStyle;
import com.rubyhuntersky.gx.internal.devices.patchdevice.PatchDevice;
import com.rubyhuntersky.gx.internal.patches.Patch;
import com.rubyhuntersky.gx.internal.shapes.RectangleShape;
import com.rubyhuntersky.gx.internal.shapes.Shape;
import com.rubyhuntersky.gx.internal.shapes.TextShape;
import com.rubyhuntersky.gx.internal.shapes.ViewShape;

/**
 * @author wehjin
 * @since 2/11/16.
 */

public class PatchDeviceView extends FrameLayout implements PatchDevice {

    public static final String TAG = PatchDeviceView.class.getSimpleName();
    private TextRuler textRuler;
    private ShapeRuler shapeRuler;
    private int elevationPixels;


    public PatchDeviceView(Context context) {
        super(context);
        initPatchDeviceView(context);
    }

    public PatchDeviceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPatchDeviceView(context);
    }

    public PatchDeviceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPatchDeviceView(context);
    }

    private void initPatchDeviceView(Context context) {
        textRuler = new TextRuler(context);
        shapeRuler = new ShapeRuler(context);
        elevationPixels = getResources().getDimensionPixelSize(R.dimen.elevationGap);
    }

    @NonNull
    @Override
    public TextSize measureText(@NonNull String text, @NonNull TextStyle textStyle) {
        return textRuler.measure(text, textStyle);
    }

    @NonNull
    @Override
    public ShapeSize measureShape(@NonNull Shape shape) {
        return shapeRuler.measure(shape);
    }

    @NonNull
    @Override
    public Patch addPatch(@NonNull Frame frame, @NonNull Shape shape, int argbColor) {
        if (shape instanceof RectangleShape) {
            return getRectanglePatch(frame, (RectangleShape) shape, argbColor);
        } else if (shape instanceof TextShape) {
            return getTextPatch(frame, (TextShape) shape);
        } else if (shape instanceof ViewShape) {
            final ViewShape viewShape = (ViewShape) shape;
            return getViewPatch(viewShape.createView(getContext()), frame, 0);
        } else {
            return Patch.EMPTY;
        }
    }

    @NonNull
    private Patch getRectanglePatch(Frame frame, RectangleShape rectangleShape, int argbColor) {
        final View view = new View(getContext());
        view.setBackgroundColor(argbColor);
        view.setContentDescription(String.format("Rectangle{%x}", argbColor));
        return getViewPatch(view, frame, 0);
    }

    @NonNull
    private Patch getTextPatch(Frame frame, TextShape textShape) {
        final TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.TOP);
        textView.setTextColor(textShape.textStyle.typecolor);
        textView.setTypeface(textShape.textStyle.typeface);
        textView.setTextSize(textShape.textStyle.typeheight);
        textView.setText(textShape.textString);
        textView.setIncludeFontPadding(false);
        final TextHeight textHeight = textShape.textSize.textHeight;
        Frame newFrame = frame.withVerticalShift(-textHeight.topPadding)
              .withVerticalLength(textHeight.topPadding + textHeight.height);
        return getViewPatch(textView, newFrame, textHeight.height / 2);
    }

    @NonNull
    private Patch getViewPatch(final View view, final Frame frame, float additionalHeight) {
        setElevation(view, frame);
        Log.d(TAG, "Add view: " + view + " frame: " + frame);
        addView(view, getPatchLayoutParams(frame, additionalHeight));
        return new Patch() {
            @Override
            public void remove() {
                Log.d(TAG, "Remove view: " + view + " frame: " + frame);
                PatchDeviceView.this.removeView(view);
            }
        };
    }

    @NonNull
    private LayoutParams getPatchLayoutParams(Frame frame, float additionalHeight) {
        final LayoutParams layoutParams = new FrameLayout.LayoutParams((int) frame.horizontal.toLength(),
                                                                       (int) (frame.vertical.toLength() + additionalHeight));
        layoutParams.leftMargin = (int) frame.horizontal.start;
        layoutParams.topMargin = (int) frame.vertical.start;
        return layoutParams;
    }


    private void setElevation(View view, Frame frame) {
        ViewCompat.setElevation(view, elevationPixels * frame.elevation);
    }
}
