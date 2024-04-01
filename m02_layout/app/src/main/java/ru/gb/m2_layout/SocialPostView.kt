package ru.gb.m2_layout

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.resourceinspection.annotation.Attribute

class SocialPostView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null):
LinearLayout(context,attrs){
    init{
        inflate(context, R.layout.social_post_view, this)
    }
}
