package com.github.braillesystems.learnbraille.utils

import kotlin.reflect.KProperty

/**
 * The file contains suitable extension functions for kotlin
 * that are not specific for particular project.
 */

inline fun <T, R> T?.side(block: (T) -> R) {
    if (this != null) block(this)
}

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

fun <T, R> Iterable<Rule<T, R>>.match(key: T): R? = matchF(key)?.invoke(key)

operator fun <T, R> Rules<T, R>.get(x: T): R? = match(x)

/**
 * Is very useful to choose android text resource depending on some condition.
 * (In that case prevent lambda of capturing context that will be invalid next time fragment entered,
 * so use `Fragment.getString` outside of (...) -> String lambdas.)
 * ```
 * val Context.inputSymbolPrintRules by rules<Context, Char, String>(
 *     {
 *         val t = getString(R.string.input_letter_intro_template)
 *         ruSymbols.map::containsKey to { c: Char -> t.format(c) }
 *     },
 *
 *     {
 *         val t = getString(R.string.input_digit_intro_template)
 *         uebDigits.map::containsKey to { c: Char -> t.format(c) }
 *     },
 *
 *     {
 *         val other = getString(R.string.input_special_intro_template)
 *         val numSign = getString(R.string.input_special_intro_num_sign)
 *         val dotIntro = getString(R.string.input_special_intro_dot)
 *         val commaIntro = getString(R.string.input_special_intro_comma)
 *         val hyphenIntro = getString(R.string.input_special_intro_hyphen)
 *         specialSymbols.map::containsKey to { c: Char ->
 *             when (c) {
 *                 ']' -> numSign
 *                 '.' -> dotIntro
 *                 ',' -> commaIntro
 *                 '-' -> hyphenIntro
 *                 else -> other.format(c)
 *             }
 *         }
 *     }
 * )
 * ```
 */
class rules<C, T, R>(private vararg val ruleProviders: C.() -> Rule<T, R>) {
    private var value: Rules<T, R>? = null
    operator fun getValue(thisRef: C, property: KProperty<*>): Rules<T, R> =
        value ?: ruleProviders
            .map { thisRef.it() }
            .also { value = it }
}

class lazyWithContext<C, R>(private val getter: C.(KProperty<*>) -> R) {
    private var value: R? = null
    operator fun getValue(thisRef: C, property: KProperty<*>): R =
        value ?: thisRef.getter(property).also { value = it }
}
