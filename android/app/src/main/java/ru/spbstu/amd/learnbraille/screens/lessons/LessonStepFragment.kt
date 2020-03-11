package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_text_step.view.*
import ru.spbstu.amd.learnbraille.R

class LessonStepFragment: Fragment(){
    private var clickCounter = 0
    private lateinit var stubTextView: View
    private lateinit var stubShowView: View

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_text_step, container, false)
        // TODO try <include> instead of <stubView>
        view.lesson_text_stub.layoutResource = R.layout.fragment_text_step_inner
        view.lesson_show_stub.layoutResource = R.layout.fragment_show_step_inner
        this.stubTextView = view.lesson_text_stub.inflate()
        this.stubShowView = view.lesson_show_stub.inflate()
        this.stubShowView.visibility = GONE

        view.next_button.setOnClickListener {
            if (clickCounter % 2 == 0){
                (activity as AppCompatActivity).supportActionBar?.title = "Ознакомьтесь с буквой"
                this.stubTextView.visibility = GONE
                this.stubShowView.visibility = VISIBLE
            } else {
                (activity as AppCompatActivity).supportActionBar?.title = "Прочтите текст"
                this.stubTextView.visibility = VISIBLE
                this.stubShowView.visibility = GONE
            }
            clickCounter++
        }
        return view
    }

}