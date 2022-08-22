package com.sih.project.model

data class TripEntity(
    val id: String? = null,
    val latitude: Double? = null,
    val longitute: Double? = null,
    val userId: String? = null,
    val time: Long? = null,
    val status: String? = null,
    val postId: String? = null
)

enum class TripStatus{
    ONGOING,
    COMPLETED,
    FAILED
}
