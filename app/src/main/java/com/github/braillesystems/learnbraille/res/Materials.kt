package com.github.braillesystems.learnbraille.res

import android.content.Context
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.dsl.known
import com.github.braillesystems.learnbraille.data.dsl.markers
import com.github.braillesystems.learnbraille.data.dsl.materials
import com.github.braillesystems.learnbraille.data.dsl.symbols
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.E
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.utils.rules
import kotlinx.serialization.Serializable

object SymbolType {
    const val ru = "ru"
    const val latin = "Latin"
    const val greek = "greek"
    const val special = "special"
    const val digit = "digit"
}

@Serializable
enum class MarkerType {
    RussianCapital,
    GreekCapital,
    LatinCapital,
    NumberSign
}

/**
 * Do not forget to register print rules below for the new types of symbols.
 *
 * Always use this variable to get materials.
 */

// TODO write a migration for lat, greek

val content by materials {
    +ruSymbols
    +punctuationSigns
    +uebDigits
    +latinLetters
    +greekLetters
    +ms
}

val knownMaterials by known(
    'А', 'Б', 'Ц', 'Д', 'Е', 'Ф', 'Г'
)

/*
 * Add here rules, how to display hints for symbols.
 *
 * Prevent lambda of capturing context that will be invalid next time fragment entered,
 * so use `Fragment.getString` outside of lambdas.
 */

val Context.inputSymbolPrintRules by rules<Context, Char, String>(
    {
        val t = getString(R.string.input_letter_intro_template)
        ruSymbols.map::containsKey to { c: Char -> t.format(c) }
    },

    {
        val t = getString(R.string.input_digit_intro_template)
        uebDigits.map::containsKey to { c: Char -> t.format(c) }
    },

    {
        val t = getString(R.string.input_latin_letter_intro_template)
        latinLetters.map::containsKey to { c: Char -> t.format(c) }
    },

    {
        val t = getString(R.string.input_greek_letter_intro_template)
        greekLetters.map::containsKey to { c: Char -> t.format(c) }
    },

    {
        val other = getString(R.string.input_special_intro_template)
        val dotIntro = getString(R.string.input_special_intro_dot)
        val commaIntro = getString(R.string.input_special_intro_comma)
        val hyphenIntro = getString(R.string.input_special_intro_hyphen)
        punctuationSigns.map::containsKey to { c: Char ->
            when (c) {
                '.' -> dotIntro
                ',' -> commaIntro
                '-' -> hyphenIntro
                else -> other.format(c)
            }
        }
    }
)

val Context.showSymbolPrintRules by rules<Context, Char, String>(
    // TODO reduce repeated code

    {
        val t = getString(R.string.show_letter_intro_template)
        ruSymbols.map::containsKey to { c: Char -> t.format(c) }
    },

    {
        val t = getString(R.string.show_digit_intro_template)
        uebDigits.map::containsKey to { c: Char -> t.format(c) }
    },

    {
        val t = getString(R.string.show_latin_letter_intro_template)
        latinLetters.map::containsKey to { c: Char -> t.format(c) }
    },

    {
        val t = getString(R.string.show_greek_letter_intro_template)
        greekLetters.map::containsKey to { c: Char -> t.format(c) }
    },

    {
        val other = getString(R.string.show_special_intro_template)
        val dotIntro = getString(R.string.show_special_intro_dot)
        val commaIntro = getString(R.string.show_special_intro_comma)
        val hyphenIntro = getString(R.string.show_special_intro_hyphen)
        punctuationSigns.map::containsKey to { c: Char ->
            when (c) {
                '.' -> dotIntro
                ',' -> commaIntro
                '-' -> hyphenIntro
                else -> other.format(c)
            }
        }
    }
)

