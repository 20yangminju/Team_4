package com.example.team_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.team_project.databinding.ListFavoriteBinding

class FavoriteAdapter (private val restaurants : Array<Favorite_restaurant>)
    :RecyclerView.Adapter<FavoriteAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListFavoriteBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(restaurants[position])
    }

    override fun getItemCount()=restaurants.size

    class Holder(private val binding: ListFavoriteBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(restaurants: Favorite_restaurant){
            binding.Name.text = restaurants.name
            binding.typefood.text = restaurants.type.toString()
            binding.location.text = restaurants.where.toString()

        }
    }
}