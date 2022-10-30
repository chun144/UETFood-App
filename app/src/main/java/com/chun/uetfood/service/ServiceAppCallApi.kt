package com.chun.uetfood.service

import com.chun.uetfood.model.request.*
import com.chun.uetfood.model.response.*
import io.reactivex.Observable
import retrofit2.http.*


interface ServiceAppCallApi {
    @POST("/api/v1/login")
    fun login(
        @Body body: LoginRequest
    ): Observable<LoginResponse>

    @POST("/api/v1/register")
    fun register(
        @Body body: RegisterRequest
    ): Observable<MessageResponse>

    @GET("/api/v1/food/all")
    fun getAllFood(): Observable<MutableList<FoodResponse>>

    @POST("/api/v1/food")
    fun addFood(
        @Body body: FoodRequest
    ): Observable<FoodResponse>

    @PUT("/api/v1/food/{id}")
    fun putFood(
        @Path("id") id: Int?, @Body body: FoodRequest
    ): Observable<FoodResponse>

    @DELETE("/api/v1/food/{id}")
    fun deleteFood(
        @Path("id") id: Int?
    ): Observable<MessageResponse>

    @POST("/api/v1/order")
    fun addOrder(
        @Body body: OrderRequest
    ): Observable<OrderResponse>

    @POST("/api/v1/shopping-cart")
    fun addCart(
        @Body body: CartRequest
    ): Observable<CartResponse>

    @GET("/api/v1/shopping-cart/{username}")
    fun getFoodCart(
        @Path("username") id: String?
    ): Observable<MutableList<FoodResponse>>

    @HTTP(method = "DELETE", path = "/api/v1/shopping-cart", hasBody = true)
    fun deleteCart(
        @Body body: CartRequest
    ): Observable<MessageResponse>

    @GET("/api/v1/order/all")
    fun getAllOrder(): Observable<MutableList<OrderResponse>>

    @GET("/api/v1/order/user/{username}")
    fun getOrderUser(
        @Path("username") username: String?
    ): Observable<MutableList<OrderResponse>>

    @GET("/api/v1/order/shipper/{username}")
    fun getOrderShipper(
        @Path("username") username: String?
    ): Observable<MutableList<OrderResponse>>

    @GET("/api/v1/order/{id}")
    fun getOrder(
        @Path("id") id: Int?
    ): Observable<OrderResponse>

    @PUT("/api/v1/order-shipper/{orderId}/{status}")
    fun putStatusOrder(
        @Path("orderId") orderId: Int?, @Path("status") status: String?
    ): Observable<OrderResponse>

    @POST("/api/v1/order-shipper")
    fun addOrderShipper(
        @Body body: OrderShipperRequest
    ): Observable<OrderShipperResponse>

    @POST("/api/v1/question")
    fun addQuestion(
        @Body body: QuestionRequest
    ): Observable<QuestionResponse>

    @GET("/api/v1/question/all")
    fun getAllQuestion(): Observable<MutableList<QuestionResponse>>

    @GET("/api/v1/question/{username}")
    fun getQuestionUser(
        @Path("username") username: String?
    ): Observable<MutableList<QuestionResponse>>

    @GET("/api/v1/question/{id}")
    fun getQuestion(
        @Path("id") id: Int?
    ): Observable<QuestionResponse>

    @POST("/api/v1/answer")
    fun addAnswer(
        @Body body: AnswerRequest
    ): Observable<AnswerResponse>

    @GET("/api/v1/comment/all")
    fun getAllFeedback(): Observable<MutableList<FeedbackResponse>>

    @POST("/api/v1/comment")
    fun addFeedback(
        @Body body: FeedbackRequest
    ): Observable<FeedbackResponse>

}