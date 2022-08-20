package com.kanyideveloper.firebasecoroutinesdemo.ui.home.ui.account

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.kanyideveloper.firebasecoroutinesdemo.model.User
import com.kanyideveloper.firebasecoroutinesdemo.util.PreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AccountRepository(
    private val userReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("users")
) {

    suspend fun getCurrentUser(): User? {
        return withContext(Dispatchers.IO){ userReference.child(PreferenceHelper.userId).get().await().getValue<User>() }
    }

}