package com.kantinku.app.model

data class LoginResponse(
    val message: String,
    val access_token: String,
    val token_type: String,
    val user: User
)

data class User(
    val id: Int,
    val name: String,
    val email: String
)