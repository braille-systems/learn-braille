package com.github.braillesystems.learnbraille.res

import android.content.Context
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.dsl.data
import com.github.braillesystems.learnbraille.data.entities.MarkerSymbol
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.utils.contextNotNull
import com.github.braillesystems.learnbraille.utils.lazyWithContext

val musicalNotesTypes = listOf(
    MarkerType.NoteC,
    MarkerType.NoteD,
    MarkerType.NoteE,
    MarkerType.NoteF,
    MarkerType.NoteG,
    MarkerType.NoteA,
    MarkerType.NoteB
)

val otherMusicalTypes = listOf(
    MarkerType.MusicRest8th,
    MarkerType.MusicRest4th,
    MarkerType.MusicRestHalf,
    MarkerType.MusicRestFull,
    MarkerType.OctaveMark1,
    MarkerType.OctaveMark2,
    MarkerType.OctaveMark3,
    MarkerType.OctaveMark4,
    MarkerType.OctaveMark5,
    MarkerType.OctaveMark6,
    MarkerType.OctaveMark7,
    MarkerType.MusicSharp,
    MarkerType.MusicFlat,
    MarkerType.MusicNatural
)

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
        deck(DeckTags.musical_notes) { data ->
            data is MarkerSymbol && data.type in musicalNotesTypes
        }
        deck(DeckTags.markers) { data ->
            data is MarkerSymbol && data.type !in (musicalNotesTypes + otherMusicalTypes)
        }
        deck(DeckTags.digits) { data ->
            data is Symbol && data.type == SymbolType.digit
        }
        deck(DeckTags.math) { data ->
            data is Symbol && data.type == SymbolType.math
        }
        deck(DeckTags.other_music) {data ->
            data is MarkerSymbol && data.type in otherMusicalTypes
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
    const val musical_notes = "notes"
    const val other_music = "other_music"
}

val Context.deckTagToName: Map<String, String> by lazyWithContext {
    DeckTags.run {
        mapOf(
            all to getString(R.string.deck_name_all),
            allWithRus to getString(R.string.deck_name_all_but_foreign),
            ruLetters to getString(R.string.deck_name_ru_letters),
            latinLetters to getString(R.string.deck_name_latin_letters),
            greekLetters to getString(R.string.deck_name_greek_letters),
            digits to getString(R.string.deck_name_digits),
            markers to getString(R.string.deck_name_markers),
            special to getString(R.string.deck_name_punctuation),
            math to getString(R.string.deck_name_math),
            musical_notes to getString(R.string.deck_name_musical_notes),
            other_music to getString(R.string.deck_other_musical_symbols)
        )
    }
}

val Fragment.deckTagToName
    get() = contextNotNull.deckTagToName
