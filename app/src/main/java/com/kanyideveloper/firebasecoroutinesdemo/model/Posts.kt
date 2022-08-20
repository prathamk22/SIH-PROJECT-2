package com.kanyideveloper.firebasecoroutinesdemo.model

data class Posts(
    val id: String? = null,
    val imageUrl: String? = null,
    val userCaption: String? = null,
    val userId: String? = null,
    val time: Long? = null,
    val likes: Int? = null,
    val comments: Int? = null,
    val status: String? = null,
)
