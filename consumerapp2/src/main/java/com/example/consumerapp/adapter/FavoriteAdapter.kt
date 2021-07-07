package com.example.consumerapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumerapp.R
import com.example.consumerapp.databinding.ItemFavoriteBinding
import com.example.consumerapp.model.User
import com.example.consumerapp.ui.activity.DetailUserActivity

class FavoriteAdapter(private val activity: Activity): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite: ArrayList<User> = ArrayList()
        set(listFavorite) {
            if (listFavorite.size > 0 ) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = listFavorite.size

    inner class FavoriteViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemFavoriteBinding.bind(view)
        fun bind(user: User) {
            with(itemView) {
                binding.tvUsername.text = user.username
                binding.tvType.text = user.company
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(binding.imgPhotoUser)
                itemView.setOnClickListener {
                    CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            val intent = Intent(activity, DetailUserActivity::class.java)
                            intent.putExtra(DetailUserActivity.EXTRA_POSITION, position)
                            intent.putExtra(DetailUserActivity.EXTRA_USER, user)
                            activity.startActivity(intent)
                            activity.finish()
                        }
                    })
                }
            }
        }
    }
}