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
        StepAnnotation.golubinaBookRequired
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

        deck(DeckTags.ruLetters) { data ->
            data is Symbol && data.type == SymbolType.ru
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
    }
}

object StepAnnotation {
    const val golubinaBookRequired = "golubina_book_required"
}

object DeckTags {
    const val all = "all"
    const val ruLetters = "ru_letters"
    const val digits = "digits"
    const val markers = "markers"
    const val special = "special"
}

val Context.deckTagToName: Map<String, String> by lazyWithContext {
    DeckTags.run {
        mapOf(
            all to getString(R.string.deck_name_all),
            ruLetters to getString(R.string.deck_name_ru_letters),
            digits to getString(R.string.deck_name_digits),
            markers to getString(R.string.deck_name_markers),
            special to getString(R.string.deck_name_punctuation)
        )
    }
}

val Fragment.deckTagToName
    get() = contextNotNull.deckTagToName
