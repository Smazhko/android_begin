package ru.gb.flagquiz.data

class Question(
    val label: String,
    val imgSrc: String,
    val ans1: String,
    val ans2: String,
    val ans3: String,
    val ans4: String,
    val rightAnswer: String,
    var userAnswer:String = ""
) {
    override fun toString(): String {
        return "$label $rightAnswer $userAnswer"
    }
}