package ru.itis.team1.summer2023.lab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
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
                    show(ivOldman2)
                    show(tvM2Oldman)
                    if (clickCount == 1) {
                        show(ivPlayer2)
                        show(tvM2Player)
                        show(ivOldman3)
                        show(tvM3Oldman)
                    } else if (clickCount == 2) {
                        show(ivPlayer3)
                        show(tvM3Player)
                        btnRespond.text = resources.getString(R.string.got_it_message)
                        // from now on the button must bring Player back to the Main screen
                    }
                    clickCount++
                } else {
                    showSnackbar(view)
                }
            }
        }
    }

    private fun showSnackbar(view: View) {
        val snackbar = Snackbar.make(view, "The username has a min length of 1 and a max length of 25 and" +
                " can only use letters of Latin alphabet and digits", Snackbar.LENGTH_LONG)
        snackbar.setDuration(8000).setTextMaxLines(4).show()
    }

    private fun show(view: View) {
        view.visibility = View.VISIBLE
    }

    private fun checkIfValid(name: String): Boolean {
        val regex = "[A-Za-z0-9]+".toRegex()
        return regex.matches(name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
