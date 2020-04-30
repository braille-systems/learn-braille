package com.github.braillesystems.learnbraille.res.russian.steps

/**
 * Add via plus steps of all lessons.
 * TODO
 *
 * Keep VERY_LAST member last.
 */
val PREPOPULATE_STEPS
    get() =
        if (TODO("Get debug from settings")) DEBUG_LESSONS
        else listOf(VERY_FIRST) + LESSON_1_STEPS + LESSON_2_STEPS + VERY_LAST
