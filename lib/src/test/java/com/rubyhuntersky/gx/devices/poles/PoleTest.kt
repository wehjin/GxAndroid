package com.rubyhuntersky.gx.devices.poles

import com.rubyhuntersky.gx.internal.screen.Screen
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * @author Jeffrey Yu
 * *
 * @since 6/8/16.
 */

class PoleTest {
    @Test
    fun testWithRelatedHeight_createsSamePole_whenNewHeightIsIdentical() {
        val screen = mock(Screen::class.java)
        val pole = Pole(1f, 2f, 3, screen)
        val nextPole = pole.withRelatedHeight(2f)
        assertEquals(pole, nextPole)
    }
}