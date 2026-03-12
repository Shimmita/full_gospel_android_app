package com.shimmita.full_gospel.model

data class UserRegister(
    val username: String,
    val password: String,
    val phone: String,
    val first_name: String,
    val last_name: String,
    val role: String,
    val imagePath: String,
)
