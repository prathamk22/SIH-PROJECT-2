package com.sih.project.viewmodel

import android.util.Patterns
import androidx.lifecycle.*
import com.google.firebase.auth.AuthResult
import com.sih.project.model.User
import com.sih.project.repository.MainRepository
import com.sih.project.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _userRegistrationStatus = MutableLiveData<Resource<AuthResult>>()
    val userRegistrationStatus: LiveData<Resource<AuthResult>> = _userRegistrationStatus

    private val _userSignUpStatus = MutableLiveData<Resource<User>>()
    val userSignUpStatus: LiveData<Resource<User>> = _userSignUpStatus

    private val mainRepository = MainRepository()


    fun createUser(userName: String, userEmailAddress: String, userPhoneNum: String, userLoginPassword: String) {
        val error =
            if (userEmailAddress.isEmpty() || userName.isEmpty() || userLoginPassword.isEmpty() || userPhoneNum.isEmpty()) {
                "Empty Strings"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmailAddress).matches()) {
                "Not a valid Email"
            } else null

        error?.let {
            _userRegistrationStatus.postValue(Resource.Error(it))
            return
        }
        _userRegistrationStatus.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.Main) {
            val registerResult = mainRepository.createUser(userName = userName, userEmailAddress = userEmailAddress, userPhoneNum = userPhoneNum, userLoginPassword = userLoginPassword)
            _userRegistrationStatus.postValue(registerResult)
        }
    }

    fun signInUser(userEmailAddress: String, userLoginPassword: String) {
        if (userEmailAddress.isEmpty() || userLoginPassword.isEmpty()) {
            _userSignUpStatus.postValue(Resource.Error("Empty Strings"))
        } else {
            _userSignUpStatus.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.Main) {
                val loginResult = mainRepository.login(userEmailAddress, userLoginPassword)
                _userSignUpStatus.postValue(loginResult)
            }
        }
    }
}
