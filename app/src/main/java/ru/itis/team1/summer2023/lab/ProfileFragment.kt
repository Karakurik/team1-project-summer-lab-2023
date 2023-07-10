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
                val playerNameKey = getString(R.string.pref_key_player_name)
                val goldTrophiesKey = getString(R.string.pref_key_gold_trophies)
                val silverTrophiesKey = getString(R.string.pref_key_silver_trophies)
                val bronzeTrophiesKey = getString(R.string.pref_key_bronze_trophies)
                val totalGamesKey = getString(R.string.pref_key_total_games)
                val gamesWonKey = getString(R.string.pref_key_games_won)

                tvPlayerName.text = getString(playerNameKey, "playername")
                tvTrophies1.text = getInt(goldTrophiesKey, 0).toString()
                tvTrophies2.text = getInt(silverTrophiesKey, 0).toString()
                tvTrophies3.text = getInt(bronzeTrophiesKey, 0).toString()
                val totalGames = getInt(totalGamesKey, 0)
                tvTotal.text = getString(R.string.total_games_text, totalGames)
                val gamesWonPercentage = when (totalGames) {
                    0 -> 0.0
                    else -> getInt(gamesWonKey, 0).toDouble() / totalGames.toDouble() * 100.0
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
