package com.sih.project.ui.garbageCollectorUI.ui.onGoingTrips

import android.os.Build
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.sih.project.model.*
import com.sih.project.util.EventResponse
import com.sih.project.util.PreferenceHelper
import com.sih.project.util.valueEventFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class OnGoingTripsRepository(
    private val userReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("users"),
    private val userId: String = PreferenceHelper.userId,
    private val postsReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("posts"),
    private val tripsReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("trips"),
) {

    suspend fun getAllUserTrips() = postsReference.valueEventFlow().map {
        val allTrips = (tripsReference.get().await().getValue<List<TripEntity>>() ?: emptyList())
        if (allTrips.isNotEmpty()) {
            val allUserTrips = allTrips.filter { it.userId == userId }
            val allUserTripsPosts = allUserTrips.map { it.postId }
            when (it) {
                is EventResponse.Cancelled -> {
                    emptyList()
                }
                is EventResponse.Changed -> {
                    val allPosts = it.snapshot.getValue<List<Posts>>()
                    if (allPosts.isNullOrEmpty()) {
                        emptyList()
                    } else {
                        val userPostTrips =
                            allUserTripsPosts.map { postId -> allPosts.firstOrNull { it.id == postId } }
                        allUserTrips.map { tripEntity ->
                            val post = userPostTrips.first { it?.id == tripEntity.postId }
                            CollectorTripEntity(
                                tripEntity = tripEntity,
                                userPosts = UserPosts(
                                    posts = post,
                                    user = userReference.child(post?.userId!!).get().await()
                                        .getValue<User>()
                                )
                            )
                        }
                    }
                }
            }
        } else {
            emptyList()
        }
    }

    suspend fun updateStatus(status: PostsStatus, item: CollectorTripEntity?) =
        withContext(Dispatchers.IO) {
            val asPerSystemStatus = when (status) {
                PostsStatus.COMPLETED -> PostsStatus.USER_VERIFICATION
                else -> status
            }
            val posts =
                postsReference.get().await().getValue<MutableList<Posts>>() ?: return@withContext
            val currentPost = posts.first { it.id.equals(item?.userPosts?.posts?.id, true) }
                .copy(status = status.name)
            updateUserPostStatus(currentPost, asPerSystemStatus)
        }

    suspend fun updateUserPostStatus(currentPost: Posts, status: PostsStatus) =
        withContext(Dispatchers.IO) {
            val posts =
                postsReference.get().await().getValue<MutableList<Posts>>() ?: return@withContext
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                posts.removeAll {
                    (it.id.equals(currentPost.id, true))
                }
                posts.add(currentPost)
                postsReference.setValue(posts).await()
            }
        }

}
