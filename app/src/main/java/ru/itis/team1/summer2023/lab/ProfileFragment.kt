package ru.itis.team1.summer2023.lab

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.itis.team1.summer2023.lab.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var binding: FragmentProfileBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        binding?.apply {
            requireActivity().getPreferences(Context.MODE_PRIVATE).run {
                // TODO: decide on key names and maybe move them into values/strings.xml
                tvPlayerName.text = getString("PLAYER_NAME", "playername")
                tvTrophies1.text = getInt("GOLD_TROPHIES", 0).toString()
                tvTrophies2.text = getInt("SILVER_TROPHIES", 0).toString()
                tvTrophies3.text = getInt("BRONZE_TROPHIES", 0).toString()
                val totalGames = getInt("TOTAL_GAMES", 0)
                tvTotal.text = getString(R.string.total_games_text, totalGames)
                val gamesWonPercentage = when (totalGames) {
                    0 -> 0.0
                    else -> getInt("GAMES_WON", 0).toDouble() / totalGames.toDouble() * 100.0
                }
                tvPercentage.text =
                    getString(R.string.games_won_text, gamesWonPercentage)
            }

            ivTrophy1.setScaledAliasedImageResource(R.drawable.trophy1, 24, 24)
            ivTrophy2.setScaledAliasedImageResource(R.drawable.trophy2, 24, 24)
            ivTrophy3.setScaledAliasedImageResource(R.drawable.trophy3, 24, 24)

            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnDictionary.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_dictionaryFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
