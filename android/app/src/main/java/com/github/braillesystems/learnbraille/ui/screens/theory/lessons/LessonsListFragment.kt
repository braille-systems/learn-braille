package com.github.braillesystems.learnbraille.ui.screens.theory.lessons

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.braillesystems.learnbraille.COURSE_ID
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Lesson
import com.github.braillesystems.learnbraille.data.repository.TheoryRepository
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsListBinding
import com.github.braillesystems.learnbraille.databinding.LessonsListItemBinding
import com.github.braillesystems.learnbraille.utils.scope
import com.github.braillesystems.learnbraille.utils.title
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LessonsListFragment : Fragment() {

    private val theoryRepository: TheoryRepository by inject()

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

        title = getString(R.string.lessons_title_lessons_list)

        scope().launch {
            val lessons = theoryRepository.getAllCourseLessons(COURSE_ID)
            val adapter = LessonsListAdapter(lessons)
            lessonsList.adapter = adapter
        }

    }.root
}

private class LessonsListAdapter(private val lessons: List<Lesson>) :
    RecyclerView.Adapter<LessonItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LessonItemViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.lessons_list_item,
            parent, false
        )
    )

    override fun getItemCount(): Int = lessons.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LessonItemViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.view.apply {
            lessonName.text = "${lesson.id}. ${lesson.name}"
            lessonDescription.text = lesson.description.parseAsHtml()
        }
    }
}

private class LessonItemViewHolder(val view: LessonsListItemBinding) :
    RecyclerView.ViewHolder(view.root)
