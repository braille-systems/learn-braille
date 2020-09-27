package com.github.braillesystems.learnbraille.utils

import java.util.*
import kotlin.reflect.KProperty

/**
 * The file contains suitable extension functions for kotlin
 * that are not specific for particular project.
 */

inline fun <T> T?.side(block: (T) -> Unit) {
    if (this != null) block(this)
}

inline fun <T> forEach(vararg xs: T, block: (T) -> Unit) = xs.forEach(block)
inline fun <T> applyForEach(vararg xs: T, block: T.() -> Unit) = xs.forEach { it.block() }

/**
 * Try to use sealed classes to avoid using `unreachable`.
 */
val unreachable: Nothing
    get() = error("Unreachable code executed")

operator fun MatchGroupCollection.component1() = get(0)
operator fun MatchGroupCollection.component2() = get(1)
operator fun MatchGroupCollection.component3() = get(2)
operator fun MatchGroupCollection.component4() = get(3)
operator fun MatchGroupCollection.component5() = get(4)

operator fun <A, B> Pair<A, B>.compareTo(other: Pair<A, B>): Int
        where A : Comparable<A>, B : Comparable<B> =
    when {
        first < other.first -> -1
        first == other.first && second < other.second -> -1
        equals(other) -> 0
        else -> 1
    }

val Any?.devnull: Unit get() {}

fun String.removeHtmlMarkup() = Regex("""<[^>]*>|&""").replace(this, "")

inline fun runIf(cond: Boolean, block: () -> Unit) {
    if (cond) block()
}

inline fun <T> T?.runIf(cond: Boolean, block: T.() -> Unit) {
    if (this != null && cond) block()
}

typealias Rule <T, R> = Pair<(T) -> Boolean, (T) -> R>
typealias Rules <T, R> = Iterable<Rule<T, R>>

fun <T, R> Rules<T, R>.matchF(key: T): ((T) -> R)? {
    forEach { (p, f) -> if (p(key)) return f }
    return null
}

fun <T, R> Rules<T, R>.match(key: T): R? = matchF(key)?.invoke(key)

operator fun <T, R> Rules<T, R>.get(x: T): R? = match(x)

fun <T, R> Rules<T, R>.getValue(x: T): R = match(x) ?: error("No rule match value $x")

/**
 * It is very useful to choose android text resource depending on some condition.
 * (In that case prevent lambda of capturing context that will be invalid next time fragment entered,
 * so use `Context::getString` outside of inner paired lambdas)
 * ```
 * val Context.inputSymbolPrintRules by rules<Context, Char, String>(
 *     {
 *         val t = getString(R.string.input_letter_intro_template)
 *         ruSymbols.map::containsKey to { c: Char -> t.format(c) }
 *     },
 *     {
 *         val t = getString(R.string.input_digit_intro_template)
 *         uebDigits.map::containsKey to { c: Char -> t.format(c) }
 *     }
 * )
 * ```
 */
class rules<C, T, R>(private vararg val ruleProviders: C.() -> Rule<T, R>) {
    private var value: Rules<T, R>? = null
    operator fun getValue(thisRef: C, property: KProperty<*>): Rules<T, R> =
        value
            ?: ruleProviders
                .map { thisRef.it() }
                .also { value = it }
}

class lazyWithContext<C, R>(private val getter: C.(KProperty<*>) -> R) {
    private var value: R? = null
    operator fun getValue(thisRef: C, property: KProperty<*>): R =
        value
            ?: thisRef
                .getter(property)
                .also { value = it }
}

class Days(val v: Int) {
    operator fun unaryMinus(): Days = Days(-v)
}

operator fun Date.plus(days: Days): Date =
    GregorianCalendar()
        .apply {
            time = this@plus
            add(Calendar.DATE, days.v)
        }
        .time

operator fun Date.minus(days: Days): Date = plus(-days)

inline fun <T : Any> retryN(n: Int, get: () -> T?): T? {
    repeat(n) {
        get()?.let { return it }
    }
    return null
}
