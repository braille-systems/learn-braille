package com.github.braillesystems.learnbraille.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class _KotlinTest {

    @Test
    fun chainifyTest() {
        run {
            val ans = mutableListOf<Pair<Int, Int>>()
            chainify<Int> { a, b -> ans += a to b }
            assertEquals(listOf<Pair<Int, Int>>(), ans)
        }
        run {
            val ans = mutableListOf<Pair<Int, Int>>()
            chainify(1) { a, b -> ans += a to b }
            assertEquals(listOf<Pair<Int, Int>>(), ans)
        }
        run {
            val ans = mutableListOf<Pair<Int, Int>>()
            chainify(1, 2) { a, b -> ans += a to b }
            assertEquals(listOf(1 to 2), ans)
        }
        run {
            val ans = mutableListOf<Pair<Int, Int>>()
            chainify(1, 2, 3) { a, b -> ans += a * 10 to b * 100 }
            assertEquals(listOf(10 to 200, 20 to 300), ans)
        }
    }

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
        fun getNextNumber(currentNumber: Int, nextNumbers: IntArray, repeat: Int): Int? {
            var i = 0
            fun getNextNumber(): Int {
                return nextNumbers[i++]
            }

            fun ifNumberSameAsCurrent(number: Int) = number == currentNumber

            return retryN(repeat, { !ifNumberSameAsCurrent(it) }, { getNextNumber() })
        }

        // Find first number in a sequence different from specified number
        assertEquals(1, getNextNumber(0, intArrayOf(1, 1, 1), 2))
        assertEquals(2, getNextNumber(1, intArrayOf(1, 2, 3), 2))
        assertEquals(2, getNextNumber(1, intArrayOf(1, 2, 3), 3))
        assertEquals(null, getNextNumber(1, intArrayOf(1, 2, 3), 1))
        assertEquals(5, getNextNumber(0, intArrayOf(0, 0, 0, 5, 1, 0, 4), 4))
    }
}
