package com.github.braillesystems.learnbraille.data.entities

import org.junit.Assert.assertEquals
import org.junit.Test

class BrailleDotsTest {

    @Test
    fun spelling() {
        assertEquals(
            "1, 3, 6",
            BrailleDots(b1 = BrailleDot.F, b3 = BrailleDot.F, b6 = BrailleDot.F).spelling
        )
    }
}