fun getCaptionTitleId(symbol: Symbol): Int {
    when(symbol.char) {
        '.' -> return R.string.show_special_intro_dot
        ',' -> return R.string.show_special_intro_comma
        '-' -> return R.string.show_special_intro_hyphen
    }
    return when (symbol.type) {
        SymbolType.ru -> R.string.letter_caption_ru
        SymbolType.greek -> R.string.letter_caption_greek
        SymbolType.latin -> R.string.letter_caption_latin
        SymbolType.digit -> R.string.letter_caption_digit
        SymbolType.special -> R.string.letter_caption_special
        else -> R.string.empty_string
    }
}

val Context.inputMarkerPrintRules by rules<Context, MarkerType, String>(
    {
        val s = getString(R.string.input_mod_ru_capital)
        MarkerType.RussianCapital::equals to { _: MarkerType -> s }
    },

    {
        val s = getString(R.string.input_mod_greek_capital)
        MarkerType.GreekCapital::equals to { _: MarkerType -> s }
    },

    {
        val s = getString(R.string.input_mod_latin_capital)
        MarkerType.LatinCapital::equals to { _: MarkerType -> s }
    },

    {
        val s = getString(R.string.input_mod_num_sign)
        MarkerType.NumberSign::equals to { _: MarkerType -> s }
    }
)

val Context.showMarkerPrintRules by rules<Context, MarkerType, String>(
    {
        val s = getString(R.string.show_mod_capital)
        MarkerType.RussianCapital::equals to { _: MarkerType -> s }
    },

    {
        val s = getString(R.string.show_mod_greek)
        MarkerType.GreekCapital::equals to { _: MarkerType -> s }
    },

    {
        val s = getString(R.string.show_mod_latin)
        MarkerType.LatinCapital::equals to { _: MarkerType -> s }
    },

    {
        val s = getString(R.string.show_mod_num_sign)
        MarkerType.NumberSign::equals to { _: MarkerType -> s }
    }
)

