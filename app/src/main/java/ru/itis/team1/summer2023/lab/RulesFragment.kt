package ru.itis.team1.summer2023.lab

import android.os.Bundle
import android.view.View
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import ru.itis.team1.summer2023.lab.databinding.FragmentRulesBinding


class RulesFragment: Fragment(R.layout.fragment_rules) {
    private var clickCount = 0
    private var binding: FragmentRulesBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRulesBinding.bind(view)
        binding?.apply {
            btnRespond.setOnClickListener{

                if (checkIfValid(etM1Player.text.toString())) {
                    show(tvM2Oldman)
                    if (clickCount == 1) {
                        show(tvM2Player)
                        show(tvM3Oldman)
                    } else if (clickCount == 2) {
                        show(tvM3Player)
                        btnRespond.text = "got it"
                    }
                    clickCount++
                }
            }
        }
    }

    private fun show(view: View) {
        view.visibility = View.VISIBLE
    }

    private fun checkIfValid(name: String): Boolean {
        val regex = "[A-Za-z]+".toRegex()
        return regex.matches(name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
