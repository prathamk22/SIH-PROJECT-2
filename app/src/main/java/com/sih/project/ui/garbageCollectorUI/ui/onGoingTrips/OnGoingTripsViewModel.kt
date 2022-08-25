package com.sih.project.ui.garbageCollectorUI.ui.onGoingTrips

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sih.project.model.CollectorTripEntity
import com.sih.project.model.getItem
import com.sih.project.util.EventResponse
import com.sih.project.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
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
            repo.getAllUserTrips()
                .collectLatest { userTrips ->
                    if (userTrips.isNotEmpty()) {
                        state.postValue(Resource.Success(userTrips))
                    } else {
                        state.postValue(Resource.Error("No trips found."))
                    }
                }
        }
    }

    fun updateTripStatus(selectedItem: String, item: CollectorTripEntity?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateStatus(getItem(selectedItem), item)
        }
    }


}