private val ruSymbols by symbols(SymbolType.ru) {
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
private val punctuationSigns by symbols(SymbolType.special) {
    symbol(char = ',', brailleDots = BrailleDots(E, F, E, E, E, E)) // Comma
    symbol(char = '-', brailleDots = BrailleDots(E, E, F, E, E, F)) // Hyphen
    symbol(char = '.', brailleDots = BrailleDots(E, F, E, E, F, F)) // Dot
}

private val uebDigits by symbols(SymbolType.digit) {
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

private val latinLetters by symbols(SymbolType.latin) {
    symbol(char = 'A', brailleDots = BrailleDots(F, E, E, E, E, E))
    symbol(char = 'B', brailleDots = BrailleDots(F, F, E, E, E, E))
    symbol(char = 'C', brailleDots = BrailleDots(F, E, E, F, E, E))
    symbol(char = 'D', brailleDots = BrailleDots(F, E, E, F, F, E))
    symbol(char = 'E', brailleDots = BrailleDots(F, E, E, E, F, E))
    symbol(char = 'F', brailleDots = BrailleDots(F, F, E, F, E, E))
    symbol(char = 'G', brailleDots = BrailleDots(F, F, E, F, F, E))
    symbol(char = 'H', brailleDots = BrailleDots(F, F, E, E, F, E))
    symbol(char = 'I', brailleDots = BrailleDots(E, F, E, F, E, E))
    symbol(char = 'J', brailleDots = BrailleDots(E, F, E, F, F, E))
    symbol(char = 'K', brailleDots = BrailleDots(F, E, F, E, E, E))
    symbol(char = 'L', brailleDots = BrailleDots(F, F, F, E, E, E))
    symbol(char = 'M', brailleDots = BrailleDots(F, E, F, F, E, E))
    symbol(char = 'N', brailleDots = BrailleDots(F, E, F, F, F, E))
    symbol(char = 'O', brailleDots = BrailleDots(F, E, F, E, F, E))
    symbol(char = 'P', brailleDots = BrailleDots(F, F, F, F, E, E))
    symbol(char = 'Q', brailleDots = BrailleDots(F, F, F, F, F, E))
    symbol(char = 'R', brailleDots = BrailleDots(F, F, F, E, F, E))
    symbol(char = 'S', brailleDots = BrailleDots(E, F, F, F, E, E))
    symbol(char = 'T', brailleDots = BrailleDots(E, F, F, F, F, E))
    symbol(char = 'U', brailleDots = BrailleDots(F, E, F, E, E, F))
    symbol(char = 'V', brailleDots = BrailleDots(F, F, F, E, E, F))
    symbol(char = 'W', brailleDots = BrailleDots(E, F, E, F, F, F))
    symbol(char = 'X', brailleDots = BrailleDots(F, E, F, F, E, F))
    symbol(char = 'Y', brailleDots = BrailleDots(F, E, F, F, F, F))
    symbol(char = 'Z', brailleDots = BrailleDots(F, E, F, E, F, F))
}

private val greekLetters by symbols(SymbolType.greek) {
    // Note: A - Alpha (Unicode char 0399); A - English (0041); А - Russian
    // You may check the code by copying and pasting at https://r12a.github.io/app-conversion/
    symbol(char = 'Α', brailleDots = BrailleDots(F, E, E, E, E, E))
    symbol(char = 'Β', brailleDots = BrailleDots(F, F, E, E, E, E))
    symbol(char = 'Γ', brailleDots = BrailleDots(F, F, E, F, F, E))
    symbol(char = 'Δ', brailleDots = BrailleDots(F, E, E, F, F, E))
    symbol(char = 'Ε', brailleDots = BrailleDots(F, E, E, E, F, E))
    symbol(char = 'Ζ', brailleDots = BrailleDots(F, E, F, E, F, F))
    symbol(char = 'Η', brailleDots = BrailleDots(E, F, E, F, F, E))
    symbol(char = 'Θ', brailleDots = BrailleDots(F, F, E, E, F, E))
    symbol(char = 'Ι', brailleDots = BrailleDots(E, F, E, F, E, E))
    symbol(char = 'Κ', brailleDots = BrailleDots(F, E, F, E, E, E))
    symbol(char = 'Λ', brailleDots = BrailleDots(F, F, F, E, E, E))
    symbol(char = 'Μ', brailleDots = BrailleDots(F, E, F, F, E, E))
    symbol(char = 'Ν', brailleDots = BrailleDots(F, E, F, F, F, E))
    symbol(char = 'Ξ', brailleDots = BrailleDots(F, E, F, F, E, F))
    symbol(char = 'Ο', brailleDots = BrailleDots(F, E, F, E, F, E))
    symbol(char = 'Π', brailleDots = BrailleDots(F, F, F, F, E, E))
    symbol(char = 'Ρ', brailleDots = BrailleDots(F, F, F, E, F, E))
    symbol(char = 'Σ', brailleDots = BrailleDots(E, F, F, F, E, E))
    symbol(char = 'Τ', brailleDots = BrailleDots(E, F, F, F, F, E))
    symbol(char = 'Υ', brailleDots = BrailleDots(F, E, F, E, E, F))
    symbol(char = 'Φ', brailleDots = BrailleDots(F, F, E, F, E, E))
    symbol(char = 'Χ', brailleDots = BrailleDots(F, E, E, F, E, E))
    symbol(char = 'Ψ', brailleDots = BrailleDots(F, E, F, F, F, F))
    symbol(char = 'Ω', brailleDots = BrailleDots(E, F, E, F, F, F))
}

private val ms by markers {
    marker(MarkerType.GreekCapital, BrailleDots(E, E, E, F, F, F))
    marker(MarkerType.LatinCapital, BrailleDots(E, E, E, F, E, F))
    marker(MarkerType.RussianCapital, BrailleDots(E, E, E, F, F, E))
    marker(MarkerType.NumberSign, BrailleDots(E, E, F, F, F, F))
}
