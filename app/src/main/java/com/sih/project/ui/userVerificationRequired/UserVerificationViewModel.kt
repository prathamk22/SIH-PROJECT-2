package com.sih.project.ui.userVerificationRequired

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sih.project.model.UserPosts
import com.sih.project.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserVerificationViewModel(
    private val repository: UserVerificationRepository = UserVerificationRepository()
) : ViewModel() {

    val state = MutableLiveData<Resource<List<UserPosts>>>()

    init {
        getAllVerificationPostsOfUser()
    }

    private fun getAllVerificationPostsOfUser() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllUserVerifications(viewModelScope)
                .collectLatest {
                    if (it.isNotEmpty()) {
                        state.postValue(Resource.Success(it))
                    } else {
                        state.postValue(Resource.Error("No verification of post required"))
                    }
                }
        }
    }

    fun garbageCollectionVerified(post: UserPosts) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.garbageCollectionVerified(post)
        }
    }

}
