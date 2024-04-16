package ru.gb.flagquiz

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ru.gb.flagquiz.data.Question
import ru.gb.flagquiz.data.QuizStorage
import ru.gb.flagquiz.databinding.LayoutQuestionBinding

class QuestionLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context,attrs){
    private val binding: LayoutQuestionBinding
    private var rightAnswer: String = ""
    private var userAnswer: String = ""

    init{
        val inflatedView = inflate(context, R.layout.layout_question, this)
        binding = LayoutQuestionBinding.bind(inflatedView)
    }

    fun setData(question: Question){
        binding.questNumber.text = question.label

        val resourceId = resources.getIdentifier(question.imgSrc, "drawable", "ru.gb.flagquiz")
        val srcImg: Drawable? = if (resourceId != 0) {
            ContextCompat.getDrawable(context, resourceId)
        } else {
            null
        }
        binding.imgFlag.setImageDrawable(srcImg)

        binding.rbVariant1.text = question.ans1
        binding.rbVariant2.text = question.ans2
        binding.rbVariant3.text = question.ans3
        binding.rbVariant4.text = question.ans4
        rightAnswer = question.rightAnswer

        binding.rbGroup.setOnCheckedChangeListener{ _, buttonId ->
            userAnswer = findViewById<RadioButton>(buttonId).text.toString()
            question.userAnswer = userAnswer
//            Snackbar.make(this, question.toString(), Snackbar.LENGTH_LONG).show()
        }
    }

    fun isUserRight(): Boolean {
        return userAnswer.equals(rightAnswer)
    }
}