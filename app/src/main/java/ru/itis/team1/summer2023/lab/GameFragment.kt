package ru.itis.team1.summer2023.lab


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import ru.itis.team1.summer2023.lab.Difficulty.*
import ru.itis.team1.summer2023.lab.databinding.FragmentGameBinding
import ru.itis.team1.summer2023.lab.databinding.InputLetterBinding
import ru.itis.team1.summer2023.lab.databinding.WordBinding
import java.util.TreeSet


class GameFragment : Fragment(R.layout.fragment_game) {

    private var binding: FragmentGameBinding? = null
    private val dictionary = HashMap<String, String>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGameBinding.bind(view)

        val difficulty = arguments?.get(DIFFICULTY) as Difficulty
        val size = when (difficulty) {
            EASY -> 4
            NORMAL -> 5
            HARD -> 6
        }
        createDict(difficulty)
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

                val lettersBindingList: List<InputLetterBinding> = listOf(
                    word.letter1,
                    word.letter2,
                    word.letter3,
                    word.letter4,
                    word.letter5,
                    word.letter6
                )

                adjustDifficulty(lettersBindingList, difficulty)

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
                                    convertWord(lettersList, answer, difficulty)
                                    if (word == wordsList[wordsList.size - 1]) {
                                        finishGame(false, answer, difficulty)
                                    }
                                    openNextWord(iterator)
                                } else {
                                    clearWord(lettersList)
                                }
                                userInput.clear()
                            }
                        }
                    })
                }
            }
        }

    }

    private fun createDict(difficulty: Difficulty) {
        val inputStream = when (difficulty) {
            EASY -> requireContext().assets.open("dictionary_4")
            NORMAL -> requireContext().assets.open("dictionary_5")
            HARD -> requireContext().assets.open("dictionary_6")
        }
        var str: List<String>
        inputStream.bufferedReader().forEachLine {
            str = it.split("=")
            dictionary[str[0]] = str[1]
        }
    }

    private fun generateAnswer(): String {
        val foundWords = requireActivity()
            .getPreferences(Context.MODE_PRIVATE)
            .getOrderedStringCollection("FOUND_WORDS")
//            TODO проверить что остались неотгаданные слова
        var str: String
        while (true) {
            str = dictionary.keys.random()
            if (!foundWords.contains (str)) {
                return str
            }
        }
    }

    private fun adjustDifficulty(
        lettersBindingList: List<InputLetterBinding>,
        difficulty: Difficulty
    ) {
        if (difficulty == NORMAL) {
            lettersBindingList[5].root.visibility = View.GONE
        } else if (difficulty == EASY) {
            lettersBindingList[4].root.visibility = View.GONE
            lettersBindingList[5].root.visibility = View.GONE
        }

    }

    private fun openNextWord(iterator: ListIterator<WordBinding>) {
        if (iterator.hasNext()) {
            iterator.next().letter1.etLetter.isEnabled = true
        }
    }

    private fun convertWord(lettersList: List<TextInputEditText>, answer: String, difficulty: Difficulty) {
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
            finishGame(true, answer, difficulty)
        }
    }

    private fun finishGame(isWin: Boolean, answer: String, difficulty: Difficulty) {
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
                // TODO: add definitions to answers
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
        if (isWin) oldmanResponse = oldmanResponse.format(answer)

        binding?.layoutOverlay?.run {
            tvTitle.text = resources.getString(if (isWin) R.string.victory_text else R.string.defeat_text)
            tvOldman.text = oldmanResponse

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
        return dictionary.keys.contains(userInput.lowercase())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val DIFFICULTY = "DIF"
        fun createBundle(selectedDifficulty: Difficulty): Bundle {
            return bundleOf(DIFFICULTY to selectedDifficulty)
        }
    }

}
