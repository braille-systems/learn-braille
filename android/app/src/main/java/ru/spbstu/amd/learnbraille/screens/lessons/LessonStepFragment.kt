package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_text_step.view.*
import ru.spbstu.amd.learnbraille.R

// TODO refactor
class LessonStepFragment : Fragment() {

    private var clickCounter = 0

    // TODO place string literals to resources
    // TODO remove unsafe casts
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(
        R.layout.fragment_text_step,
        container,
        false
    ).apply {

        // TODO try <include> instead of <stubView>
        // TODO fix naming code style
        lesson_text_stub.layoutResource = R.layout.fragment_text_step_inner
        lesson_show_stub.layoutResource = R.layout.fragment_show_step_inner

        val stubTextView = lesson_text_stub.inflate()
        val stubShowView = lesson_show_stub.inflate()
        stubShowView.visibility = GONE
        (activity as AppCompatActivity).supportActionBar?.title = "Прочтите текст"

        next_button.setOnClickListener {
            if (clickCounter % 2 == 0) {
                (activity as AppCompatActivity).supportActionBar?.title = "Ознакомьтесь с буквой"
                stubTextView.visibility = GONE
                stubShowView.visibility = VISIBLE
            } else {
                (activity as AppCompatActivity).supportActionBar?.title = "Прочтите текст"
                stubTextView.visibility = VISIBLE
                stubShowView.visibility = GONE
            }
            clickCounter++
        }
    }
}
