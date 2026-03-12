package com.shimmita.full_gospel.model

data class PostResponse(
    val mainId: Long ,
    val username: String,
    var phone: String = "",
    var author: String = "",
    var role: String = "",
    val details: String="",
    val verse: String="",
    var imagePath: String=""
)
