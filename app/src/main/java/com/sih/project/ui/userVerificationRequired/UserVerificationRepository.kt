package com.sih.project.ui.userVerificationRequired

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.sih.project.model.Posts
import com.sih.project.model.PostsStatus
import com.sih.project.ui.home.ui.home.HomeRepository
import com.sih.project.util.EventResponse
import com.sih.project.util.valueEventFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class UserVerificationRepository(
    private val postsReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("posts"),
    private val homeRepository: HomeRepository = HomeRepository()
) {

    suspend fun getAllUserVerifications(scope: CoroutineScope) =
        postsReference.valueEventFlow().flowOn(Dispatchers.IO).map {
            when (it) {
                is EventResponse.Cancelled -> {
                    emptyList()
                }
                is EventResponse.Changed -> {
                    val allPosts = it.snapshot.getValue<List<Posts>>()?.filter { post ->
                        PostsStatus.valueOf(
                            post.status ?: PostsStatus.COMPLETED.name
                        ) == PostsStatus.USER_VERIFICATION
                    } ?: emptyList()
                    homeRepository.transformToUserPostsFromListOfPosts(allPosts, scope)
                }
            }
        }

}
