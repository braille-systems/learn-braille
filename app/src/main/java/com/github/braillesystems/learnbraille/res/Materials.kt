package com.github.braillesystems.learnbraille.res

import android.content.Context
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.dsl.known
import com.github.braillesystems.learnbraille.data.dsl.markers
import com.github.braillesystems.learnbraille.data.dsl.materials
import com.github.braillesystems.learnbraille.data.dsl.symbols
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.E
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.res.MathSigns.*
import com.github.braillesystems.learnbraille.res.PunctuationSigns.*
import com.github.braillesystems.learnbraille.utils.contextNotNull
import com.github.braillesystems.learnbraille.utils.rules
import kotlinx.serialization.Serializable

object SymbolType {
    const val ru = "ru"
    const val digit = "digit"
    const val latin = "Latin"
    const val greek = "greek"
    const val special = "special"
    const val math = "math"
}

@Serializable
enum class MarkerType {
    RussianCapital,
    GreekCapital,
    LatinCapital,
    LatinSmall,
    BoldFont,
    ItalicFont,
    NumberSign
}

/**
 * Do not forget to register print rules below for the new types of symbols.
 *
 * Always use this variable to get materials.
 */
val content by materials {
    +ruSymbols
    +punctuationSigns
    +uebDigits
    +latinLetters
    +greekLetters
    +mathSigns
    +ms
}

val knownMaterials by known(
    'А', 'Б', 'Ц', 'Д', 'Е', 'Ф', 'Г'
)


