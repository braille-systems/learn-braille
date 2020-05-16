package com.github.braillesystems.learnbraille.utils

/**
 * The file contains suitable extension functions for kotlin
 * that are not specific for particular project.
 */

inline fun <T, R> T?.side(block: (T) -> R) {
    if (this != null) block(this)
}

operator fun MatchGroupCollection.component1() = get(0)
operator fun MatchGroupCollection.component2() = get(1)
operator fun MatchGroupCollection.component3() = get(2)
operator fun MatchGroupCollection.component4() = get(3)
operator fun MatchGroupCollection.component5() = get(4)

val Any?.devnull: Unit get() {}

fun String.removeHtmlMarkup() = Regex("""<[^>]*>""").replace(this, "")

inline fun executeIf(cond: Boolean, block: () -> Unit) {
    if (cond) block()
}

inline fun <T> T?.executeIf(cond: Boolean, block: T.() -> Unit) {
    if (this != null && cond) block()
}
