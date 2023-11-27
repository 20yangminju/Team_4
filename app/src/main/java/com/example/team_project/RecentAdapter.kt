package com.example.team_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.team_project.databinding.ListMenusBinding
import com.example.team_project.databinding.ListRecentRestaurantBinding

class RecentAdapter(val recentRestaurants: LiveData<ArrayList<RecentRestaurant>>): RecyclerView.Adapter<RecentAdapter.ViewHolder>() {
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
        fun bind(recentRestaurant: RecentRestaurant){
            binding.txtMenuValue.text = recentRestaurant.menu
            binding.txtNameValue.text = recentRestaurant.name
            binding.txtPriceValue.text = recentRestaurant.price
            binding.txtTypeValue.text = recentRestaurant.type
        }
    }
}