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
    ): Observable<RegisterResponse>

    //Admin call cái này, item ngoài hiện idOrder, totalprice, date, status, click vào item hiện đủ
    //Thanh search có các chức năng tìm kiếm theo id, date, status
    //Các status: ordered(đã đặt hàng), shipping(đang giao hàng), shipped(đã giao hàng), canceled(bùng hàng)
    @GET("/api/v1/order/all")
    fun getAllOrder(): Observable<MutableList<OrderResponse>>

    //User call, hiện tương tự như admin
    @GET("/api/v1/order/user/{username}")
    fun getOrderUser(
        @Path("username") username: String?
    ): Observable<MutableList<OrderResponse>>

    //Shipper call, lấy ra những order shipper đã nhận hoặc đã giao, hiển thị tương tự như admin
    @GET("/api/v1/order/shipper/{username}")
    fun getOrderShipper(
        @Path("username") username: String?
    ): Observable<MutableList<OrderResponse>>

    //Shipper: với những item order status shipping có thêm nút xác nhận đã giao hàng
    //truyền id của order và status là shipped(đã giao hàng) hoặc canceled(bùng hàng)
    @PUT("/api/v1/order-shipper/{orderId}/{status}")
    fun putStatusOrder(
        @Path("orderId") orderId: Int?, @Path("status") status: String?
    ): Observable<OrderResponse>

    //User call để tạo các question
    @POST("/api/v1/question")
    fun addQuestion(
        @Body body: QuestionRequest
    ): Observable<QuestionResponse>

    //Admin call để lấy toàn bộ câu hỏi của User
    //các item ngoài hiện id(câu hỏi số...), text, số lượng câu trả lời, click item hiện tương tự như app chat
    @GET("/api/v1/question/all")
    fun getAllQuestion(): Observable<MutableList<QuestionResponse>>

    //User call lấy question của user đó
    @GET("/api/v1/question/{username}")
    fun getQuestionUser(
        @Path("username") username: String?
    ): Observable<MutableList<QuestionResponse>>

    //User và Admin call để tạo các answer của question nào đó
    @POST("/api/v1/answer")
    fun addAnswer(
        @Body body: AnswerRequest
    ): Observable<AnswerResponse>

}