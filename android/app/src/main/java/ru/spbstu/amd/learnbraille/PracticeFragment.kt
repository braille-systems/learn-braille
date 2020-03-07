package ru.spbstu.amd.learnbraille

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import ru.spbstu.amd.learnbraille.BrailleDot.E
import ru.spbstu.amd.learnbraille.BrailleDot.F
import ru.spbstu.amd.learnbraille.databinding.FragmentPracticeBinding
import timber.log.Timber

class PracticeFragment : Fragment() {

    private lateinit var binding: FragmentPracticeBinding
    private lateinit var vibrator: Vibrator
    private lateinit var dotCheckBoxes: Array<CheckBox>

    private var enteredDots = BrailleDots(E, E, E, E, E, E)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentPracticeBinding>(
        inflater, R.layout.fragment_practice, container, false
    ).apply {
        assert(dotsToRuLetters.size == 33) {
            "33 letters are in russian"
        }

        binding = this
        vibrator = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        dotCheckBoxes = arrayOf(
            dotButton1, dotButton2, dotButton3,
            dotButton4, dotButton5, dotButton6
        )

        val navigate = Navigation.createNavigateOnClickListener(
            R.id.action_practiceFragment_to_menuFragment
        )
        mainMenuButton.setOnClickListener(navigate)

        nextButton.setOnClickListener {
            if (checkRuLetter()) {
                correctAnswer()
            } else {
                incorrectAnswer()
            }
        }

        assert(dotCheckBoxes.size == 6)
        dotCheckBoxes.forEachIndexed { index, checkBox ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                val toSet = if (isChecked) F else E
                enteredDots = when (index + 1) {
                    1 -> enteredDots.copy(b1 = toSet)
                    2 -> enteredDots.copy(b2 = toSet)
                    3 -> enteredDots.copy(b3 = toSet)
                    4 -> enteredDots.copy(b4 = toSet)
                    5 -> enteredDots.copy(b5 = toSet)
                    6 -> enteredDots.copy(b6 = toSet)
                    else -> throw IllegalArgumentException(
                        "Wrong button index: $index, not in range from 0 to 5 inclusive"
                    )
                }
            }
        }

        updateCard()

    }.root

    private fun updateCard() {
        enteredDots = BrailleDots()
        binding.letter.text = randomRuLetter()
        clearCheckBoxes()
    }

    private fun clearCheckBoxes() = dotCheckBoxes.forEach {
        if (it.isChecked) {
            it.toggle()
        }
    }

    private fun correctAnswer() {
        // Logging should be before clearing checkboxes
        Timber.i("Correct: letter = ${binding.letter.text}, dots = $enteredDots")
        Toast.makeText(context, "Correct! :)", Toast.LENGTH_SHORT).show()
        updateCard()

        // todo check vibration
        vibrator.vibrate(correctVibrationDuration)  // Use deprecated for API level compatibility
    }

    private fun incorrectAnswer() {
        // Logging should be before clearing checkboxes
        Timber.i("Correct: letter = ${binding.letter.text}, dots = $enteredDots")
        Toast.makeText(context, "Incorrect! :(", Toast.LENGTH_SHORT).show()
        clearCheckBoxes()

        // todo check vibration
        vibrator.vibrate(incorrectVibrationDuration)  // Use deprecated for API level compatibility
    }

    private fun randomRuLetter() = dotsToRuLetters.values.random().toString()

    private fun checkRuLetter() = dotsToRuLetters[enteredDots].toString() == binding.letter.text

    private companion object {
        private const val correctVibrationDuration = 100L
        private const val incorrectVibrationDuration = 500L
        private val dotsToRuLetters = mapOf(
            BrailleDots(F, E, E, E, E, E) to 'А',
            BrailleDots(F, F, E, E, E, E) to 'Б',
            BrailleDots(E, F, E, F, F, F) to 'В',
            BrailleDots(F, F, E, F, F, E) to 'Г',
            BrailleDots(F, E, E, F, F, E) to 'Д',
            BrailleDots(F, E, E, E, F, E) to 'Е',
            BrailleDots(F, E, E, E, E, F) to 'Ё',
            BrailleDots(E, F, E, F, F, E) to 'Ж',
            BrailleDots(F, E, F, E, F, F) to 'З',
            BrailleDots(E, F, E, F, E, E) to 'И',
            BrailleDots(F, F, F, F, E, F) to 'Й',
            BrailleDots(F, E, F, E, E, E) to 'К',
            BrailleDots(F, F, F, E, E, E) to 'Л',
            BrailleDots(F, E, F, F, E, E) to 'М',
            BrailleDots(F, E, F, F, F, E) to 'Н',
            BrailleDots(F, E, F, E, F, E) to 'О',
            BrailleDots(F, F, F, F, E, E) to 'П',
            BrailleDots(F, F, F, E, F, E) to 'Р',
            BrailleDots(E, F, F, F, E, E) to 'С',
            BrailleDots(E, F, F, F, F, E) to 'Т',
            BrailleDots(F, E, F, E, E, F) to 'У',
            BrailleDots(F, F, E, F, E, E) to 'Ф',
            BrailleDots(F, F, E, E, F, E) to 'Х',
            BrailleDots(F, E, E, F, E, E) to 'Ц',
            BrailleDots(F, F, F, F, F, E) to 'Ч',
            BrailleDots(F, E, E, E, F, F) to 'Ш',
            BrailleDots(F, E, F, F, E, F) to 'Щ',
            BrailleDots(F, F, F, E, F, F) to 'Ъ',
            BrailleDots(E, F, F, F, E, F) to 'Ы',
            BrailleDots(E, F, F, F, F, F) to 'Ь',
            BrailleDots(E, F, E, F, E, F) to 'Э',
            BrailleDots(F, F, E, E, F, F) to 'Ю',
            BrailleDots(F, F, E, F, E, F) to 'Я'
        )
    }
}

private enum class BrailleDot {
    E,  // Empty
    F   // Filled
}

private data class BrailleDots(
    val b1: BrailleDot = E, val b2: BrailleDot = E, val b3: BrailleDot = E,
    val b4: BrailleDot = E, val b5: BrailleDot = E, val b6: BrailleDot = E
)
