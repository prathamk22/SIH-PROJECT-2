package com.kanyideveloper.firebasecoroutinesdemo.ui.home.ui.home

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.kanyideveloper.firebasecoroutinesdemo.model.User
import com.kanyideveloper.firebasecoroutinesdemo.util.valueEventFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class HomeRepository(
    private val postsReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("posts"),
    private val userReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("users"),
) {

    suspend fun getAllPosts() = postsReference.valueEventFlow().flowOn(Dispatchers.IO)

    suspend fun getUser(it: String): User? {
        val genericTypeIndicator = object : GenericTypeIndicator<User>(){}
        return userReference.child(it).get().await().getValue(genericTypeIndicator)
    }

}
