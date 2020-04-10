package ru.spbstu.amd.learnbraille.res.russian.steps

import ru.spbstu.amd.learnbraille.DEBUG

/**
 * Add via plus steps of all lessons.
 *
 * Keep VERY_LAST member last.
 */
val PREPOPULATE_STEPS
    get() =
        if (DEBUG) DEBUG_LESSONS
        else LESSON_1_STEPS + LESSON_2_STEPS + VERY_LAST
