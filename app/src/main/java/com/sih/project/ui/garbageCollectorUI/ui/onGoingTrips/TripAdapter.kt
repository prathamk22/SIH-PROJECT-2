package com.sih.project.ui.garbageCollectorUI.ui.onGoingTrips

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sih.project.databinding.TripItemBinding
import com.sih.project.model.CollectorTripEntity
import com.sih.project.model.PostsStatus
import com.sih.project.util.loadImage

class TripAdapter(private val onUpdateStatus: (selectedItem: String, item: CollectorTripEntity?) -> Unit) :
    ListAdapter<CollectorTripEntity, CollectorTripViewHolder>(
        object : DiffUtil.ItemCallback<CollectorTripEntity>() {
            override fun areItemsTheSame(
                oldItem: CollectorTripEntity,
                newItem: CollectorTripEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CollectorTripEntity,
                newItem: CollectorTripEntity
            ): Boolean {
                return oldItem.toString().equals(newItem.toString(), true)
            }

        }
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorTripViewHolder {
        return CollectorTripViewHolder(
            TripItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onUpdateStatus
        )
    }

    override fun onBindViewHolder(holder: CollectorTripViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}

class CollectorTripViewHolder(
    private val binding: TripItemBinding,
    private val onUpdateStatus: (selectedItem: String, item: CollectorTripEntity?) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        val spinnerAdapter = ArrayAdapter(
            binding.root.context, android.R.layout.simple_list_item_1, listOf(
                PostsStatus.PENDING.text,
                PostsStatus.COMPLETED.text,
                PostsStatus.FAKE.text
            )
        )
        binding.statusSpinner.adapter = spinnerAdapter
    }

    fun bind(item: CollectorTripEntity?, position: Int) {
        if (item == null)
            return
        binding.homeImage.loadImage(item.userPosts.posts?.imageUrl)
        binding.personName.text = item.userPosts.user?.name ?: ""
        binding.userComment.text = item.userPosts.posts?.userCaption ?: ""
        with(PostsStatus.valueOf(item.userPosts.posts?.status ?: "")) {
            binding.postStatus.text = text
            binding.postStatus.setTextColor(ContextCompat.getColor(binding.root.context, colorId))
        }
        binding.updateStatus.setOnClickListener {
            onUpdateStatus.invoke(binding.statusSpinner.selectedItem.toString(), item)
        }
    }
}
