package com.rubyhuntersky.gx;

import android.support.annotation.NonNull;

import com.rubyhuntersky.coloret.Coloret;
import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.basics.Range;
import com.rubyhuntersky.gx.basics.ShapeSize;
import com.rubyhuntersky.gx.basics.TextSize;
import com.rubyhuntersky.gx.basics.TextStyle;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.devices.poles.SeedPole;
import com.rubyhuntersky.gx.internal.patches.Patch;
import com.rubyhuntersky.gx.internal.shapes.Shape;
import com.rubyhuntersky.gx.reactions.Reaction;
import com.rubyhuntersky.gx.uis.divs.Div;
import com.rubyhuntersky.gx.uis.divs.Div0;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.rubyhuntersky.coloret.Coloret.GREEN;
import static com.rubyhuntersky.gx.basics.Sizelet.pixels;
import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class Div0UnitTest {

    private Human human;
    private Pole pole;
    private ArrayList<Frame> frames;
    private Frame lastFrame;
    private Div0 padTopUi;
    private Div.Presentation presentation;
    private RecordingObserver recordingObserver;

    @Before
    public void setUp() throws Exception {
        human = new Human(17, 13);
        frames = new ArrayList<>();
        pole = new SeedPole(100, 27, 5) {
            @NonNull
            @Override
            public Patch addPatch(@NonNull final Frame frame, @NonNull Shape shape, int argbColor) {
                frames.add(frame);
                lastFrame = frame;
                return new Patch() {
                    @Override
                    public void remove() {
                        frames.remove(frame);
                    }
                };
            }

            @NonNull
            @Override
            public TextSize measureText(@NonNull String text, @NonNull TextStyle textStyle) {
                return TextSize.ZERO;
            }

            @NonNull
            @Override
            public ShapeSize measureShape(@NonNull Shape shape) {
                return ShapeSize.ZERO;
            }
        };
        padTopUi = Gx.INSTANCE.colorColumn(pixels(10), Coloret.BLACK).padTop(pixels(15));
        presentation = Div.CancelledPresentation.INSTANCE;
        recordingObserver = new RecordingObserver();
    }

    @After
    public void tearDown() throws Exception {
        presentation.cancel();
    }

    @Test
    public void expandVertical_increasesHeight() throws Exception {
        final Div0 verticalExpansion = Gx.INSTANCE.colorColumn(pixels(17), Coloret.BLACK).expandVertical(pixels(5));
        presentation = verticalExpansion.present(human, pole, recordingObserver);
        assertEquals(27, recordingObserver.lastHeight, .0001);
    }

    @Test
    public void expandVertical_movesFrameDown() throws Exception {
        final Div0 verticalExpansion = Gx.INSTANCE.colorColumn(pixels(17), Coloret.BLACK).expandVertical(pixels(5));
        presentation = verticalExpansion.present(human, pole, recordingObserver);
        Range vertical = frames.get(0).getVertical();
        assertEquals(5, vertical.getStart(), .0001);
        assertEquals(22, vertical.getEnd(), .0001);
    }

    @Test
    public void padTop_addsPaddingToFrameTop() throws Exception {
        final Div.Presentation present = padTopUi.present(human, pole, recordingObserver);
        present.cancel();
        assertEquals(15, lastFrame.getVertical().getStart(), .001);
    }

    @Test
    public void padTop_addsPaddingToHeight() throws Exception {
        final Div.Presentation present = padTopUi.present(human, pole, recordingObserver);
        present.cancel();
        assertEquals(25, recordingObserver.lastHeight, .001);
    }

    @Test
    public void expandBottomWithColumn_expandsPresentationHeight() throws Exception {
        final Div0 expandBottomWithColumn = Gx.INSTANCE.colorColumn(pixels(10), Coloret.BLACK)
              .expandDown(Gx.INSTANCE.colorColumn(pixels(5), GREEN));

        presentation = expandBottomWithColumn.present(human, pole, recordingObserver);
        assertEquals(15, recordingObserver.lastHeight, .001);
    }

    @Test
    public void expandBottomWithColumn_movesExpansionFrameDown() throws Exception {
        final Gx gx = Gx.INSTANCE;

        final Div0 expandBottomWithColumn = gx.gapColumn(pixels(10)).expandDown(gx.colorColumn(pixels(5), GREEN));
        presentation = expandBottomWithColumn.present(human, pole, recordingObserver);
        assertEquals(10, frames.get(0).getVertical().getStart(), .001);
    }

    @Test
    public void presentation_takesWidthFromColumn() throws Exception {
        final Div0 div0 = Div0.create(new Div.OnPresent() {
            @Override
            public void onPresent(@NonNull Div.Presenter presenter) {
                // Do nothing.
            }
        });
        final Div.Presentation presentation = div0.present(human, pole, recordingObserver);
        assertEquals(100, pole.getWidth(), .001);
    }

    static class RecordingObserver implements Div.Observer {
        public float lastHeight;
        public Reaction lastReaction;
        public Throwable lastError;

        @Override
        public void onHeight(float height) {
            this.lastHeight = height;
        }

        @Override
        public void onReaction(@NonNull Reaction reaction) {
            this.lastReaction = reaction;
        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            this.lastError = throwable;
        }
    }
}