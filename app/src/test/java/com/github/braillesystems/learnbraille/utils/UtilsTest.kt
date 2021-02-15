package com.github.braillesystems.learnbraille.utils

import kotlinx.serialization.Serializable
import org.junit.Assert.assertEquals
import org.junit.Test

@Serializable
sealed class S {
    abstract val s: String
}

@Serializable
data class A(override val s: String) : S()

@Serializable
data class B(val i: Long, override val s: String) : S()

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
        assertEquals("wow", s1.s)
    }

    @Test
    fun versionSerialization() {
        val v = Version(1, 2, 3)
        assertEquals(v, Version.valueOf(v.toString()))
        assertEquals(v, Version.valueOf("1.2.3"))
    }
}
