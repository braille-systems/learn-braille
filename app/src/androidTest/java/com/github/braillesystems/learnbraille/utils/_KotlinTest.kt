package com.github.braillesystems.learnbraille.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class KotlinTest {

    private fun getNextNumber(currentNumber: Int, nextNumbers: Array<Int>, repeat: Int): Int? {
        var i = 0
        fun getNextNumber(): Int {
            return nextNumbers[i++]
        }

        fun ifNumberSameAsCurrent(number: Int) = number == currentNumber

        return tryN(repeat, { !ifNumberSameAsCurrent(it) }, { getNextNumber() })
    }

    @Test
    fun tryNTest() {
        // testing how tryN helps to find first number in a sequence different from specified number
        assertEquals(1, getNextNumber(0, arrayOf(1, 1, 1), 2))
        assertEquals(2, getNextNumber(1, arrayOf(1, 2, 3), 2))
        assertEquals(2, getNextNumber(1, arrayOf(1, 2, 3), 3))
        assertEquals(null, getNextNumber(1, arrayOf(1, 2, 3), 1))
        assertEquals(5, getNextNumber(0, arrayOf(0, 0, 0, 5, 1, 0, 4), 4))

    }
}
