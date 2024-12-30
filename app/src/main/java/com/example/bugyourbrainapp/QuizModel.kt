package com.example.bugyourbrainapp

data class QuizModel(
    val id : String,
    val title : String,
    val subtitle : String,
    val time : String,
    val questionList : List<QuestionModel>
){
    constructor() : this("","","","", emptyList())
    fun isValid(): Boolean {
        if (id.isBlank() || title.isBlank() || subtitle.isBlank() || time.isBlank()) return false
        if (questionList.isEmpty()) return false
        return questionList.all { it.isValid() }
    }
}

data class QuestionModel(
    val question : String,
    val options : List<String>,
    val correct : String,
){
    fun isValid(): Boolean {
        if (question.isBlank() || options.size < 2 || correct.isBlank()) return false
        return true
    }

    constructor() : this ("", emptyList(),"")
}

data class QuizResult(
    val username: String = "",
    val score: Int = 0,
    val percentage: Int = 0,
    val status: String = " ",
    val quizTitle: String = " "
)
