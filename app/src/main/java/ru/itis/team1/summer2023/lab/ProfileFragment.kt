package ru.itis.team1.summer2023.lab

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
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
                    else -> getInt("GAMES_WON", 0).toDouble() / totalGames.toDouble()
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
                // TODO: navigate to dictionary fragment
            }
        }
    }

    private fun ImageView.setScaledAliasedImageResource(resId: Int, width: Int, height: Int) {
        var bmp = BitmapFactory.decodeResource(resources, resId)
        bmp = Bitmap.createScaledBitmap(bmp, width, height, false)
        setImageBitmap(bmp)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
