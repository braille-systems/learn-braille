package com.github.braillesystems.learnbraille.utils

import org.junit.Assert.*
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
        // Last try will succeed
        fun getTrier(n: Int): () -> Int? {
            var i = 0
            return {
                ++i
                if (i == n) n
                else null
            }
        }

        assertNull(retryN(-1) { 1 })
        assertNull(retryN(0) { 1 })
        run {
            val trier = getTrier(5)
            assertNull(retryN(4, trier))
        }
        run {
            val trier = getTrier(5)
            assertEquals(5, retryN(5, trier))
        }
        run {
            val trier = getTrier(5)
            assertEquals(5, retryN(10, trier))
        }
    }
}
