package com.github.braillesystems.learnbraille.res.russian.steps

import com.github.braillesystems.learnbraille.USE_DEBUG_LESSONS

/**
 * Add via plus steps of all lessons.
 *
 * Keep VERY_LAST member last. And VERY_FIRST - first.
 */
val PREPOPULATE_STEPS
    get() =
        if (USE_DEBUG_LESSONS) DEBUG_LESSONS
        else listOf(VERY_FIRST) + LESSON_1_STEPS + LESSON_2_STEPS + VERY_LAST
