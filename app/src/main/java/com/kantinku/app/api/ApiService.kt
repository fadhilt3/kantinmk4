package com.kantinku.app.api

import com.kantinku.app.model.Category
import com.kantinku.app.model.FavoriteCheckResponse
import com.kantinku.app.model.FavoriteResponse
import com.kantinku.app.model.LoginRequest
import com.kantinku.app.model.LoginResponse
import com.kantinku.app.model.Menu
import com.kantinku.app.model.OrderData
import com.kantinku.app.model.OrderRequest
import com.kantinku.app.model.OrderResponse
import com.kantinku.app.model.PaymentRequest
import com.kantinku.app.model.PaymentResponse
import com.kantinku.app.model.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("menu")
    fun getMenu(): Call<List<Menu?>>

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Call<LoginResponse>

    @POST("order")
    fun createOrder(
        @Header("Authorization") token: String,
        @Body request: OrderRequest
    ): Call<OrderResponse>

    @GET("order")
    fun getOrders(
        @Header("Authorization") token: String
    ): Call<List<OrderData>>

    @POST("payment")
    fun createPayment(
        @Header("Authorization") token: String,
        @Body request: PaymentRequest
    ): Call<PaymentResponse>

    @GET("categories")
    fun getCategories(): Call<List<Category>>

    @GET("favorites")
    fun getFavorites(
        @Header("Authorization") token: String
    ): Call<List<Menu>>

    @POST("favorites/{menuId}")
    fun toggleFavorite(
        @Header("Authorization") token: String,
        @Path("menuId") menuId: Int
    ): Call<FavoriteResponse>

    @GET("favorites/{menuId}/check")
    fun checkFavorite(
        @Header("Authorization") token: String,
        @Path("menuId") menuId: Int
    ): Call<FavoriteCheckResponse>
}