package com.chun.uetfood.model.response

class OrderResponse {
    var id = 0
    var user: UserResponse? = null
    var address = ""
    var phone = ""
    var totalPrice = 0
    var date = ""
    var status = ""
    var listOrderComposition = mutableListOf<OrderCompositionResponse>()
    var note = ""
}