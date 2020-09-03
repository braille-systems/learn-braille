package com.github.braillesystems.learnbraille.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KotlinTest {

    @Test
    fun pairCompareTo() {
        assertTrue((0 to 0) == (0 to 0))
        assertTrue((1 to 0) > (0 to 0))
        assertTrue((0 to 1) > (0 to 0))
        assertTrue((1 to 2) < (2 to 1))
        assertTrue((0 to 0) <= (0 to 0))
        assertTrue((1 to 2) > (0 to 0))
    }

    @Test
    fun retryNTest() {
        fun getNextNumber(currentNumber: Int, nextNumbers: Array<Int>, repeat: Int): Int? {
            var i = 0
            fun getNextNumber(): Int {
                return nextNumbers[i++]
            }

            fun ifNumberSameAsCurrent(number: Int) = number == currentNumber

            return retryN(repeat, { !ifNumberSameAsCurrent(it) }, { getNextNumber() })
        }

        // Find first number in a sequence different from specified number
        assertEquals(1, getNextNumber(0, arrayOf(1, 1, 1), 2))
        assertEquals(2, getNextNumber(1, arrayOf(1, 2, 3), 2))
        assertEquals(2, getNextNumber(1, arrayOf(1, 2, 3), 3))
        assertEquals(null, getNextNumber(1, arrayOf(1, 2, 3), 1))
        assertEquals(5, getNextNumber(0, arrayOf(0, 0, 0, 5, 1, 0, 4), 4))
    }
}
