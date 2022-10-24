package com.chun.uetfood.model.response

import java.util.*

class OrderResponse {
    var id = 0
    var user: UserResponse? = null
    var address = ""
    var phone = ""
    var totalPrice = 0
    var date: Date? = null
    var status = ""
    var listOrderComposition = mutableListOf<OrderCompositionResponse>()
    var note = ""
}