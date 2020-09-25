package com.github.braillesystems.learnbraille.res

import android.content.Context
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.dsl.data
import com.github.braillesystems.learnbraille.data.entities.MarkerSymbol
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.utils.contextNotNull
import com.github.braillesystems.learnbraille.utils.lazyWithContext

/**
 * Do not change name of this property, it is used for prepopulation.
 *
 * Use `DslTest.kt` file as DSL tutorial.
 *
 * Text in steps is parsed as HTML.
 *
 * If using `content.symbols.getValue`, `content` should be added to `data` as `materials`.
 * It is better to simply have only one value declared as `by materials`.
 *
 * Correctness of all information should be checked in compile time or in runtime.
 * If some additional info is need, do not hardcode it.
 * Just make request to the new DSL feature via github issues.
 */
val prepopulationData by data(
    materials = content,
    stepAnnotationNames = listOf(
        StepAnnotation.golubinaBookRequired,
        StepAnnotation.slateStylusRequired
    ),
    knownMaterials = knownMaterials
) {

    users {
        // Default user should always exist and be first in the list of users
        user(
            login = "default",
            name = "John Smith"
        )
    }

    courses {
        // Dev course should be always be first
        course(
            name = "Test course for developers",
            description = "Small course for tests during development",
            lessons = testLessons
        )

        course(
            name = "Курс по методике В. В. Голубиной",
            description = """
                Курс, основанный на методике В. В. Голубиной: символы изучаютсяв том порядке, 
                как их придумал Луи Брайль для французского языка. 
                Одновременно изучаются и цифры.""",
            lessons = golubinaIntroLessons
        )
    }

    decks {
        // All cards deck should always exist and be first in the list
        deck(DeckTags.all) { true }

        deck(DeckTags.allWithRus) { data ->
            val isNative = data is Symbol
                    && data.type != SymbolType.greek
                    && data.type != SymbolType.latin
            isNative || data !is Symbol
        }
        deck(DeckTags.ruLetters) { data ->
            data is Symbol && data.type == SymbolType.ru
        }
        deck(DeckTags.latinLetters) { data ->
            data is Symbol && data.type == SymbolType.latin
        }
        deck(DeckTags.greekLetters) { data ->
            data is Symbol && data.type == SymbolType.greek
        }
        deck(DeckTags.special) { data ->
            data is Symbol && data.type == SymbolType.special
        }
        deck(DeckTags.markers) { data ->
            data is MarkerSymbol
        }
        deck(DeckTags.digits) { data ->
            data is Symbol && data.type == SymbolType.digit
        }
        deck(DeckTags.math) { data ->
            data is Symbol && data.type == SymbolType.math
        }
    }
}

object StepAnnotation {
    const val golubinaBookRequired = "golubina_book_required"
    const val slateStylusRequired = "brl_slate_stylus_required"
}

object DeckTags {
    const val all = "all"
    const val allWithRus = "all_with_rus"
    const val ruLetters = "ru_letters"
    const val latinLetters = "latin_letters"
    const val greekLetters = "greek_letters"
    const val digits = "digits"
    const val markers = "markers"
    const val special = "special"
    const val math = "math"
}

val Context.deckTagToName: Map<String, String> by lazyWithContext {
    DeckTags.run {
        mapOf(
            all to getString(R.string.deck_name_all),
            allWithRus to getString(R.string.deck_name_all_with_ru),
            ruLetters to getString(R.string.deck_name_ru_letters),
            latinLetters to getString(R.string.deck_name_latin_letters),
            greekLetters to getString(R.string.deck_name_greek_letters),
            digits to getString(R.string.deck_name_digits),
            markers to getString(R.string.deck_name_markers),
            special to getString(R.string.deck_name_punctuation),
            math to getString(R.string.deck_name_math)
        )
    }
}

val Fragment.deckTagToName
    get() = contextNotNull.deckTagToName
