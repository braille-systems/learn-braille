package com.github.braillesystems.learnbraille.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.serialization.Serializable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@Serializable
sealed class S

@Serializable
data class A(val s: String) : S()

@Serializable
data class B(val i: Long, val s: String) : S()

@RunWith(AndroidJUnit4::class)
class UtilsTest {

    @Test
    fun serializationBasic() {
        @Serializable
        data class D(val s: String, val i: Int)

        val d = D("Wow", 100500)
        assertEquals(d, parse(D.serializer(), stringify(D.serializer(), d)))
    }

    @Test
    fun serializationVirtual() {
        val s1: S = A("wow")
        assertEquals(s1 as A, parse(S.serializer(), stringify(S.serializer(), s1)))
        val s2: S = B(100500, "aaa")
        assertEquals(s2 as B, parse(S.serializer(), stringify(S.serializer(), s2)))
    }
}
