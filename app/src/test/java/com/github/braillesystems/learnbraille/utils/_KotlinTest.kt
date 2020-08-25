package com.github.braillesystems.learnbraille.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class KotlinTest {

    private fun getNextNumber(currentNumber: Int, nextNumbers: IntArray, repeat: Int): Int? {
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
        assertEquals(1, getNextNumber(0, intArrayOf(1, 1, 1), 2))
        assertEquals(2, getNextNumber(1, intArrayOf(1, 2, 3), 2))
        assertEquals(2, getNextNumber(1, intArrayOf(1, 2, 3), 3))
        assertEquals(null, getNextNumber(1, intArrayOf(1, 2, 3), 1))
        assertEquals(5, getNextNumber(0, intArrayOf(0, 0, 0, 5, 1, 0, 4), 4))

    }
}
