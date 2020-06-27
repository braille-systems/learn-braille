package com.github.braillesystems.learnbraille.data.entities

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrailleDotsTest {

    @Test
    fun spelling() {
        assertEquals(
            "1, 3, 6",
            BrailleDots(b1 = BrailleDot.F, b3 = BrailleDot.F, b6 = BrailleDot.F).spelling
        )
    }
}
