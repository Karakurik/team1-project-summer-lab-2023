package ru.itis.team1.summer2023.lab

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
                var flag = false
//                TODO ломается когда есть скрытые элементы
                for ((i, letter) in lettersList.withIndex()) {
                    if (letter.visibility == View.VISIBLE

                    ) {
                        if ((i + 1) < lettersList.size
                            && lettersList[i + 1].visibility == View.VISIBLE
                        ) {
                            letter.addTextChangedListener(object : TextWatcher {
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
                                    if (letter.text.toString().isNotEmpty()) {
                                        letter.focusSearch(View.FOCUS_RIGHT).requestFocus()
                                    }
                                }

                                override fun afterTextChanged(p0: Editable?) {}
                            })
                        } else while (!flag) {
                            if (letter.text.toString().isNotEmpty()) flag = true
                        }
                    }
                }

                val str = "HELLO"

                if (verifyWord() && flag) {
                    convertWord(lettersList, str)
                } else {

                }
            }
        }

    }

    private fun convertWord(lettersList: List<TextInputEditText>, answer: String) {
        for ((i, letter) in lettersList.withIndex()) {
            letter.inputType = InputType.TYPE_NULL
            val value: String = letter.text.toString()
            if (answer.contains(value)) {
                if (answer.indexOf(value) == i) {
                    letter.setTextColor(resources.getColor(R.color.bull))
                } else {
                    letter.setTextColor(resources.getColor(R.color.cow))
                }
            } else {
                letter.setTextColor(resources.getColor(R.color.miss))
            }
        }
    }

    private fun verifyWord(): Boolean {
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
