package com.sih.project.ui.home.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sih.project.databinding.HomeItemRvBinding
import com.sih.project.model.PostsStatus
import com.sih.project.model.UserPosts
import com.sih.project.util.loadImage

class HomeRecyclerAdapter : ListAdapter<UserPosts, UserPostsViewHolder>(
    object : DiffUtil.ItemCallback<UserPosts>() {
        override fun areItemsTheSame(oldItem: UserPosts, newItem: UserPosts): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserPosts, newItem: UserPosts): Boolean {
            return oldItem.toString().equals(newItem.toString(), true)
        }

    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostsViewHolder {
        return UserPostsViewHolder(HomeItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserPostsViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}

class UserPostsViewHolder(private val binding: HomeItemRvBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UserPosts?, position: Int) {
        if (item == null)
            return
        binding.homeImage.loadImage(item.posts?.imageUrl)
        binding.personName.text = item.user?.name ?: ""
        binding.userComment.text = item.posts?.userCaption ?: ""
        with(PostsStatus.valueOf(item.posts?.status ?: "")) {
            binding.postStatus.text = text
            binding.postStatus.setTextColor(ContextCompat.getColor(binding.root.context, colorId))
        }
    }

}
