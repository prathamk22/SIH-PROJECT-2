package com.sih.project.ui.garbageCollectorUI.ui.onGoingTrips

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.sih.project.model.*
import com.sih.project.util.PreferenceHelper
import kotlinx.coroutines.Dispatchers
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

    suspend fun getAllUserTrips() = withContext(Dispatchers.IO) {
        val allTrips = (tripsReference.get().await().getValue<List<TripEntity>>() ?: emptyList())
        if (allTrips.isNotEmpty()) {
            val allUserTrips = allTrips.filter { it.userId == userId }
            val allUserTripsPosts = allUserTrips.map { it.postId }
            val allPosts = postsReference.get().await().getValue<List<Posts>>()
            if (allPosts.isNullOrEmpty())
                return@withContext emptyList<CollectorTripEntity>()
            val userPostTrips =
                allUserTripsPosts.map { postId -> allPosts.firstOrNull { it.id == postId } }
            return@withContext allUserTrips.map { tripEntity ->
                val post = userPostTrips.first { it?.id == tripEntity.postId }
                CollectorTripEntity(
                    tripEntity = tripEntity,
                    userPosts = UserPosts(
                        posts = post,
                        user = userReference.child(post?.userId!!).get().await().getValue<User>()
                    )
                )
            }
        }
        return@withContext emptyList()
    }

}
