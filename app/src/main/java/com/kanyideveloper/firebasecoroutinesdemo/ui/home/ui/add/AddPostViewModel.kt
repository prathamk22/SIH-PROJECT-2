package com.kanyideveloper.firebasecoroutinesdemo.ui.home.ui.add

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.firebasecoroutinesdemo.util.Resource
import kotlinx.coroutines.launch

class AddPostViewModel(
    private val repo: AddPostRepository = AddPostRepository()
) : ViewModel() {

    val status: LiveData<Resource<Boolean>>
        get() = _status
    private val _status = MutableLiveData<Resource<Boolean>>(Resource.Idle())

    fun uploadImage(imageComment: String?, imagePath: Uri?) {
        viewModelScope.launch {
            if (imageComment.isNullOrEmpty()) {
                _status.value = Resource.Error("Add comments to upload the post")
                return@launch
            }
            if (imagePath == null) {
                _status.value = Resource.Error("Image is not selected. Please select a image.")
                return@launch
            }
            when (val result = repo.uploadImage(imageComment, imagePath)) {
                is Resource.Error -> {
                    Log.e("TAG", "uploadImage: $result", )
                }
                is Resource.Idle -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    Log.e("TAG", "uploadImage: $result", )
                }
            }
        }
    }

    fun setIdle() {
        _status.postValue(Resource.Idle())
    }

}
