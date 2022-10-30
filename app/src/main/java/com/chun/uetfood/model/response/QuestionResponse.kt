package com.chun.uetfood.model.response

class QuestionResponse {
    var id = 0
    var text = ""
    var user: UserResponse? = null
    var listAnswer = mutableListOf<AnswerResponse>()
    var date = ""
}