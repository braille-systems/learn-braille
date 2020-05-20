package com.github.braillesystems.learnbraille.res

import android.content.Context
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.dsl.materials
import com.github.braillesystems.learnbraille.data.dsl.symbols
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.E
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.utils.P2F
import com.github.braillesystems.learnbraille.utils.lazyWithContext
import com.github.braillesystems.learnbraille.utils.listOfP2F


/**
 * Do not forget to register new types of symbols in `symbolTypeDisplayList` below.
 */
val content by materials {
    +ruSymbols
    +specialSymbols
    +uebDigits
}

object SymbolType {
    const val ru = "ru"
    const val special = "special"
    const val digit = "digit"
}

val ruSymbols by symbols(SymbolType.ru) {
    symbol(char = 'А', brailleDots = BrailleDots(F, E, E, E, E, E))
    symbol(char = 'Б', brailleDots = BrailleDots(F, F, E, E, E, E))
    symbol(char = 'В', brailleDots = BrailleDots(E, F, E, F, F, F))
    symbol(char = 'Г', brailleDots = BrailleDots(F, F, E, F, F, E))
    symbol(char = 'Д', brailleDots = BrailleDots(F, E, E, F, F, E))
    symbol(char = 'Е', brailleDots = BrailleDots(F, E, E, E, F, E))
    symbol(char = 'Ё', brailleDots = BrailleDots(F, E, E, E, E, F))
    symbol(char = 'Ж', brailleDots = BrailleDots(E, F, E, F, F, E))
    symbol(char = 'З', brailleDots = BrailleDots(F, E, F, E, F, F))
    symbol(char = 'И', brailleDots = BrailleDots(E, F, E, F, E, E))
    symbol(char = 'Й', brailleDots = BrailleDots(F, F, F, F, E, F))
    symbol(char = 'К', brailleDots = BrailleDots(F, E, F, E, E, E))
    symbol(char = 'Л', brailleDots = BrailleDots(F, F, F, E, E, E))
    symbol(char = 'М', brailleDots = BrailleDots(F, E, F, F, E, E))
    symbol(char = 'Н', brailleDots = BrailleDots(F, E, F, F, F, E))
    symbol(char = 'О', brailleDots = BrailleDots(F, E, F, E, F, E))
    symbol(char = 'П', brailleDots = BrailleDots(F, F, F, F, E, E))
    symbol(char = 'Р', brailleDots = BrailleDots(F, F, F, E, F, E))
    symbol(char = 'С', brailleDots = BrailleDots(E, F, F, F, E, E))
    symbol(char = 'Т', brailleDots = BrailleDots(E, F, F, F, F, E))
    symbol(char = 'У', brailleDots = BrailleDots(F, E, F, E, E, F))
    symbol(char = 'Ф', brailleDots = BrailleDots(F, F, E, F, E, E))
    symbol(char = 'Х', brailleDots = BrailleDots(F, F, E, E, F, E))
    symbol(char = 'Ц', brailleDots = BrailleDots(F, E, E, F, E, E))
    symbol(char = 'Ч', brailleDots = BrailleDots(F, F, F, F, F, E))
    symbol(char = 'Ш', brailleDots = BrailleDots(F, E, E, E, F, F))
    symbol(char = 'Щ', brailleDots = BrailleDots(F, E, F, F, E, F))
    symbol(char = 'Ъ', brailleDots = BrailleDots(F, F, F, E, F, F))
    symbol(char = 'Ы', brailleDots = BrailleDots(E, F, F, F, E, F))
    symbol(char = 'Ь', brailleDots = BrailleDots(E, F, F, F, F, F))
    symbol(char = 'Э', brailleDots = BrailleDots(E, F, E, F, E, F))
    symbol(char = 'Ю', brailleDots = BrailleDots(F, F, E, E, F, F))
    symbol(char = 'Я', brailleDots = BrailleDots(F, F, E, F, E, F))
}

val specialSymbols by symbols(SymbolType.special) {
    symbol(char = ']', brailleDots = BrailleDots(E, E, F, F, F, F)) // Number sign
    symbol(char = ',', brailleDots = BrailleDots(E, F, E, E, E, E)) // Comma
    symbol(char = '-', brailleDots = BrailleDots(E, E, F, E, E, F)) // Hyphen
    symbol(char = '.', brailleDots = BrailleDots(E, F, F, E, F, E)) // Dot
}

val uebDigits by symbols(SymbolType.digit) {
    symbol(char = '1', brailleDots = BrailleDots(F, E, E, E, E, E))
    symbol(char = '2', brailleDots = BrailleDots(F, F, E, E, E, E))
    symbol(char = '3', brailleDots = BrailleDots(F, E, E, F, E, E))
    symbol(char = '4', brailleDots = BrailleDots(F, E, E, F, F, E))
    symbol(char = '5', brailleDots = BrailleDots(F, E, E, E, F, E))
    symbol(char = '6', brailleDots = BrailleDots(F, F, E, F, E, E))
    symbol(char = '7', brailleDots = BrailleDots(F, F, E, F, F, E))
    symbol(char = '8', brailleDots = BrailleDots(F, F, E, E, F, E))
    symbol(char = '9', brailleDots = BrailleDots(E, F, E, F, E, E))
    symbol(char = '0', brailleDots = BrailleDots(E, F, E, F, F, E))
}

/**
 * Add here rules, how to display hints for symbols.
 */
val Context.symbolTypeIntroList: List<P2F<Char, String>> by lazyWithContext {
    // Prevent lambda of capturing context that will be invalid next time fragment entered,
    // so use `Fragment.getString` outside of lambdas.
    listOfP2F(
        getString(R.string.input_letter_intro_template).let {
            ruSymbols.map::containsKey to { c: Char -> it.format(c) }
        },

        getString(R.string.input_digit_intro_template).let {
            uebDigits.map::containsKey to { c: Char -> it.format(c) }
        },

        {
            val other = getString(R.string.input_special_intro_template)
            val numSign = getString(R.string.input_special_intro_num_sign)
            val dotIntro = getString(R.string.input_special_intro_dot)
            val commaIntro = getString(R.string.input_special_intro_comma)
            val hyphenIntro = getString(R.string.input_special_intro_hyphen)
            specialSymbols.map::containsKey to { c: Char ->
                when (c) {
                    ']' -> numSign
                    '.' -> dotIntro
                    ',' -> commaIntro
                    '-' -> hyphenIntro
                    else -> other.format(c)
                }
            }
        }()
    )
}
