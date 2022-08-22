package com.sih.project.ui.garbageCollectorUI.ui.tripBottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sih.project.databinding.FragmentTripBottomSheetBinding
import com.sih.project.model.PostsStatus
import com.sih.project.model.UserPosts
import com.sih.project.util.loadImage
import com.sih.project.util.openInMaps

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
            binding.userPost.postStatus.text = PostsStatus.valueOf(item.posts?.status ?: "").text
        } ?: run {
            binding.successGroup.isVisible = false
        }
        binding.startJourney.setOnClickListener {
            LatLng(arguments?.getDouble(LATITUDE)!!, arguments?.getDouble(LONGITUDE)!!).openInMaps(
                requireContext()
            )
        }
    }

}
