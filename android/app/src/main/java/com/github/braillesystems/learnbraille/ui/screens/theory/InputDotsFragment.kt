//package com.github.braillesystems.learnbraille.ui.screens.theory
//
//import android.os.Bundle
//import android.os.Vibrator
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import com.github.braillesystems.learnbraille.R
//import com.github.braillesystems.learnbraille.data.entities.BrailleDots
//import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInputDotsBinding
//import com.github.braillesystems.learnbraille.ui.views.BrailleDotsState
//import timber.log.Timber
//
//class InputDotsFragment : AbstractInputLesson(R.string.lessons_help_input_dots) {
//
//    private lateinit var expectedDots: BrailleDots
//    private lateinit var dotsState: BrailleDotsState
//    private var buzzer: Vibrator? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ) = DataBindingUtil.inflate<FragmentLessonsInputDotsBinding>(
//        inflater,
//        R.layout.fragment_lessons_input_dots,
//        container,
//        false
//    ).apply {
//
//        Timber.i("Start initialize input dots fragment")
//
////        updateTitle(getString(R.string.lessons_title_input_dots))
////        setHasOptionsMenu(true)
//
////        val step = getStepArg()
////        require(step.data is InputDots)
////        titleTextView.text = step.title
////        infoTextView.text = step.data.text
////            ?: getString(R.string.lessons_input_dots_info_template)
////                .format(step.data.dots.spelling)
////        brailleDots.dotsState.display(step.data.dots)
////
////        expectedDots = step.data.dots
////        userTouchedDots = false
////        dotsState = brailleDots.dotsState.apply {
////            uncheck()
////            clickable(true)
////            checkBoxes.forEach { checkBox ->
////                checkBox.setOnClickListener {
////                    userTouchedDots = true
////                }
////            }
////        }
////
////
////        val viewModelFactory = InputViewModelFactory(application, expectedDots) {
////            dotsState.brailleDots
////        }
////        viewModel = ViewModelProvider(
////            this@InputDotsFragment, viewModelFactory
////        ).get(InputViewModel::class.java)
////        buzzer = activity?.getSystemService()
////
////
////        inputViewModel = viewModel
////        lifecycleOwner = this@InputDotsFragment
////
////
////        val database = getDBInstance()
////
////        prevButton.setOnClickListener(getPrevButtonListener(step, application.userId, database))
////        toCurrStepButton.setOnClickListener(getToCurrStepListener(application.userId, database))
////
////        viewModel.observeEventCorrect(
////            viewLifecycleOwner, application, dotsState, buzzer,
////            getEventCorrectObserverBlock(step, application.userId, database)
////        )
////
////        viewModel.observeEventIncorrect(
////            viewLifecycleOwner, application, dotsState,
////            buzzer = null,  // No notification by default
////            block = getEventIncorrectObserverBlock(
////                step, application.userId, database
////            ) {
////                makeIncorrectToast()
////                buzzer.checkedBuzz(application, INCORRECT_BUZZ_PATTERN)
////            }
////        )
////
////        viewModel.observeEventHint(
////            viewLifecycleOwner, dotsState, /* serial */
////            block = getEventHintObserverBlock()
////        )
////
////        viewModel.observeEventPassHint(
////            viewLifecycleOwner, dotsState,
////            getEventPassHintObserverBlock()
////        )
//
//    }.root
//}
