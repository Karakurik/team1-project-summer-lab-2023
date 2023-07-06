package ru.itis.team1.summer2023.lab

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


class GameFragment : Fragment(R.layout.fragment_game) {

    private var binding: FragmentGameBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGameBinding.bind(view)

        binding?.run {

            val list: List<TextInputEditText> = listOf(
                layoutWord1.letter1.etLetter,
                layoutWord1.letter2.etLetter,
                layoutWord1.letter3.etLetter,
                layoutWord1.letter4.etLetter,
                layoutWord1.letter5.etLetter
            )

            layoutWord1.letter1.etLetter.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (layoutWord1.letter1.etLetter.text.toString().isNotEmpty()) {
                            layoutWord1.letter2.etLetter.requestFocus()
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {
//                        layoutWord1.letter1.etLetter.inputType = InputType.TYPE_NULL
//                               TODO: set colors
                    }
                })


        }

    }

    private fun getNext() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
