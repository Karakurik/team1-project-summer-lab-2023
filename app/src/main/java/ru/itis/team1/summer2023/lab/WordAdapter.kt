package ru.itis.team1.summer2023.lab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.itis.team1.summer2023.lab.databinding.ItemWordBinding

class WordAdapter(
    private var list: List<String>,
) : RecyclerView.Adapter<WordItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordItem =
        WordItem(
            ItemWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: WordItem, position: Int) = holder.onBind(list[position])

}
