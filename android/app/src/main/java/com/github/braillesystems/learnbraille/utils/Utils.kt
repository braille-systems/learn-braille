package com.github.braillesystems.learnbraille.utils

import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.LearnBrailleApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

val Fragment.application: LearnBrailleApplication
    get() = requireNotNull(activity).application as LearnBrailleApplication

fun scope(job: Job = Job()) = CoroutineScope(Dispatchers.Main + job)
