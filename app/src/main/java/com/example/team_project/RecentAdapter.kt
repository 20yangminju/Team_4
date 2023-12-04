package com.example.team_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.team_project.databinding.ListMenusBinding
import com.example.team_project.databinding.ListRecentRestaurantBinding
import com.example.team_project.viewmodel.AnalysisViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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
            viewHolder.bind(it, position)
        }
    }

    class ViewHolder(private val binding: ListRecentRestaurantBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(recentRestaurant: RecentRestaurant, position: Int) {
            binding.txtMenuValue.text = recentRestaurant.menu
            binding.txtNameValue.text = recentRestaurant.name
            binding.txtPriceValue.text = recentRestaurant.price
            binding.txtTypeValue.text = recentRestaurant.type
            Glide.with(binding.imageView.context).load(recentRestaurant.url).into(binding.imageView)
        }
    }
}
