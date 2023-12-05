package com.example.team_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.team_project.databinding.ListRecentRestaurantBinding

class RecentAdapter(private val recentRestaurants: LiveData<ArrayList<RecentRestaurant>>): RecyclerView.Adapter<RecentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListRecentRestaurantBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return recentRestaurants.value?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        recentRestaurants.value?.get(position)?.let {
            viewHolder.bind(it)
        }
    }

    class ViewHolder(private val binding: ListRecentRestaurantBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(recentRestaurant: RecentRestaurant) {
            binding.txtMenuValue.text = recentRestaurant.menu
            binding.txtNameValue.text = recentRestaurant.name
            binding.txtPriceValue.text = "${recentRestaurant.price}원"
            binding.txtTypeValue.text = when (recentRestaurant.type) {
                "korean" -> "한식"
                "chinese" -> "중식"
                "japanese" -> "일식"
                "western" -> "양식"
                "fastfood" -> "Fast food"
                else -> "기타"
            }

            // URL을 통해 imageView에 image 업로드
            Glide.with(binding.imageView.context).load(recentRestaurant.url).into(binding.imageView)
        }
    }
}
