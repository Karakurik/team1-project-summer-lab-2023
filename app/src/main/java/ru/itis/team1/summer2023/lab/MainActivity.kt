package ru.itis.team1.summer2023.lab

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf


class MainActivity : AppCompatActivity() {

    private lateinit var dictionary: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val anim = AnimationUtils.loadAnimation(this, R.anim.scan_animation)
        anim.setInterpolator(this, android.R.anim.linear_interpolator)
        findViewById<ImageView>(R.id.scan_line).startAnimation(anim)

        dictionary = createDictionary()

    }

    private fun createDictionary(): Bundle {
        val dictionary =  HashMap<String, String>()

        var str: List<String>
        assets.open("dictionary").bufferedReader().forEachLine {
            str = it.split("=")
            dictionary[str[0]] = str[1]
        }
        return dictionary.toBundle()
    }

    private fun Map<String, String>.toBundle(): Bundle {
        val pairs = this.map { entry ->
            Pair(entry.key, entry.value)
        }.toTypedArray()
        return bundleOf(*pairs)
    }

    fun getDictionary(): Bundle {
        return dictionary
    }

}
