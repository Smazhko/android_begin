package ru.gb.flagquiz.data

data object QuizStorage {
    val questions: MutableList<Question> = mutableListOf(
        Question(
            "Вопрос 1",
            "flag_br",
            "Болгария",
            "Бразилия",
            "Аргентина",
            "Марокко",
            "Бразилия"
        ),
        Question(
            "Вопрос 2",
            "flag_hr",
            "Чехия",
            "Португалия",
            "Словакия",
            "Хорватия",
            "Хорватия"
        ),
        Question(
            "Вопрос 3",
            "flag_nz",
            "Новая Гвинея",
            "Австралия",
            "Новая Зеландия",
            "Великобритания",
            "Новая Зеландия"
        )
    )

//    fun updateQuestion(updatedQuestion: Question) {
//        val index = questions.indexOfFirst { it.label == updatedQuestion.label }
//        if (index != -1) {
//            questions[index] = updatedQuestion
//        }
//    }
}