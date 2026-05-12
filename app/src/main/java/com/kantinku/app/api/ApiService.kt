package com.kantinku.app.api

import com.kantinku.app.model.LoginRequest
import com.kantinku.app.model.LoginResponse
import com.kantinku.app.model.Menu
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("menu")
    fun getMenu(): Call<List<Menu?>>

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}