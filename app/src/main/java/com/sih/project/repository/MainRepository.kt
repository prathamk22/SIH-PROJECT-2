package com.sih.project.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sih.project.model.User
import com.sih.project.util.PreferenceHelper
import com.sih.project.util.Resource
import com.sih.project.util.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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

    suspend fun login(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                PreferenceHelper.userId = result.user?.uid!!
                PreferenceHelper.isLoggedIn = true
                Resource.Success(result)
            }
        }
    }
}