package ru.spbstu.amd.learnbraille.screens.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentMenuBinding>(
        inflater,
        R.layout.fragment_menu,
        container,
        false
    ).apply {

        // TODO remove cast
        (activity as AppCompatActivity)
            .supportActionBar
            ?.title = getString(R.string.menu_actionbar_text)

        // TODO should be activity ecosystem be separate activity?
        lessonsButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_menuFragment_to_lessonFragment)
        )
        practiceButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_menuFragment_to_practiceFragment)
        )
        exitButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_menuFragment_to_exitFragment)
        )
    }.root
}
