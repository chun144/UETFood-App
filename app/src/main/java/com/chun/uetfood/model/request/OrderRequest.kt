package com.chun.uetfood.model.request

data class OrderRequest (val username: String, val listOrderComposition: String, val address: String, val phone: String, val note: String)