private val ruSymbols by symbols(SymbolType.ru) {
    // UTF-16: 0410-042F
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

enum class PunctuationSigns(val c: Char) {
    Hyphen('—'),
    LeftQuotation('«'),
    RightQuotation('»')
}

private val punctuationSigns by symbols(SymbolType.special) {
    symbol(char = ',', brailleDots = BrailleDots(E, F, E, E, E, E)) // Comma
    symbol(char = Hyphen.c, brailleDots = BrailleDots(E, E, F, E, E, F)) // Hyphen
    symbol(char = '.', brailleDots = BrailleDots(E, F, E, E, F, F)) // Dot
    symbol(char = '!', brailleDots = BrailleDots(E, F, F, E, F, E)) // Exclamation mark
    symbol(char = '?', brailleDots = BrailleDots(E, F, E, E, E, F)) // Question mark
    symbol(char = LeftQuotation.c, brailleDots = BrailleDots(E, F, F, E, E, F))
    symbol(char = RightQuotation.c, brailleDots = BrailleDots(E, E, F, E, F, F))
    symbol(char = '(', brailleDots = BrailleDots(F, F, E, E, E, F)) // Left parenthesis
    symbol(char = ')', brailleDots = BrailleDots(E, E, F, F, F, E)) // Right parenthesis
    symbol(char = '*', brailleDots = BrailleDots(E, E, F, E, F, E)) // Asterisk
    symbol(char = ':', brailleDots = BrailleDots(E, F, E, E, F, E)) // Colon
    symbol(char = ';', brailleDots = BrailleDots(E, F, F, E, E, E)) // Semicolon
    symbol(char = '\'', brailleDots = BrailleDots(E, E, E, F, E, E)) // Stress
}

enum class MathSigns(val c: Char) {
    DotMul('·'),
    CrossMul('✕'),
    Div('÷')
}

private val mathSigns by symbols(SymbolType.math) {
    symbol(char = '+', brailleDots = BrailleDots(E, F, F, E, F, E)) // Plus
    symbol(char = '-', brailleDots = BrailleDots(E, E, F, E, E, F)) // Minus
    symbol(char = DotMul.c, brailleDots = BrailleDots(E, E, F, E, E, E)) // Dot multiplication
    symbol(char = CrossMul.c, brailleDots = BrailleDots(E, F, F, E, E, F)) // Cross multiplication
    symbol(char = '/', brailleDots = BrailleDots(E, E, F, E, F, F)) // Division (fraction)
    symbol(char = Div.c, brailleDots = BrailleDots(E, F, E, E, F, F)) // Division
    symbol(char = '=', brailleDots = BrailleDots(E, F, F, E, F, F)) // Equality
}

private val uebDigits by symbols(SymbolType.digit) {
    // UTF-16: 0030-0039
    symbol(char = '0', brailleDots = BrailleDots(E, F, E, F, F, E))
    symbol(char = '1', brailleDots = BrailleDots(F, E, E, E, E, E))
    symbol(char = '2', brailleDots = BrailleDots(F, F, E, E, E, E))
    symbol(char = '3', brailleDots = BrailleDots(F, E, E, F, E, E))
    symbol(char = '4', brailleDots = BrailleDots(F, E, E, F, F, E))
    symbol(char = '5', brailleDots = BrailleDots(F, E, E, E, F, E))
    symbol(char = '6', brailleDots = BrailleDots(F, F, E, F, E, E))
    symbol(char = '7', brailleDots = BrailleDots(F, F, E, F, F, E))
    symbol(char = '8', brailleDots = BrailleDots(F, F, E, E, F, E))
    symbol(char = '9', brailleDots = BrailleDots(E, F, E, F, E, E))
}

private val latinLetters by symbols(SymbolType.latin) {
    // UTF-16: 0041-005A
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
    // Note: A - Alpha (Unicode char 0391); A - English (0041); А - Russian (0410)
    // You may check the code by copying and pasting at https://r12a.github.io/app-conversion/
    // Capital greek letters in UTF-16: 0391-03A9
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
    marker(MarkerType.LatinSmall, BrailleDots(E, E, E, E, E, F))
    marker(MarkerType.RussianCapital, BrailleDots(E, E, E, F, F, E))
    marker(MarkerType.BoldFont, BrailleDots(F, F, E, F, F, F))
    marker(MarkerType.ItalicFont, BrailleDots(E, E, E, F, F, F))
    marker(MarkerType.NumberSign, BrailleDots(E, E, F, F, F, F))
}


/*
 * Add here rules, how to display hints for symbols.
 *
 * Prevent lambda of capturing context that will be invalid next time fragment entered,
 * so use `Fragment.getString` outside of lambdas.
 */

val Fragment.inputSymbolPrintRules get() = contextNotNull.inputSymbolPrintRules
val Fragment.showSymbolPrintRules get() = contextNotNull.showSymbolPrintRules
val Fragment.captionRules get() = contextNotNull.captionRules
val Fragment.inputMarkerPrintRules get() = contextNotNull.inputMarkerPrintRules
val Fragment.showMarkerPrintRules get() = contextNotNull.showMarkerPrintRules

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
        val dotIntro = getString(R.string.input_special_intro_dot)
        val commaIntro = getString(R.string.input_special_intro_comma)
        val hyphenIntro = getString(R.string.input_special_intro_hyphen)
        val exclamationIntro = getString(R.string.input_special_intro_exclamation)
        val questionIntro = getString(R.string.input_special_intro_question)
        val quotationLeftIntro = getString(R.string.input_special_intro_quotation_left)
        val quotationRightIntro = getString(R.string.input_special_intro_quotation_right)
        val parenthesisLeftIntro = getString(R.string.input_special_intro_parenthesis_left)
        val parenthesisRightIntro = getString(R.string.input_special_intro_parenthesis_right)
        val asteriskIntro = getString(R.string.input_special_intro_asterisk)
        val colonIntro = getString(R.string.input_special_intro_colon)
        val semicolonIntro = getString(R.string.input_special_intro_semicolon)
        val stressIntro = getString(R.string.input_special_intro_stress)
        punctuationSigns.map::containsKey to { c: Char ->
            when (c) {
                '.' -> dotIntro
                ',' -> commaIntro
                Hyphen.c -> hyphenIntro
                '!' -> exclamationIntro
                '?' -> questionIntro
                LeftQuotation.c -> quotationLeftIntro
                RightQuotation.c -> quotationRightIntro
                '(' -> parenthesisLeftIntro
                ')' -> parenthesisRightIntro
                '*' -> asteriskIntro
                ':' -> colonIntro
                ';' -> semicolonIntro
                '\'' -> stressIntro
                else -> error("Undefined symbol: $c")
            }
        }
    },

    {
        val plusIntro = getString(R.string.input_math_plus)
        val minusIntro = getString(R.string.input_math_minus)
        val dotMultIntro = getString(R.string.input_math_dot_mult)
        val crossMultIntro = getString(R.string.input_math_cross_mult)
        val fracDivisionIntro = getString(R.string.input_math_division_fraction)
        val divisionIntro = getString(R.string.input_math_division)
        val equalityIntro = getString(R.string.input_math_equality)
        mathSigns.map::containsKey to { c: Char ->
            when (c) {
                '+' -> plusIntro
                '-' -> minusIntro
                DotMul.c -> dotMultIntro
                CrossMul.c -> crossMultIntro
                '/' -> fracDivisionIntro
                Div.c -> divisionIntro
                '=' -> equalityIntro
                else -> error("Undefined symbol: $c")
            }
        }
    }
)

