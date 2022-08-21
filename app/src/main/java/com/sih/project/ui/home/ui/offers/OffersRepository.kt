package com.sih.project.ui.home.ui.offers

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sih.project.util.valueEventFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class OffersRepository(
    private val offersReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("offers")
) {

    suspend fun getAllOffers() = offersReference.valueEventFlow().flowOn(Dispatchers.IO)

}
