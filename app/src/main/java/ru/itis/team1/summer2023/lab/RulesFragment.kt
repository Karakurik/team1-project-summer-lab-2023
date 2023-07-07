package ru.itis.team1.summer2023.lab

import android.os.Bundle
import android.view.View
import android.widget.TextView
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
                val name = etM1Player.text.toString()
                if (clickCount == 1) {
                    show(ivPlayer2)
                    show(tvM2Player)
                    show(ivOldman3)
                    show(tvM3Oldman)
                    clickCount++
                } else if (clickCount == 2) {
                    show(ivPlayer3)
                    show(tvM3Player)
                    btnRespond.text = resources.getString(R.string.got_it_message)
                    clickCount++
                    // from now on the button must bring Player back to the Main screen
                }
                if (checkIfValidName(name) && clickCount == 0) {
                    etM1Player.isFocusable = false
                    show(ivOldman2)
                    show(tvM2Oldman)
                    setIndividualMessage(tvM2Oldman, name)
                    setIndividualMessage(tvM3Oldman, name)
                    clickCount++
                } else if (clickCount == 0) {
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

    private fun setIndividualMessage(view: TextView, username: String) {
        view.text = view.text.toString().replace("Name", username)
    }
    private fun show(view: View) {
        view.visibility = View.VISIBLE
    }

    private fun checkIfValidName(name: String): Boolean {
        val regex = "[A-Za-z0-9]+".toRegex()
        return regex.matches(name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
