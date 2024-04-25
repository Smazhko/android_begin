package ru.gb.flagquiz.data

import android.content.Context
import ru.gb.flagquiz.R

data class QuizStorage(private val context: Context) {

    private val labels : Array<String> = context.resources.getStringArray(R.array.questions_titles)
    private val quest1Answers: Array<String> = context.resources.getStringArray(R.array.quest1_answers)
    private val quest2Answers: Array<String> = context.resources.getStringArray(R.array.quest2_answers)
    private val quest3Answers: Array<String> = context.resources.getStringArray(R.array.quest3_answers)

    val questions: MutableList<Question> = mutableListOf(
        Question(
            labels[0],
            "flag_br",
            quest1Answers[0],
            quest1Answers[1],
            quest1Answers[2],
            quest1Answers[3],
            quest1Answers[4]
        ),
        Question(
            labels[1],
            "flag_hr",
            quest2Answers[0],
            quest2Answers[1],
            quest2Answers[2],
            quest2Answers[3],
            quest2Answers[4]
        ),
        Question(
            labels[2],
            "flag_nz",
            quest3Answers[0],
            quest3Answers[1],
            quest3Answers[2],
            quest3Answers[3],
            quest3Answers[4]
        )
    )
}

//    fun updateQuestion(updatedQuestion: Question) {
//        val index = questions.indexOfFirst { it.label == updatedQuestion.label }
//        if (index != -1) {
//            questions[index] = updatedQuestion
//        }
//    }
