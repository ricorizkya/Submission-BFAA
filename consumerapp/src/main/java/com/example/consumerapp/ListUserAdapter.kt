package com.example.consumerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class ListUserAdapter(private var listData: ArrayList<User>, private val dataClickCallback: DataClickCallback)
    : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>(), Filterable {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_github, viewGroup, false)
        return ListViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listData[position], dataClickCallback)
    }

    override fun getItemCount(): Int = listData.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowGithubBinding.bind(itemView)
        fun bind(user: User, dataClickCallback: DataClickCallback) {
            itemView.apply {
                binding.tvUsername.text = user.username
                binding.tvType.text = user.type
                Glide.with(context)
                    .load(user.avatar)
                    .into(binding.imgPhotoUser)
                itemView.setOnClickListener {
                    dataClickCallback.onClick(it, user)
                }
            }
        }
    }

    interface DataClickCallback {
        fun onClick(view: View, listData: User)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val search = constraint.toString()
                listData = if (search.isEmpty()) {
                    listData
                }else {
                    val result = ArrayList<User>()
                    for (i in listData) {
                        if ((i.username.toString().toLowerCase(Locale.ROOT).contains(search.toLowerCase(
                                Locale.ROOT)))) {
                            result.add(User(
                                i.username,
                                i.name,
                                i.avatar,
                                i.type,
                            ))
                        }
                    }
                    result
                }
                val filter = FilterResults()
                filter.values = listData
                return filter
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listData = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }
}