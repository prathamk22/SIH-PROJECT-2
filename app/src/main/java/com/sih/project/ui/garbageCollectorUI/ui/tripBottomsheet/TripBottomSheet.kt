package com.sih.project.ui.garbageCollectorUI.ui.tripBottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.sih.project.databinding.FragmentTripBottomSheetBinding
import com.sih.project.model.*
import com.sih.project.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*

class TripBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "TripBottomSheet"
        private const val POST_ID = "post"
        private const val LATITUDE = "lat"
        private const val LONGITUDE = "long"
        fun getInstance(post: UserPosts, latitude: Double, longitude: Double): TripBottomSheet {
            return TripBottomSheet().apply {
                arguments = bundleOf(
                    POST_ID to post,
                    LATITUDE to latitude,
                    LONGITUDE to longitude
                )
            }
        }
    }

    private var _binding: FragmentTripBottomSheetBinding? = null
    val binding: FragmentTripBottomSheetBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripBottomSheetBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (arguments?.getSerializable(POST_ID) as? UserPosts)?.let { item ->
            binding.userPost.homeImage.loadImage(item.posts?.imageUrl)
            binding.userPost.personName.text = item.user?.name ?: ""
            binding.userPost.userComment.text = item.posts?.userCaption ?: ""
            with(PostsStatus.valueOf(item.posts?.status ?: "")) {
                binding.userPost.postStatus.text = text
                binding.userPost.postStatus.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        colorId
                    )
                )
                if (this == PostsStatus.COMPLETED) {
                    binding.startJourney.isVisible = false
                }
            }
        } ?: run {
            binding.startJourney.isVisible = false
            binding.userPost.root.isVisible = false
        }
        binding.startJourney.setOnClickListener {
            GlobalScope.launch {
                val tripReference: DatabaseReference = FirebaseDatabase.getInstance()
                    .getReference("trips")
                val userReference: DatabaseReference = FirebaseDatabase.getInstance()
                    .getReference("users")
                withContext(Dispatchers.IO) {
                    safeCall {
                        val tripId = UUID.randomUUID().toString()
                        val addedPosts = async {
                            val typeIndicator =
                                object : GenericTypeIndicator<MutableList<TripEntity>>() {}
                            var trips = tripReference.get().await().getValue(typeIndicator)
                            if (trips.isNullOrEmpty()) {
                                val tripList = mutableListOf<TripEntity>()
                                tripList.add(
                                    TripEntity(
                                        id = tripId,
                                        userId = PreferenceHelper.userId,
                                        time = System.currentTimeMillis(),
                                        status = TripStatus.ONGOING.name,
                                        latitude = arguments?.getDouble(LATITUDE)!!,
                                        longitute = arguments?.getDouble(LONGITUDE)!!,
                                        postId = (arguments?.getSerializable(POST_ID) as? UserPosts)?.posts?.id
                                    )
                                )
                                trips = tripList
                            } else {
                                trips.add(
                                    TripEntity(
                                        id = tripId,
                                        userId = PreferenceHelper.userId,
                                        time = System.currentTimeMillis(),
                                        status = TripStatus.ONGOING.name,
                                        latitude = arguments?.getDouble(LATITUDE)!!,
                                        longitute = arguments?.getDouble(LONGITUDE)!!,
                                        postId = (arguments?.getSerializable(POST_ID) as? UserPosts)?.posts?.id
                                    )
                                )
                            }
                            tripReference.setValue(trips).await()
                        }
                        val addedInUserPosts = async {
                            val typeIndicator =
                                object : GenericTypeIndicator<MutableList<String>>() {}
                            var userTrips =
                                userReference.child(PreferenceHelper.userId).child("trips").get()
                                    .await().getValue(typeIndicator)
                            if (userTrips.isNullOrEmpty()) {
                                userTrips = mutableListOf(tripId)
                            } else {
                                userTrips.add(tripId)
                            }
                            userReference.child(PreferenceHelper.userId).child("trips")
                                .setValue(userTrips).await()
                        }
                        listOf(addedPosts, addedInUserPosts).awaitAll()
                        Resource.Success(true)
                    }
                }
            }
            LatLng(arguments?.getDouble(LATITUDE)!!, arguments?.getDouble(LONGITUDE)!!).openInMaps(
                requireContext()
            )
        }
    }

}
