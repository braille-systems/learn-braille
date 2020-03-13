package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_text_step.view.*
import ru.spbstu.amd.learnbraille.R

// TODO refactor
class LessonStepFragment : Fragment() {

    private var clickCounter = 0

    // TODO should be lessons ecosystem be separate activity?
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

        // TODO fix naming style
        menu_button.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_lessonStepFragment_to_menuFragment)
        )
        help_button.setOnClickListener {
            Toast.makeText(context, "Справка!", Toast.LENGTH_SHORT).show()
        }

        val navigate = View.OnClickListener {
            if (clickCounter % 2 == 0) {
                (activity as AppCompatActivity).supportActionBar?.title =
                    "Ознакомьтесь с буквой"
                stubTextView.visibility = GONE
                stubShowView.visibility = VISIBLE
            } else {
                (activity as AppCompatActivity).supportActionBar?.title = "Прочтите текст"
                stubTextView.visibility = VISIBLE
                stubShowView.visibility = GONE
            }
            clickCounter++
        }

        // TODO fix naming style
        next_button.setOnClickListener(navigate)
        prev_button.setOnClickListener(navigate)
    }
}
