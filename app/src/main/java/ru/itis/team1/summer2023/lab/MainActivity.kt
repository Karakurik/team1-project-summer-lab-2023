package ru.itis.team1.summer2023.lab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val anim = AnimationUtils.loadAnimation(this, R.anim.scan_animation)
        anim.setInterpolator(this, android.R.anim.linear_interpolator)
        findViewById<ImageView>(R.id.scan_line).startAnimation(anim)
    }
}
