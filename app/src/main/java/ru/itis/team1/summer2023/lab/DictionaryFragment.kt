package ru.itis.team1.summer2023.lab

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.itis.team1.summer2023.lab.databinding.FragmentDictionaryBinding


class DictionaryFragment : Fragment(R.layout.fragment_dictionary) {
    private var binding: FragmentDictionaryBinding? = null
    private var adapter: WordAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDictionaryBinding.bind(view)

        initAdapter()

        if (adapter?.itemCount == 0) {
            binding?.tvEmptyList?.visibility = View.VISIBLE
        }

        binding?.btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() {
        val activity = requireActivity() as MainActivity
        val bundle = activity.getDictionary()
        val list = requireActivity()
            .getPreferences(Context.MODE_PRIVATE)
            .getOrderedStringCollection(getString(R.string.pref_key_found_words))
            .map{
                word -> word + " - " + bundle.getString(word)
            }

        adapter = WordAdapter(list)
        binding?.rvWords?.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
