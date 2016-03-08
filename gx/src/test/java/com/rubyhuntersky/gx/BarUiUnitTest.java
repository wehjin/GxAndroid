package com.rubyhuntersky.gx;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.bars.Bar;
import com.rubyhuntersky.gx.bars.BarUi;
import com.rubyhuntersky.gx.bars.FullBar;
import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.basics.ShapeSize;
import com.rubyhuntersky.gx.basics.TextSize;
import com.rubyhuntersky.gx.basics.TextStyle;
import com.rubyhuntersky.gx.patches.Patch;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.presenters.OnPresent;
import com.rubyhuntersky.gx.presenters.Presenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.rubyhuntersky.gx.Creator.colorBar;
import static com.rubyhuntersky.coloret.Coloret.BLACK;
import static com.rubyhuntersky.coloret.Coloret.GREEN;
import static com.rubyhuntersky.gx.basics.Sizelet.pixels;
import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class BarUiUnitTest {

    private Human human;
    private Bar bar;
    private ArrayList<Frame> frames;
    private Presentation presentation;

    @Before
    public void setUp() throws Exception {
        human = new Human(17, 13);
        frames = new ArrayList<>();
        bar = new FullBar(100, 27, 5) {

            @NonNull
            @Override
            public Patch addPatch(Frame frame, Shape shape) {
                frames.add(frame);
                return Patch.EMPTY;
            }

            @NonNull
            @Override
            public TextSize measureText(String text, TextStyle textStyle) {
                return TextSize.ZERO;
            }

            @NonNull
            @Override
            public ShapeSize measureShape(Shape shape) {
                return ShapeSize.ZERO;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        if (presentation != null) {
            presentation.cancel();
            presentation = null;
        }
    }

    @Test
    public void expandStart_movesEndFrame() throws Exception {
        final BarUi ui = colorBar(BLACK, pixels(30)).expandStart(colorBar(GREEN, pixels(50)));
        presentation = ui.present(human, bar, Observer.EMPTY);
        assertEquals(50, frames.get(1).horizontal.start, .0001);
    }

    @Test
    public void expandStart_combinesWidths() throws Exception {
        final BarUi ui = colorBar(BLACK, pixels(30)).expandStart(colorBar(GREEN, pixels(50)));
        presentation = ui.present(human, bar, Observer.EMPTY);
        assertEquals(80, presentation.getWidth(), .0001);
    }

    @Test
    public void padStart_movesFrame() throws Exception {
        final BarUi padStartUi = colorBar(BLACK, pixels(30)).padStart(pixels(10));
        presentation = padStartUi.present(human, bar, Observer.EMPTY);
        assertEquals(10, frames.get(0).horizontal.start, .0001);
    }

    @Test
    public void padStart_increasesWidth() throws Exception {
        final BarUi padStartUi = colorBar(BLACK, pixels(30)).padStart(pixels(10));
        presentation = padStartUi.present(human, bar, Observer.EMPTY);
        assertEquals(40, presentation.getWidth(), .0001);
    }

    @Test
    public void presentation_takesHeightFromBar() throws Exception {
        final BarUi barUi = BarUi.create(new OnPresent<Bar>() {
            @Override
            public void onPresent(Presenter<Bar> presenter) {
                // Do nothing.
            }
        });
        final Presentation presentation = barUi.present(human, bar, Observer.EMPTY);
        assertEquals(100, presentation.getHeight(), .001);
    }
}