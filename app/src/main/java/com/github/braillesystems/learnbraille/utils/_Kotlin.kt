package com.github.braillesystems.learnbraille.utils

import kotlin.reflect.KProperty

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

inline fun runIf(cond: Boolean, block: () -> Unit) {
    if (cond) block()
}

inline fun <T> T?.runIf(cond: Boolean, block: T.() -> Unit) {
    if (this != null && cond) block()
}


typealias Rule <T, R> = Pair<(T) -> Boolean, (T) -> R>

fun <T, R> Iterable<Rule<T, R>>.matchF(key: T): ((T) -> R)? {
    forEach { (p, f) -> if (p(key)) return f }
    return null
}

fun <T, R> Iterable<Rule<T, R>>.match(key: T): R? = matchF(key)?.invoke(key)


class rules<C, T, R>(private vararg val ruleProviders: C.() -> Rule<T, R>) {
    private var value: Rules<T, R>? = null
    operator fun getValue(thisRef: C, property: KProperty<*>): Rules<T, R> =
        value ?: Rules(ruleProviders.map { thisRef.it() }).also { value = it }
}

class Rules<T, R>(private val rules: Iterable<Rule<T, R>>) {
    operator fun get(x: T): R? = rules.match(x)
}


class lazyWithContext<C, R>(private val getter: C.(String) -> R) {
    private var value: R? = null
    operator fun getValue(thisRef: C, property: KProperty<*>): R =
        value ?: thisRef.getter(property.name).also { value = it }
}
