package com.kantinku.app.api

import com.kantinku.app.model.Menu
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @get:GET("menu")
    val menu: Call<List<Menu?>?>?
}