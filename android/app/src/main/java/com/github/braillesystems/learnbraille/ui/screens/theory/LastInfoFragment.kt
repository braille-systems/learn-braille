//package com.github.braillesystems.learnbraille.ui.screens.theory
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import com.github.braillesystems.learnbraille.R
//import com.github.braillesystems.learnbraille.databinding.FragmentLessonLastInfoBinding
//import com.github.braillesystems.learnbraille.userId
//import com.github.braillesystems.learnbraille.utils.updateTitle
//
//class LastInfoFragment : AbstractLesson(R.string.lessons_help_last_info) {
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ) = DataBindingUtil.inflate<FragmentLessonLastInfoBinding>(
//        inflater,
//        R.layout.fragment_lesson_last_info,
//        container,
//        false
//    ).apply {
//
//        updateTitle(getString(R.string.lessons_title_last_info))
//        setHasOptionsMenu(true)
//
////        val step = getStepArg()
////        require(step.data is LastInfo)
////        titleTextView.text = step.title
////        infoTextView.text = step.data.text
////        infoTextView.movementMethod = ScrollingMovementMethod()
////
////        getDBInstance().run {
////            prevButton.setOnClickListener {
////                navigateToPrevStep(
////                    current = step,
////                    userId = application.userId,
////                    stepDao = stepDao,
////                    lastStepDao = userLastStep
////                )
////            }
////        }
//
//    }.root
//}
