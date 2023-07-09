package ru.itis.team1.summer2023.lab

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.itis.team1.summer2023.lab.databinding.FragmentDictionaryBinding
import java.util.ArrayList

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
        // TODO: decide on key
//        val collection = requireActivity()
//            .getPreferences(Context.MODE_PRIVATE)
//            .getOrderedStringCollection("FOUND_WORDS")
//        val list = ArrayList(collection)
        val activity = requireActivity() as MainActivity
        val bundle = activity.getDictionary()
        val list = requireActivity()
            .getPreferences(Context.MODE_PRIVATE)
            .getOrderedStringCollection("FOUND_WORDS")
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
