package com.sih.project.ui.garbageCollectorUI

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sih.project.model.User
import com.sih.project.ui.home.ui.account.AccountRepository
import kotlinx.coroutines.launch

class GarbageCollectorViewModel(
    private val repository: AccountRepository = AccountRepository()
) : ViewModel() {

    val user = MutableLiveData<User>()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            user.postValue(repository.getCurrentUser())
        }
    }

}
