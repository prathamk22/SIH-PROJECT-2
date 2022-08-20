package com.kanyideveloper.firebasecoroutinesdemo.ui.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.GenericTypeIndicator
import com.kanyideveloper.firebasecoroutinesdemo.model.Posts
import com.kanyideveloper.firebasecoroutinesdemo.model.UserPosts
import com.kanyideveloper.firebasecoroutinesdemo.util.EventResponse
import com.kanyideveloper.firebasecoroutinesdemo.util.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

class HomeViewModel(
    private val repo: HomeRepository = HomeRepository()
) : ViewModel() {

    val state: LiveData<Resource<List<UserPosts>>>
        get() = _state
    private val _state = MutableLiveData<Resource<List<UserPosts>>>()

    init {
        observeAllPosts()
    }

    private fun observeAllPosts() {
        viewModelScope.launch {
            _state.postValue(Resource.Loading())
            repo.getAllPosts()
                .map {
                    return@map when (it) {
                        is EventResponse.Cancelled -> {
                            Resource.Error(it.error.message)
                        }
                        is EventResponse.Changed -> {
                            val genericTypeIndicator =
                                object : GenericTypeIndicator<List<Posts>>() {}
                            val posts = (it.snapshot.getValue(genericTypeIndicator) ?: emptyList())
                            val userIds = posts.map { post -> post.userId ?: "" }
                                .map { userId -> async { repo.getUser(userId) } }
                            val newUsers = userIds.awaitAll()
                            val userPosts = posts.map { post ->
                                UserPosts(
                                    posts = post,
                                    user = newUsers.firstOrNull { it?.id == post.userId }
                                )
                            }
                            Resource.Success(userPosts)
                        }
                    }
                }
                .collectLatest {
                    _state.postValue(it)
                }
        }
    }

}