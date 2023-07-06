package ru.itis.team1.summer2023.lab

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.itis.team1.summer2023.lab.databinding.ItemWordBinding

class WordItem(
    private val binding: ItemWordBinding,
) : ViewHolder(binding.root) {

    fun onBind(word: String) {
        binding.run {
            tvWord.text = word
        }
    }
}
