package com.rubyhuntersky.gx.android;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.internal.devices.FixedDimensionDevice;
import com.rubyhuntersky.gx.observers.Observer;
import com.rubyhuntersky.gx.presentations.MultiDevicePresentation;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.uis.core.Ui0;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wehjin
 * @since 1/23/16.
 */

abstract public class FixedDimensionDeviceView<T extends FixedDimensionDevice<T>> extends ScreenView implements FixedDimensionDevice<T> {

    public final String TAG = getClass().getSimpleName();
    private Human human = new AndroidHuman(getContext());
    private T device;
    private MultiDevicePresentation<T> multiDevicePresentation = new MultiDevicePresentation<>();
    private Ui0<T> ui;
    private Ui0<T> measuredUi;
    private Map<Integer, Integer> variableDimensions = new HashMap<>();

    public FixedDimensionDeviceView(Context context) {
        super(context);
        initUiView();
    }

    public FixedDimensionDeviceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUiView();
    }

    public FixedDimensionDeviceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUiView();
    }

    private void initUiView() {
        Log.d(TAG, "Human: " + human);
        setContentDescription(TAG);
    }

    public void clearVariableDimensions() {
        variableDimensions.clear();
    }

    public Presentation present(Ui0<T> ui, Observer observer) {
        multiDevicePresentation.cancel();
        this.ui = ui;
        final MultiDevicePresentation<T> presentation1 = ui == null
              ? new MultiDevicePresentation<T>()
              : new MultiDevicePresentation<T>(ui, human, device, observer) {
                  @Override
                  protected void onCancel() {
                      super.onCancel();
                      FixedDimensionDeviceView.this.ui = null;
                      requestLayout();
                  }
              };
        multiDevicePresentation = presentation1;
        requestLayout();
        return presentation1;
    }

    abstract protected void setMeasuredDimensionFromDeviceDimensions(float fixed, float variable);
    abstract protected float getVariableDimension(Presentation presentation);
    abstract protected float getFixedDimension(int width, int height);
    abstract protected int getFixedDimensionMeasureSpec(int widthMeasureSpec, int heightMeasureSpec);

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int fixedDimensionMeasureSpec = getFixedDimensionMeasureSpec(widthMeasureSpec, heightMeasureSpec);
        final int fixedDimensionMode = MeasureSpec.getMode(fixedDimensionMeasureSpec);
        if (fixedDimensionMode == MeasureSpec.UNSPECIFIED) {
            throw new IllegalStateException("Fixed-dimension must be specified");
        }

        int fixedDimension = MeasureSpec.getSize(fixedDimensionMeasureSpec);
        if (ui == null) {
            setMeasuredDimensionFromDeviceDimensions(fixedDimension, 0);
            return;
        }

        if (ui != measuredUi) {
            variableDimensions.clear();
            measuredUi = ui;
        }
        if (!variableDimensions.containsKey(fixedDimension)) {
            Log.d(TAG, "onMeasure widthSpec: " + MeasureSpec.toString(widthMeasureSpec) + ", heightSpec: " + MeasureSpec
                  .toString(heightMeasureSpec));
            final T device = withFixedDimension(fixedDimension).withDelay().toType();
            final Presentation presentation = ui.present(human, device, Observer.EMPTY);
            final float variableDimension = getVariableDimension(presentation);
            presentation.cancel();
            variableDimensions.put(fixedDimension, (int) variableDimension);
        }
        final int variableDimension = variableDimensions.get(fixedDimension);
        Log.d(TAG, "onMeasure setMeasureDimension fixed: " + fixedDimension + ", variable: " + variableDimension);
        setMeasuredDimensionFromDeviceDimensions(fixedDimension, variableDimension);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        Log.d(TAG, "onSizeChanged");

        final float fixedDimension = getFixedDimension(w, h);
        if (fixedDimension == getFixedDimension(oldWidth, oldHeight)) {
            Log.d(TAG, "no change in fixed dimension");
            return;
        }
        device = this.withFixedDimension(fixedDimension);
        // Patch views sometimes don't show up if beginPresentation is called directly.
        post(new Runnable() {
            @Override
            public void run() {
                multiDevicePresentation.setDevice(device);
            }
        });
    }
}
