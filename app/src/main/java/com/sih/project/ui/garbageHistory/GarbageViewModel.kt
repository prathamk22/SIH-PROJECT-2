package com.sih.project.ui.garbageHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.GenericTypeIndicator
import com.sih.project.model.UserPosts
import com.sih.project.ui.home.ui.home.HomeRepository
import com.sih.project.util.EventResponse
import com.sih.project.util.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GarbageViewModel(
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
            repo.getAllUserPosts()
                .map {
                    return@map when (it) {
                        is EventResponse.Cancelled -> {
                            Resource.Error(it.error.message)
                        }
                        is EventResponse.Changed -> {
                            val genericTypeIndicator =
                                object : GenericTypeIndicator<List<String>>() {}
                            val postIdsList = (it.snapshot.getValue(genericTypeIndicator) ?: emptyList())
                            val postLists = repo.getAllPostsFromIds(postIdsList)
                            Resource.Success(repo.transformToUserPostsFromListOfPosts(postLists, viewModelScope))
                        }
                    }
                }
                .collectLatest {
                    _state.postValue(it)
                }
        }
    }


}