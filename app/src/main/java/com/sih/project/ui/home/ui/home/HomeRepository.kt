package com.sih.project.ui.home.ui.home

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.getValue
import com.sih.project.model.Posts
import com.sih.project.model.User
import com.sih.project.model.UserPosts
import com.sih.project.util.PreferenceHelper
import com.sih.project.util.valueEventFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class HomeRepository(
    private val postsReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("posts"),
    private val userReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("users"),
) {

    suspend fun getAllPosts() = postsReference.valueEventFlow().flowOn(Dispatchers.IO)

    private suspend fun getUser(it: String): User? {
        val genericTypeIndicator = object : GenericTypeIndicator<User>() {}
        return userReference.child(it).get().await().getValue(genericTypeIndicator)
    }

    suspend fun getAllUserPosts(userId: String = PreferenceHelper.userId) =
        userReference.child(userId).child("posts").valueEventFlow().flowOn(Dispatchers.IO)

    suspend fun transformToUserPostsFromListOfPosts(
        posts: List<Posts?>,
        scope: CoroutineScope
    ) =
        withContext(Dispatchers.IO) {
            val userIds = posts.map { post -> post?.userId ?: "" }
                .map { userId -> scope.async(Dispatchers.IO) { getUser(userId) } }
            val newUsers = userIds.awaitAll()
            val userPosts = posts.map { post ->
                UserPosts(
                    posts = post,
                    user = newUsers.firstOrNull { it?.id == post?.userId }
                )
            }
            userPosts
        }

    suspend fun getAllPostsFromIds(list: List<String>) = withContext(Dispatchers.IO) {
        val allPosts = postsReference.get().await().getValue<List<Posts>>()
        if (allPosts.isNullOrEmpty())
            return@withContext emptyList<Posts>()
        return@withContext list.map { postId -> allPosts.firstOrNull { it.id == postId } }
    }

}
