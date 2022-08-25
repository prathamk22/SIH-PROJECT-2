package com.sih.project.model

enum class PostsStatus(val text: String) {
    PENDING("Pending"),
    COMPLETED("Completed"),
    FAKE("Fake");
}

fun getItem(text: String): PostsStatus {
    return PostsStatus.values().first { it.text.equals(text, true) }
}