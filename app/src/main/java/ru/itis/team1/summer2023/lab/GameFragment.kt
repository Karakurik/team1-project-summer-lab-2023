package ru.itis.team1.summer2023.lab


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.itis.team1.summer2023.lab.Difficulty.*
import ru.itis.team1.summer2023.lab.databinding.*
import java.util.TreeSet


class GameFragment : Fragment(R.layout.fragment_game) {

    private var binding: FragmentGameBinding? = null

    private val dictionary = HashSet<String>()
    private var difficulty = NORMAL
    private var isWin = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGameBinding.bind(view)
        difficulty = arguments?.get(DIFFICULTY) as Difficulty

        val size = when (difficulty) {
            EASY -> 4
            NORMAL -> 5
            HARD -> 6
        }

        createDict()

        val answer = generateAnswer()

        binding?.run {

            val wordsList = listOf(
                layoutWord1,
                layoutWord2,
                layoutWord3,
                layoutWord4,
                layoutWord5,
                layoutWord6
            )
            val iterator = wordsList.listIterator()
            openNextWord(iterator)

            wordsList.forEach { word ->

                val lettersBindingList: MutableList<InputLetterBinding> = mutableListOf(
                    word.letter1,
                    word.letter2,
                    word.letter3,
                    word.letter4,
                    word.letter5,
                    word.letter6
                )

                adjustDifficulty(lettersBindingList)

                val lettersList: List<TextInputEditText> =
                    lettersBindingList.map { letter -> letter.etLetter }

                val userInput = java.lang.StringBuilder()

                for (i in 0 until size) {

                    lettersList[i].setOnKeyListener(object : View.OnKeyListener {
                        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
                            if (keyCode == KeyEvent.KEYCODE_DEL && event?.action == KeyEvent.ACTION_UP && i > 0) {
                                lettersList[i - 1].text?.clear()
                                lettersList[i - 1].isEnabled = true
                                lettersList[i - 1].requestFocus()
                                lettersList[i].isEnabled = false
                                return true
                            }
                            return false
                        }

                    })

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
                            if (p0.isNullOrEmpty()) {
                                userInput.deleteCharAt(userInput.length - 1)
                            } else {
                                userInput.append(lettersList[i].text.toString())
                            }
                            if (i == size - 1) {
                                if (verifyWord(userInput.toString())) {

                                    convertWord(lettersList, answer)
                                    if (!isWin && word == wordsList[wordsList.size - 1]) {
                                        finishGame(false, answer)

                                    }
                                    openNextWord(iterator)
                                } else {
                                    clearWord(lettersList)
                                }
                                userInput.clear()
                            }
                        }
                    })

                    lettersList[i].setOnFocusChangeListener { v, hasFocus ->
                        val ti = v.parent.parent as TextInputLayout // first parent - FrameLayout
                        val colorId = if (hasFocus) R.color.element_bg_focused else R.color.element_bg
                        val color = ContextCompat.getColor(requireContext(), colorId)
                        ti.setBackgroundColor(color)
                    }
                }
            }
        }

    }

    private fun createDict() {
        val inputStream = when (difficulty) {
            EASY -> requireContext().assets.open("words_4")
            NORMAL -> requireContext().assets.open("words_5")
            HARD -> requireContext().assets.open("words_6")
        }
        inputStream.bufferedReader().forEachLine {
            dictionary.add(it)
        }
    }

    private fun getAnswerDefinition(answer: String): String? {
        val activity = requireActivity() as MainActivity
        return activity.getDictionary().getString(answer)
    }

    private fun generateAnswer(): String {
        requireActivity().getPreferences(Context.MODE_PRIVATE).run {
            val foundWords = getOrderedStringCollection("FOUND_WORDS")
            val key = when (difficulty) {
                EASY -> "GOLD_TROPHIES"
                NORMAL -> "SILVER_TROPHIES"
                HARD -> "BRONZE_TROPHIES"
            }
            if (getInt(key, 0) > dictionary.size) {
//                TODO
            }
            var str: String
            while (true) {
                str = dictionary.random()
                if (!foundWords.contains (str)) {
                    return str
                }
            }
        }
    }

    private fun adjustDifficulty(lettersBindingList: MutableList<InputLetterBinding>) {
        if (difficulty == NORMAL) {
            lettersBindingList[5].root.visibility = View.GONE
            lettersBindingList.removeAt(5)
        } else if (difficulty == EASY) {
            lettersBindingList[5].root.visibility = View.GONE
            lettersBindingList[4].root.visibility = View.GONE
            lettersBindingList.removeAt(5)
            lettersBindingList.removeAt(4)
        }

    }

    private fun openNextWord(iterator: ListIterator<WordBinding>) {
        if (iterator.hasNext()) {
            iterator.next().letter1.etLetter.isEnabled = true
        }
    }

    private fun convertWord(lettersList: List<TextInputEditText>, answer: String) {
        var isAnswer = true
        for ((i, letter) in lettersList.withIndex()) {

            val value = letter.text.toString()

            if (answer.contains(value, true)) {
                var flag = false
                var index = -1
                do {
                    index = answer.indexOf(value, index + 1, true)
                    if (index == i) {
                        letter.setTextColor(requireContext().getColor(R.color.bull))
                        flag = true
                    }
                } while (index != -1 && !flag)

                if (!flag) {
                    isAnswer = false
                    letter.setTextColor(requireContext().getColor(R.color.cow))
                }

            } else {
                isAnswer = false
                letter.setTextColor(requireContext().getColor(R.color.miss))
            }

        }
        if (isAnswer) {
            finishGame(true, answer)
            isWin = true
        }
    }

    private fun finishGame(isWin: Boolean, answer: String) {
        requireActivity().getPreferences(Context.MODE_PRIVATE).run {
            val total = getInt("TOTAL_GAMES", 0) + 1
            edit { putInt("TOTAL_GAMES", total) }

            if (isWin) {
                val won = getInt("GAMES_WON", 0) + 1
                val trophiesKey = when (difficulty) {
                    EASY -> "BRONZE_TROPHIES"
                    NORMAL -> "SILVER_TROPHIES"
                    HARD -> "GOLD_TROPHIES"
                }
                val trophies = getInt(trophiesKey, 0) + 1
                val set = TreeSet(getOrderedStringCollection("FOUND_WORDS"))
                set.add(answer)


                edit {
                    putInt("GAMES_WON", won)
                    putInt(trophiesKey, trophies)
                    putOrderedStringCollection("FOUND_WORDS", set)
                }
            }
        }

        // TODO: add more responses
        val oldmanResponseId: Int = if (isWin) {
            listOf(
                R.string.game_win_text_1
            ).random()
        } else {
            listOf(
                R.string.game_lose_text_1
            ).random()
        }
        var oldmanResponse = resources.getString(oldmanResponseId)
        if (isWin) {
            val definition = answer + " - " + (requireActivity() as MainActivity).getDictionary().getString(answer)
            oldmanResponse = oldmanResponse.format(definition)
        }

        binding?.layoutOverlay?.run {
            tvTitle.text = resources.getString(if (isWin) R.string.victory_text else R.string.defeat_text)
            tvOldman.text = oldmanResponse

            if (isWin) {
                val trophyResId = when (difficulty) {
                    EASY -> R.drawable.trophy3
                    NORMAL -> R.drawable.trophy2
                    HARD -> R.drawable.trophy1
                }
                ivTrophy.setScaledAliasedImageResource(trophyResId, 24, 24)
            } else {
                llTrophy.visibility = View.GONE
            }

            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            root.visibility = View.VISIBLE
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
        return dictionary.contains(userInput.lowercase())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {

        private const val DIFFICULTY = "DIFFICULTY"
        fun createBundle(selectedDifficulty: Difficulty): Bundle {
            return bundleOf(DIFFICULTY to selectedDifficulty)
        }
    }

}
