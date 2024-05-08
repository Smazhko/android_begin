package ru.gb.myapplication

import android.content.Context
import android.view.View
import android.widget.Toast

object Helper {
    @JvmStatic
    fun onClick(c: Context) {
        Toast.makeText(c, "on click", Toast.LENGTH_SHORT).show()
    }
    @JvmStatic
    fun onTextChanged(v: View, string: String) {
        Toast.makeText(v.context, "on text $string", Toast.LENGTH_SHORT).show()
    }
    @JvmStatic
    fun onChecked(v: View, checked: Boolean) {
        Toast.makeText(v.context, "on checked $checked", Toast.LENGTH_SHORT).show()
    }
}