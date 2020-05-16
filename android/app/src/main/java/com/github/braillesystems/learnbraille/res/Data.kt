package com.github.braillesystems.learnbraille.res

import com.github.braillesystems.learnbraille.data.dsl.data
import com.github.braillesystems.learnbraille.data.entities.Symbol


object SymbolType {
    const val ru = "ru"
    const val special = "special"
    const val digit = "digit"
}

object StepAnnotation {
    const val golubinaBookRequired = "golubina_book_required"
}


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
    materials = practiceContent, // TODO replace with `content`
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
        deck("Русские буквы") { data ->
            data is Symbol && data.type == SymbolType.ru
        }
    }
}
