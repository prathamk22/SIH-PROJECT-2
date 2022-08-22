package com.sih.project.ui.garbageCollectorUI.ui.onGoingTrips

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sih.project.model.CollectorTripEntity
import com.sih.project.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnGoingTripsViewModel(
    private val repo: OnGoingTripsRepository = OnGoingTripsRepository()
) : ViewModel() {

    val state = MutableLiveData<Resource<List<CollectorTripEntity>>>()

    init {
        getAllUserTrips()
    }

    private fun getAllUserTrips() {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(Resource.Loading())
            val userTrips = repo.getAllUserTrips()
            if (userTrips.isNotEmpty()){
                state.postValue(Resource.Success(userTrips))
            } else {
                state.postValue(Resource.Error("No trips found."))
            }
        }
    }


}
