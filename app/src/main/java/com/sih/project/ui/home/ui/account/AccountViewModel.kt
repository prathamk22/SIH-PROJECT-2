package com.sih.project.ui.home.ui.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sih.project.model.User
import kotlinx.coroutines.launch

class AccountViewModel(
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