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
import kotlinx.android.synthetic.main.fragment_show_step_inner.view.*
import kotlinx.android.synthetic.main.fragment_text_step.view.*
import kotlinx.android.synthetic.main.fragment_text_step_inner.view.*
import ru.spbstu.amd.learnbraille.R

interface LessonStep{
    abstract fun show()
    abstract fun getTitle():String
}

// TODO refactor
class LessonStepFragment : Fragment() {

    companion object{
        private var currentStep = 0
    }

    // TODO should be lessons ecosystem be separate activity?
    // TODO place string literals to resources
    // TODO remove unsafe casts
    // TODO make type safe: use DataBinging
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
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

        class TextStep(
            val nLesson:Int = 1,
            val nStep:Int = 1,
            val longText:String = "",
            val stepTitle:String="Прочтите текст"
        ): LessonStep{
            override fun show(){
                //(activity as AppCompatActivity).supportActionBar?.title = this.stepTitle
                edu_text_view.text = this.longText
                stubTextView.visibility = VISIBLE
                stubShowView.visibility = GONE
            }
            override fun getTitle() = this.stepTitle
        }

        class ShowStep(
            val nLesson:Int = 1,
            val nStep:Int = 1,
            val bigText:Char = ' ',
            val brailleDots:BooleanArray = booleanArrayOf(true, false, false, false, false, false),
            val stepTitle:String="Ознакомьтесь с буквой"
        ): LessonStep{
            override fun show(){
                letter.text = bigText.toString()

                dotButton1.isChecked = brailleDots[0]
                dotButton2.isChecked = brailleDots[1]
                dotButton3.isChecked = brailleDots[2]
                dotButton4.isChecked = brailleDots[3]
                dotButton5.isChecked = brailleDots[4]
                dotButton6.isChecked = brailleDots[5]
                stubShowView.visibility = VISIBLE
                stubTextView.visibility = GONE
            }
            override fun getTitle() = this.stepTitle
        }

        val steps:Array<LessonStep> = arrayOf(
            TextStep(1, 1, resources.getString(R.string.text_step1), "Знакомство с шеститочием"),
            ShowStep(1, 2, ' ', booleanArrayOf(true, true, true, true, true, true), "Шеститочие"),
            TextStep(1, 3, resources.getString(R.string.text_step2), "Работа с букварём"),
            TextStep(2, 1, resources.getString(R.string.text_step3)),
            TextStep(2, 2, resources.getString(R.string.text_step4)),
            ShowStep(2, 3, 'А', booleanArrayOf(true, false, false, false, false, false)),
            TextStep(2, 4, resources.getString(R.string.text_step5), "Работа с букварём"),
            ShowStep(2, 5, 'Б', booleanArrayOf(true, true, false, false, false, false)),
            TextStep(2, 6, resources.getString(R.string.text_step6))
        )

        // TODO fix naming style
        menu_button.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_lessonStepFragment_to_menuFragment)
        )
        help_button.setOnClickListener {
            Toast.makeText(context, "Вы находитесь в разделе 'Пошаговые уроки'", Toast.LENGTH_SHORT).show()
        }

        val navigateFwd = View.OnClickListener {
            currentStep++
            currentStep %= steps.size
            steps[currentStep].show()
            (activity as AppCompatActivity).supportActionBar?.title = steps[currentStep].getTitle()
        }

        val navigateBack = View.OnClickListener {
            if(currentStep > 0) currentStep--
            steps[currentStep].show()
            (activity as AppCompatActivity).supportActionBar?.title = steps[currentStep].getTitle()
        }

        // TODO fix naming style
        next_button.setOnClickListener(navigateFwd)
        prev_button.setOnClickListener(navigateBack)

        steps[currentStep].show()
    }
}
