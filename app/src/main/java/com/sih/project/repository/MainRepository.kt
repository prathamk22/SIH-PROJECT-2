package com.sih.project.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sih.project.util.PreferenceHelper
import com.sih.project.util.Resource
import com.sih.project.util.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.sih.project.model.User
import com.google.firebase.database.ktx.getValue
import com.sih.project.util.UserTypes

class MainRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().getReference("users")

    suspend fun createUser(userName: String, userEmailAddress: String, userPhoneNum: String, userLoginPassword: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val registrationResult = firebaseAuth.createUserWithEmailAndPassword(userEmailAddress, userLoginPassword).await()
                val userId = registrationResult.user?.uid!!
                val newUser = User(id = userId, userName, userEmailAddress, userPhoneNum)
                databaseReference.child(userId).setValue(newUser).await()
                PreferenceHelper.userId = userId
                PreferenceHelper.isLoggedIn = true
                Resource.Success(registrationResult)
            }
        }
    }

    suspend fun login(email: String, password: String): Resource<User> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val user = databaseReference.child(result.user?.uid!!).get().await().getValue<User>()
                if (user == null){
                    Resource.Error("Something went wrong. Please try again")
                } else {
                    PreferenceHelper.userId = result.user?.uid!!
                    PreferenceHelper.isLoggedIn = true
                    PreferenceHelper.userType = user.type ?: UserTypes.USER.name
                    Resource.Success(user)
                }
            }
        }
    }
}