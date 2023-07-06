package ru.itis.team1.summer2023.lab

import android.content.SharedPreferences
import org.json.JSONArray
import java.util.LinkedList

fun SharedPreferences.Editor.putOrderedStringCollection(key: String, collection: Collection<String>) {
    val json = JSONArray(collection)
    putString(key, json.toString())
    apply()
}

fun SharedPreferences.getOrderedStringCollection(key: String): Collection<String> {
    val list = LinkedList<String>()
    val json = JSONArray(getString(key, "[]"))
    for (i: Int in 0 until json.length()) {
        list.add(json.getString(i))
    }
    return list
}
