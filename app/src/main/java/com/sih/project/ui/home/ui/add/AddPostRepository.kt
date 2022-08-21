package com.sih.project.ui.home.ui.add

import android.net.Uri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sih.project.model.Posts
import com.sih.project.model.PostsStatus
import com.sih.project.util.PreferenceHelper
import com.sih.project.util.Resource
import com.sih.project.util.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class AddPostRepository(
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference,
    private val userReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("users"),
    private val userId: String = PreferenceHelper.userId,
    private val postsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("posts"),
) {

    suspend fun uploadImage(imageComment: String, imagePath: Uri) = withContext(Dispatchers.IO) {
        val ref = storageReference.child("uploads/" + PreferenceHelper.userId)
        safeCall {
            val uploadTask = ref.putFile(imagePath).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            val postId = UUID.randomUUID().toString()
            val addedPosts = async {
                val typeIndicator = object : GenericTypeIndicator<MutableList<Posts>>() {}
                var posts = postsReference.get().await().getValue(typeIndicator)
                if (posts.isNullOrEmpty()) {
                    val postsList = mutableListOf<Posts>()
                    postsList.add(
                        Posts(
                            id = postId,
                            userId = userId,
                            userCaption = imageComment,
                            imageUrl = downloadUrl.toString(),
                            time = System.currentTimeMillis(),
                            likes = 0,
                            comments = 0,
                            status = PostsStatus.PENDING.name
                        )
                    )
                    posts = postsList
                } else {
                    posts.add(
                        Posts(
                            id = postId,
                            userId = userId,
                            userCaption = imageComment,
                            imageUrl = downloadUrl.toString(),
                            time = System.currentTimeMillis(),
                            likes = 0,
                            comments = 0,
                            status = PostsStatus.PENDING.name
                        )
                    )
                }
                postsReference.setValue(posts).await()
            }
            val addedInUserPosts = async {
                val typeIndicator = object : GenericTypeIndicator<MutableList<String>>() {}
                var userPosts = userReference.child(userId).child("posts").get().await().getValue(typeIndicator)
                if (userPosts.isNullOrEmpty()) {
                    userPosts = mutableListOf(postId)
                } else {
                    userPosts.add(postId)
                }
                userReference.child(userId).child("posts").setValue(userPosts).await()
            }
            listOf(addedPosts, addedInUserPosts).awaitAll()
            Resource.Success(true)
        }
    }

}
