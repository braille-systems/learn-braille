package com.github.braillesystems.learnbraille.res

import android.content.Context
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.dsl.data
import com.github.braillesystems.learnbraille.data.entities.MarkerSymbol
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.utils.contextNotNull
import com.github.braillesystems.learnbraille.utils.lazyWithContext

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
        deck(DeckTags.Grouping.all) { true }

        deck(DeckTags.Grouping.allWithRus) { data ->
            val isNative = data is Symbol
                    && data.type != SymbolType.greek
                    && data.type != SymbolType.latin
            isNative || data !is Symbol
        }
        deck(DeckTags.Unique.RuLetters.tag) { data ->
            data is Symbol && data.type == SymbolType.ru
        }
        deck(DeckTags.Unique.LatinLetters.tag) { data ->
            data is Symbol && data.type == SymbolType.latin
        }
        deck(DeckTags.Unique.GreekLetters.tag) { data ->
            data is Symbol && data.type == SymbolType.greek
        }
        deck(DeckTags.Unique.Special.tag) { data ->
            data is Symbol && data.type == SymbolType.special
        }
        deck(DeckTags.Unique.Markers.tag) { data ->
            data is MarkerSymbol
        }
        deck(DeckTags.Unique.Digits.tag) { data ->
            data is Symbol && data.type == SymbolType.digit
        }
        deck(DeckTags.Unique.Math.tag) { data ->
            data is Symbol && data.type == SymbolType.math
        }
    }
}

object StepAnnotation {
    const val golubinaBookRequired = "golubina_book_required"
    const val slateStylusRequired = "brl_slate_stylus_required"
}

object DeckTags {
    object Grouping {
        const val all = "all"
        const val allWithRus = "all_with_rus"
    }

    enum class Unique(val tag: String) {
        RuLetters("ru_letters"),
        LatinLetters("latin_letters"),
        GreekLetters("greek_letters"),
        Digits("digits"),
        Markers("markers"),
        Special("special"),
        Math("math")
    }
}

val Context.deckTagToName: Map<String, String> by lazyWithContext {
    val generalizing = DeckTags.Grouping.run {
        mapOf(
            all to getString(R.string.deck_name_all),
            allWithRus to getString(R.string.deck_name_all_but_foreign)
        )
    }
    generalizing + mapOf(
        DeckTags.Unique.RuLetters.tag to getString(R.string.deck_name_ru_letters),
        DeckTags.Unique.LatinLetters.tag to getString(R.string.deck_name_latin_letters),
        DeckTags.Unique.GreekLetters.tag to getString(R.string.deck_name_greek_letters),
        DeckTags.Unique.Digits.tag to getString(R.string.deck_name_digits),
        DeckTags.Unique.Markers.tag to getString(R.string.deck_name_markers),
        DeckTags.Unique.Special.tag to getString(R.string.deck_name_punctuation),
        DeckTags.Unique.Math.tag to getString(R.string.deck_name_math)
    )
}

val Fragment.deckTagToName
    get() = contextNotNull.deckTagToName