val Context.showSymbolPrintRules by rules<Context, Char, String>(

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
        val dotIntro = getString(R.string.show_special_intro_dot)
        val commaIntro = getString(R.string.show_special_intro_comma)
        val hyphenIntro = getString(R.string.show_special_intro_hyphen)
        val exclamationIntro = getString(R.string.show_special_intro_exclamation)
        val questionIntro = getString(R.string.show_special_intro_question)
        val quotationLeftIntro = getString(R.string.show_special_intro_quotation_left)
        val quotationRightIntro = getString(R.string.show_special_intro_quotation_right)
        val parenthesisLeftIntro = getString(R.string.show_special_intro_parenthesis_left)
        val parenthesisRightIntro = getString(R.string.show_special_intro_parenthesis_right)
        val asteriskIntro = getString(R.string.show_special_intro_asterisk)
        val colonIntro = getString(R.string.show_special_intro_colon)
        val semicolonIntro = getString(R.string.show_special_intro_semicolon)
        val stressIntro = getString(R.string.show_special_intro_stress)
        punctuationSigns.map::containsKey to { c: Char ->
            when (c) {
                '.' -> dotIntro
                ',' -> commaIntro
                Hyphen.c -> hyphenIntro
                '!' -> exclamationIntro
                '?' -> questionIntro
                LeftQuotation.c -> quotationLeftIntro
                RightQuotation.c -> quotationRightIntro
                '(' -> parenthesisLeftIntro
                ')' -> parenthesisRightIntro
                '*' -> asteriskIntro
                ':' -> colonIntro
                ';' -> semicolonIntro
                '\'' -> stressIntro
                else -> error("Undefined symbol: $c")
            }
        }
    },

    {
        val plusIntro = getString(R.string.show_math_plus)
        val minusIntro = getString(R.string.show_math_minus)
        val dotMultIntro = getString(R.string.show_math_dot_mult)
        val crossMultIntro = getString(R.string.show_math_cross_mult)
        val fracDivisionIntro = getString(R.string.show_math_division_fraction)
        val divisionIntro = getString(R.string.show_math_division)
        val equalityIntro = getString(R.string.show_math_equality)
        mathSigns.map::containsKey to { c: Char ->
            when (c) {
                '+' -> plusIntro
                '-' -> minusIntro
                DotMul.c -> dotMultIntro
                CrossMul.c -> crossMultIntro
                '/' -> fracDivisionIntro
                Div.c -> divisionIntro
                '=' -> equalityIntro
                else -> error("Undefined symbol: $c")
            }
        }
    }
)

val Context.captionRules by rules<Context, Symbol, String>(
    {
        val specialCaptions = mapOf(
            '.' to R.string.show_special_intro_dot,
            ',' to R.string.show_special_intro_comma,
            Hyphen.c to R.string.show_special_intro_hyphen,
            '!' to R.string.show_special_intro_exclamation,
            '?' to R.string.show_special_intro_question,
            LeftQuotation.c to R.string.show_special_intro_quotation_left,
            RightQuotation.c to R.string.show_special_intro_quotation_right,
            '(' to R.string.show_special_intro_parenthesis_left,
            ')' to R.string.show_special_intro_parenthesis_right,
            '*' to R.string.show_special_intro_asterisk,
            ':' to R.string.show_special_intro_colon,
            ';' to R.string.show_special_intro_semicolon,
            '\'' to R.string.show_special_intro_stress,
            '+' to R.string.show_math_plus,
            '-' to R.string.show_math_minus,
            DotMul.c to R.string.show_math_dot_mult,
            CrossMul.c to R.string.show_math_cross_mult,
            '/' to R.string.show_math_division_fraction,
            Div.c to R.string.show_math_division,
            '=' to R.string.show_math_equality
        ).mapValues {
            getString(it.value)
        }
        return@rules { s: Symbol ->
            specialCaptions.containsKey(s.char)
        } to { s: Symbol ->
            specialCaptions.getValue(s.char)
        }
    },

    {
        val specialCaptions = mapOf(
            SymbolType.ru to R.string.letter_caption_ru,
            SymbolType.greek to R.string.letter_caption_greek,
            SymbolType.latin to R.string.letter_caption_latin,
            SymbolType.digit to R.string.letter_caption_digit,
            SymbolType.special to R.string.letter_caption_special
        ).mapValues {
            getString(it.value)
        }
        return@rules { s: Symbol ->
            specialCaptions.containsKey(s.type)
        } to { s: Symbol ->
            specialCaptions.getValue(s.type)
        }
    },

    {
        { _: Symbol -> true } to { _: Symbol -> "" }
    }
)

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
        val s = getString(R.string.input_mod_latin_small)
        MarkerType.LatinSmall::equals to { _: MarkerType -> s }
    },

    {
        val s = getString(R.string.input_mod_bold)
        MarkerType.BoldFont::equals to { _: MarkerType -> s }
    },

    {
        val s = getString(R.string.input_mod_italic)
        MarkerType.ItalicFont::equals to { _: MarkerType -> s }
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
        val s = getString(R.string.show_mod_latin)
        MarkerType.LatinCapital::equals to { _: MarkerType -> s }
    },

    {
        val s = getString(R.string.show_mod_latin_small)
        MarkerType.LatinSmall::equals to { _: MarkerType -> s }
    },

    {
        val s = getString(R.string.show_mod_bold)
        MarkerType.BoldFont::equals to { _: MarkerType -> s }
    },

    {
        val s = getString(R.string.show_mod_italic)
        MarkerType.ItalicFont::equals to { _: MarkerType -> s }
    },

    {
        val s = getString(R.string.show_mod_num_sign)
        MarkerType.NumberSign::equals to { _: MarkerType -> s }
    }
)
