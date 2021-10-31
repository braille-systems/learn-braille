package com.github.braillesystems.learnbraille.ui.screens.theory.lessons

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.github.braillesystems.learnbraille.COURSE
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Lesson
import com.github.braillesystems.learnbraille.data.repository.TheoryRepository
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsListBinding
import com.github.braillesystems.learnbraille.databinding.LessonsListItemBinding
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragment
import com.github.braillesystems.learnbraille.ui.screens.theory.toLastLessonStep
import com.github.braillesystems.learnbraille.utils.application
import com.github.braillesystems.learnbraille.utils.checkedToast
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LessonsListFragment : AbstractFragment() {

    private val theoryRepository: TheoryRepository by inject()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsListBinding>(
        inflater,
        R.layout.fragment_lessons_list,
        container,
        false
    ).apply {

        lifecycleScope.launch {
            val curr = theoryRepository.currentStep(COURSE.id)
            val lessons = theoryRepository.allCourseLessons(COURSE.id)
            val last = theoryRepository.lastCourseStep(curr.courseId)
            val activeListener = object : LessonItemListener {
                override fun onClick(item: Lesson) = toLastLessonStep(COURSE.id, item.id)
            }
            val disabledListener = object : LessonItemListener {
                override fun onClick(item: Lesson) =
                    checkedToast(
                        getString(R.string.lessons_not_available_lesson)
                            .format(item.id, curr.lessonId)
                    )
            }
            val adapter = LessonsListAdapter(lessons) { item ->
                lesson = item
                lessonName.text = "${item.id}. ${item.name}"
                lessonName.setTypeface(
                    lessonName.typeface,
                    if (item.id == last.lessonId) Typeface.BOLD
                    else Typeface.NORMAL
                )
                if (preferenceRepository.teacherModeEnabled || item.id <= curr.lessonId) {
                    clickListener = activeListener
                    lessonName.setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.colorOnBackgroundDark
                        )
                    )
                    lessonState.setImageResource(R.drawable.unlocked)
                } else {
                    clickListener = disabledListener
                    lessonName.setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.colorOnBackgroundLight
                        )
                    )
                    lessonState.setImageResource(R.drawable.locked)
                }
            }
            lessonsList.adapter = adapter
        }

    }.root
}

private class LessonsListAdapter(
    private val lessons: List<Lesson>,
    private val bind: LessonsListItemBinding.(Lesson) -> Unit
) : RecyclerView.Adapter<LessonItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LessonItemViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.lessons_list_item,
            parent, false
        )
    )

    override fun getItemCount(): Int = lessons.size

    override fun onBindViewHolder(holder: LessonItemViewHolder, position: Int) {
        val item = lessons[position]
        holder.binding.bind(item)
    }
}

private class LessonItemViewHolder(
    val binding: LessonsListItemBinding
) : RecyclerView.ViewHolder(binding.root)

interface LessonItemListener {
    fun onClick(item: Lesson)
}
