package com.sih.project.ui.userVerificationRequired

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sih.project.databinding.UserVerificationItemBinding
import com.sih.project.model.PostsStatus
import com.sih.project.model.UserPosts
import com.sih.project.util.loadImage

class UserVerificationAdapter(private val verificationListener: (UserPosts) -> Unit) : ListAdapter<UserPosts, UserVerificationViewHolder>(
    object : DiffUtil.ItemCallback<UserPosts>() {
        override fun areItemsTheSame(oldItem: UserPosts, newItem: UserPosts): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserPosts, newItem: UserPosts): Boolean {
            return oldItem.toString().equals(newItem.toString(), true)
        }

    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVerificationViewHolder {
        return UserVerificationViewHolder(UserVerificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), verificationListener)
    }

    override fun onBindViewHolder(holder: UserVerificationViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}

class UserVerificationViewHolder(
    private val binding: UserVerificationItemBinding,
    private val verificationListener: (UserPosts) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
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
        binding.verifyPost.setOnClickListener {
            verificationListener.invoke(item)
        }
    }

}
