package com.kanyideveloper.firebasecoroutinesdemo.util

sealed class Resource<T>(open val data: T? = null, open val message: String? = null) {
    data class Success<T>(override val data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    data class Error<T>(override val message: String, override val data: T? = null) :
        Resource<T>(data, message)
    class Idle<T> : Resource<T>()
}
