package com.github.braillesystems.learnbraille.res

import android.content.Context
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.dsl.data
import com.github.braillesystems.learnbraille.data.entities.Symbol
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
 * If you need some additional info, do not hardcode it. Just make request to the new DSL feature.
 */
val prepopulationData by data(
    materials = content,
    stepAnnotations = listOf(
        StepAnnotation.golubinaBookRequired
    )
) {

    users {
        user(
            login = "default",
            name = "John Smith"
        )
    }

    courses {
        // This course should be always be first
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
                Одновременно изучаются и цифры."""
        ) {
            +golubinaIntroLessons
            +someMoreGolubinaLessons
        }
    }

    decks {
        deck(DeckTags.all) { true }
        deck(DeckTags.ruLetters) { data ->
            data is Symbol && data.type == SymbolType.ru
        }
        deck(DeckTags.special) { data ->
            data is Symbol && data.type == SymbolType.special
        }
        deck(DeckTags.digits) { data ->
            data is Symbol && data.type == SymbolType.digit
        }
    }
}

object StepAnnotation {
    const val golubinaBookRequired = "golubina_book_required"
}

object DeckTags {
    const val all = "all"
    const val ruLetters = "ru_letters"
    const val digits = "digits"
    const val special = "special"
}

val Context.deckTagToName: Map<String, String> by lazyWithContext {
    DeckTags.run {
        mapOf(
            all to getString(R.string.deck_name_all),
            ruLetters to getString(R.string.deck_name_ru_letters),
            digits to getString(R.string.deck_name_digits),
            special to getString(R.string.deck_name_special_symbols)
        )
    }
}

val Fragment.deckTagToName
    get() = context?.deckTagToName ?: error("Fragment should have context")
