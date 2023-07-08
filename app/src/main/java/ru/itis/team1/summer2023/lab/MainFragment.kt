package ru.itis.team1.summer2023.lab

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import ru.itis.team1.summer2023.lab.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {
    private var binding: FragmentMainBinding? = null
    private var selectedDifficulty = Difficulty.NORMAL

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        changeDifficulty(selectedDifficulty)

        val pref: SharedPreferences = requireContext().getSharedPreferences("Default",
            Context.MODE_PRIVATE
        )
        if (pref.getString("PLAYER_NAME", "Hacker") == "Hacker") {
            findNavController().navigate(R.id.action_mainFragment_to_rulesFragment)
        }

        binding?.apply {
            btnEasy.setOnClickListener { changeDifficulty(Difficulty.EASY) }
            btnNormal.setOnClickListener { changeDifficulty(Difficulty.NORMAL) }
            btnHard.setOnClickListener { changeDifficulty(Difficulty.HARD) }

            btnProfile.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
            }
            btnStart.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_gameFragment)
            }
            btnRules.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_rulesFragment)
            }
        }
    }

    private fun changeDifficulty(newDifficulty: Difficulty) {
        var btn = buttonOfDifficulty(selectedDifficulty)
        btn?.apply {
            backgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.element_bg))
            setTextColor(requireContext().getColor(R.color.primary))
        }

        btn = buttonOfDifficulty(newDifficulty)
        btn?.apply {
            backgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.primary))
            setTextColor(requireContext().getColor(R.color.element_bg))
        }

        selectedDifficulty = newDifficulty
    }

    private fun buttonOfDifficulty(difficulty: Difficulty): MaterialButton? = when (difficulty) {
        Difficulty.EASY -> binding?.btnEasy
        Difficulty.NORMAL -> binding?.btnNormal
        Difficulty.HARD -> binding?.btnHard
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
