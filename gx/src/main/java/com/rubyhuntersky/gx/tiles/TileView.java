package com.rubyhuntersky.gx.tiles;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.rubyhuntersky.gx.client.Observer;
import com.rubyhuntersky.gx.R;
import com.rubyhuntersky.gx.client.Human;
import com.rubyhuntersky.gx.client.Presentation;
import com.rubyhuntersky.gx.devices.mosaics.Mosaic;
import com.rubyhuntersky.gx.material.PatchDeviceView;

/**
 * @author wehjin
 * @since 2/13/16.
 */

public class TileView extends PatchDeviceView {

    private Human human;
    private Presentation presentation;

    public TileView(Context context) {
        super(context);
        initTileView();
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTileView();
    }

    public TileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTileView();
    }

    private void initTileView() {
        final Resources resources = getResources();
        this.human = new Human(resources.getDimensionPixelSize(R.dimen.fingerTip),
                               resources.getDimensionPixelSize(R.dimen.readingText));
        setLayoutParams(new ViewGroup.LayoutParams(0, 0));
    }

    public void present(@NonNull Tile0 tile) {
        if (presentation != null) {
            presentation.cancel();
            presentation = null;
        }
        presentation = tile.present(human, new Mosaic(0, 0, 0, this), Observer.EMPTY);
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = (int) presentation.getWidth();
        layoutParams.height = (int) presentation.getHeight();
        setLayoutParams(layoutParams);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (presentation == null) {
            setMeasuredDimension(0, 0);
            return;
        }

        setMeasuredDimension((int) presentation.getWidth(), (int) presentation.getHeight());
    }
}
