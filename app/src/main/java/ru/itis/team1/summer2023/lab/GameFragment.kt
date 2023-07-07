package ru.itis.team1.summer2023.lab


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import ru.itis.team1.summer2023.lab.databinding.FragmentGameBinding
import ru.itis.team1.summer2023.lab.databinding.WordBinding


class GameFragment : Fragment(R.layout.fragment_game) {

    private var binding: FragmentGameBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGameBinding.bind(view)

        binding?.run {
            val wordsList: List<WordBinding> = listOf(
                layoutWord1,
                layoutWord2,
                layoutWord3,
                layoutWord4,
                layoutWord5,
                layoutWord6
            )

            wordsList.forEach { word ->
                val lettersList: List<TextInputEditText> = listOf(
                    word.letter1.etLetter,
                    word.letter2.etLetter,
                    word.letter3.etLetter,
                    word.letter4.etLetter,
                    word.letter5.etLetter,
                    word.letter6.etLetter
                )

                lettersList[0].isEnabled = true

//                количество видимых элементов
                val size = 5

//                ответ
                val str = "HELLO"
                val userInput = java.lang.StringBuilder()

                for (i in 0 until size) {
                    lettersList[i].addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                        }

                        override fun onTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                            if (i < size - 1) {
                                lettersList[i + 1].isEnabled = true
                                if (lettersList[i].text.toString().isNotEmpty()) {
                                    lettersList[i + 1].requestFocus()
                                }
                            }
                        }

                        override fun afterTextChanged(p0: Editable?) {
                            lettersList[i].isEnabled = false
                            userInput.append(lettersList[i].text.toString())
                            if (i == size - 1) {
                                if (verifyWord(userInput.toString())) {
                                    convertWord(lettersList, str)
                                } else {
                                    clearWord(lettersList)
                                }
                            }
                        }
                    })
                }
            }
        }

    }

    private fun convertWord(lettersList: List<TextInputEditText>, answer: String) {
        for ((i, letter) in lettersList.withIndex()) {

            val value = letter.text.toString()

            if (answer.contains(value, true)) {
                var flag = false
                var index = -1
                do {
                    index = answer.indexOf(value, index + 1, true)
                    if (index == i) {
                        letter.setTextColor(resources.getColor(R.color.bull, context?.theme))
                        flag = true
                    }
                } while (index != -1 && !flag)

                if (!flag) {
                    letter.setTextColor(resources.getColor(R.color.cow, context?.theme))
                }

            } else {
                letter.setTextColor(resources.getColor(R.color.miss, context?.theme))
            }

        }
    }

    private fun clearWord(lettersList: List<TextInputEditText>) {
        lettersList.forEach { letter ->
            letter.text?.clear()
        }
        lettersList[0].isEnabled = true
        lettersList[0].requestFocus()
    }


    private fun verifyWord(userInput: String): Boolean {
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
