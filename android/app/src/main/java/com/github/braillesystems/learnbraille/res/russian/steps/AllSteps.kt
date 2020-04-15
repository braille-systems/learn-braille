package com.github.braillesystems.learnbraille.res.russian.steps

import com.github.braillesystems.learnbraille.DEBUG

/**
 * Add via plus steps of all lessons.
 *
 * Keep VERY_LAST member last.
 */
val PREPOPULATE_STEPS
    get() =
        if (DEBUG) DEBUG_LESSONS
        else listOf(VERY_FIRST) + LESSON_1_STEPS + LESSON_2_STEPS + VERY_LAST
