package ru.itis.team1.summer2023.lab

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import ru.itis.team1.summer2023.lab.databinding.FragmentRulesBinding


class RulesFragment: Fragment(R.layout.fragment_rules) {
    private var clickCount = 0
    private var binding: FragmentRulesBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRulesBinding.bind(view)
        val pref: SharedPreferences = requireContext().getSharedPreferences("Default", MODE_PRIVATE)
        binding?.apply {
            etM1Player.setOnFocusChangeListener{_, _ -> tiM1Player.isHintEnabled = false}
            if (pref.getBoolean("IS_CHAT_COMPLETED", false)) {
                val name = pref.getString("PLAYER_NAME", "Hacker")!!
                val messages: HashMap<TextView, ImageView> = HashMap()
                messages[tvM2Oldman] = ivOldman2
                messages[tvM2Player] = ivPlayer2
                messages[tvM3Player] = ivPlayer3
                messages[tvM3Oldman] = ivOldman3
                restoreMessages(messages)
                setIndividualMessage(tvM2Oldman, name)
                setIndividualMessage(tvM3Oldman, name)
                blockInputting(etM1Player)
                navigateButton(btnRespond)
            } else {
                btnRespond.setOnClickListener {
                    val name = etM1Player.text.toString()
                    if (clickCount == 1) {
                        showMessage(tvM2Player, ivPlayer2)
                        showMessage(tvM3Oldman, ivOldman3)
                        clickCount++
                    } else if (clickCount >= 2) {
                        showMessage(tvM3Player, ivPlayer3)
                        clickCount++
                        pref.edit {
                            putBoolean("IS_CHAT_COMPLETED", true)
                        }
                        navigateButton(btnRespond)
                        // from now on the button must bring Player back to the Main screen
                    }
                    if (checkIfValidName(name) && clickCount == 0) {
                        pref.edit {
                            putString("PLAYER_NAME", etM1Player.text.toString())
                        }
                        blockInputting(etM1Player)
                        showMessage(tvM2Oldman, ivOldman2)
                        setIndividualMessage(tvM2Oldman, name)
                        setIndividualMessage(tvM3Oldman, name)
                        clickCount++
                    } else if (clickCount == 0) {
                        showSnackbar(view)
                    }
                }
            }
        }
    }

    private fun blockInputting(view: TextInputEditText) {
        view.isFocusable = false
    }

    private fun navigateButton(button: MaterialButton) {
        button.text = resources.getString(R.string.got_it_message)
        button.setOnClickListener{
            findNavController().navigate(R.id.action_rulesFragment_to_mainFragment)
        }
    }
    private fun restoreMessages(messages: HashMap<TextView, ImageView>) {
        messages.forEach { (m, i) ->  showMessage(m, i)}
    }
    private fun showSnackbar(view: View) {
        val snackbar = Snackbar.make(view, "The username has a min length of 1 and a max length of 25 and" +
                " can only use letters of Latin alphabet and digits", Snackbar.LENGTH_LONG)
        snackbar.setDuration(8000).setTextMaxLines(4).show()
    }

    private fun setIndividualMessage(view: TextView, username: String) {
        view.text = view.text.toString().replace("Name", username)
    }
    private fun showMessage(message: View, image: View) {
        message.visibility = View.VISIBLE
        image.visibility = View.VISIBLE
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

