package com.sih.project.ui.home.ui.offers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ktx.getValue
import com.sih.project.model.OffersEntity
import com.sih.project.util.EventResponse
import com.sih.project.util.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OffersViewModel(
    private val repository: OffersRepository = OffersRepository()
) : ViewModel() {

    val offers = MutableLiveData<Resource<List<OffersEntity>>>()

    init {
        getAllOffers()
    }

    private fun getAllOffers() {
        viewModelScope.launch {
            offers.postValue(Resource.Loading())
            repository.getAllOffers()
                .collectLatest { result ->
                    when (result) {
                        is EventResponse.Cancelled -> {
                            offers.postValue(Resource.Error(result.error.message))
                        }
                        is EventResponse.Changed -> {
                            val offersList = result.snapshot.getValue<List<OffersEntity>>()
                            offersList?.let {
                                offers.postValue(Resource.Success(it))
                            }
                        }
                    }
                }
        }
    }

}