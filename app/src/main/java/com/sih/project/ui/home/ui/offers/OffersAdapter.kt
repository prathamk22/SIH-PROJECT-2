package com.sih.project.ui.home.ui.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sih.project.databinding.OffersItemBinding
import com.sih.project.model.OffersEntity
import com.sih.project.util.loadImage


class OffersAdapter : ListAdapter<OffersEntity, OffersViewHolder>(
    object : DiffUtil.ItemCallback<OffersEntity>() {
        override fun areItemsTheSame(oldItem: OffersEntity, newItem: OffersEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OffersEntity, newItem: OffersEntity): Boolean {
            return oldItem.toString().equals(newItem.toString(), true)
        }

    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OffersViewHolder {
        return OffersViewHolder(
            OffersItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OffersViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}

class OffersViewHolder(private val binding: OffersItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: OffersEntity?, position: Int) {
        if (item == null)
            return
        binding.apply {
            image.loadImage(item.imageUrl)
            title.text = item.title
            companyName.text = item.subtitle
            coins.text = "${item.swatchhCoins} coins"
            message.text = item.message
        }
    }

}
