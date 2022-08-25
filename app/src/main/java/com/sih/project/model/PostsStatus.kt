package com.sih.project.model

import androidx.annotation.ColorRes
import com.sih.project.R

enum class PostsStatus(val text: String, @ColorRes val colorId: Int) {
    PENDING("Pending", R.color.black),
    COMPLETED("Completed", R.color.darkGreen),
    FAKE("Fake", R.color.red),
    USER_VERIFICATION("Verification Required", R.color.blue);
}

fun getItem(text: String): PostsStatus {
    return PostsStatus.values().first { it.text.equals(text, true) }
}