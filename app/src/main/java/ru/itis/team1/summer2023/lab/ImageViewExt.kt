package ru.itis.team1.summer2023.lab

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView

fun ImageView.setScaledAliasedImageResource(resId: Int, width: Int, height: Int) {
    var bmp = BitmapFactory.decodeResource(resources, resId)
    bmp = Bitmap.createScaledBitmap(bmp, width, height, false)
    setImageBitmap(bmp)
}
