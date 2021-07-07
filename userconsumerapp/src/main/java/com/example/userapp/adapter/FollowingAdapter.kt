package com.example.userconsumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.userconsumerapp.model.User
import com.example.userconsumerapp.R
import com.example.userconsumerapp.model.User
import com.example.userconsumerapp.databinding.ItemRowGithubBinding
import com.example.userapp.R

class FollowingAdapter: RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {

    private val mData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_github, viewGroup, false)
        return ListViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowGithubBinding.bind(itemView)
        fun bind(user: User) {
            with(itemView) {
                binding.tvUsername.text = user.username
                binding.tvType.text = user.type
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(binding.imgPhotoUser)
            }
        }
    }
}