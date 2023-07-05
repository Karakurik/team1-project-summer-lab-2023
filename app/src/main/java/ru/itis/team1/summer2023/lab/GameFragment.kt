package ru.itis.team1.summer2023.lab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class GameFragment : Fragment(R.layout.fragment_game) {

//    private var binding: FragmentFirstBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding = FragmentFirstBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
//        binding = null
    }

}
