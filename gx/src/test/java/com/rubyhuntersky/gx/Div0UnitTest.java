package com.rubyhuntersky.gx;

import android.support.annotation.NonNull;

import com.rubyhuntersky.coloret.Coloret;
import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.basics.ShapeSize;
import com.rubyhuntersky.gx.basics.TextSize;
import com.rubyhuntersky.gx.basics.TextStyle;
import com.rubyhuntersky.gx.client.Human;
import com.rubyhuntersky.gx.client.Observer;
import com.rubyhuntersky.gx.client.Presentation;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.divs.Div0;
import com.rubyhuntersky.gx.devices.poles.SeedPole;
import com.rubyhuntersky.gx.internal.patches.Patch;
import com.rubyhuntersky.gx.internal.shapes.Shape;
import com.rubyhuntersky.gx.internal.presenters.OnPresent;
import com.rubyhuntersky.gx.internal.presenters.Presenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.rubyhuntersky.gx.client.Gx.colorColumn;
import static com.rubyhuntersky.gx.client.Gx.gapColumn;
import static com.rubyhuntersky.gx.basics.Sizelet.pixels;
import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class Div0UnitTest {

    private Human human;
    private Pole pole;
    private ArrayList<Frame> frames;
    private Div0 padTopUi;
    private Presentation presentation;

    @Before
    public void setUp() throws Exception {
        human = new Human(17, 13);
        frames = new ArrayList<>();
        pole = new SeedPole(100, 27, 5) {
            @NonNull
            @Override
            public Patch addPatch(Frame frame, Shape shape, int argbColor) {
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
        padTopUi = colorColumn(pixels(10), Coloret.BLACK).padTop(pixels(15));
        presentation = Presentation.EMPTY;
    }

    @After
    public void tearDown() throws Exception {
        presentation.cancel();
    }

    @Test
    public void expandVertical_increasesHeight() throws Exception {
        final Div0 verticalExpansion = colorColumn(pixels(17), Coloret.BLACK).expandVertical(pixels(5));
        presentation = verticalExpansion.present(human, pole, Observer.EMPTY);
        assertEquals(27, presentation.getHeight(), .0001);
    }

    @Test
    public void expandVertical_movesFrameDown() throws Exception {
        final Div0 verticalExpansion = colorColumn(pixels(17), Coloret.BLACK).expandVertical(pixels(5));
        presentation = verticalExpansion.present(human, pole, Observer.EMPTY);
        assertEquals(5, frames.get(0).vertical.start, .0001);
        assertEquals(22, frames.get(0).vertical.end, .0001);
    }

    @Test
    public void padTop_addsPaddingToFrameTop() throws Exception {
        final Presentation present = padTopUi.present(human, pole, Observer.EMPTY);
        present.cancel();
        assertEquals(15, frames.get(0).vertical.start, .001);
    }

    @Test
    public void padTop_addsPaddingToHeight() throws Exception {
        final Presentation present = padTopUi.present(human, pole, Observer.EMPTY);
        final float height = present.getHeight();
        present.cancel();
        assertEquals(25, height, .001);
    }

    @Test
    public void expandBottomWithColumn_expandsPresentationHeight() throws Exception {
        final Div0 expandBottomWithColumn = colorColumn(pixels(10), Coloret.BLACK)
              .expandDown(colorColumn(pixels(5), Coloret.GREEN));

        presentation = expandBottomWithColumn.present(human, pole, Observer.EMPTY);
        final float height = presentation.getHeight();
        assertEquals(15, height, .001);
    }

    @Test
    public void expandBottomWithColumn_movesExpansionFrameDown() throws Exception {
        final Div0 expandBottomWithColumn = gapColumn(pixels(10))
              .expandDown(colorColumn(pixels(5), Coloret.GREEN));

        presentation = expandBottomWithColumn.present(human, pole, Observer.EMPTY);
        assertEquals(10, frames.get(0).vertical.start, .001);
    }

    @Test
    public void presentation_takesWidthFromColumn() throws Exception {
        final Div0 div0 = Div0.create(new OnPresent<Pole>() {
            @Override
            public void onPresent(Presenter<Pole> presenter) {
                // Do nothing.
            }
        });
        final Presentation presentation = div0.present(human, pole, Observer.EMPTY);
        assertEquals(100, presentation.getWidth(), .001);
    }
}