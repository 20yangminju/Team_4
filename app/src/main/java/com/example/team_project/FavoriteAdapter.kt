package com.example.team_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.team_project.databinding.ListFavoriteBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FavoriteAdapter(
    private var restaurants: ArrayList<Favorite_restaurant>,
    private var fragment: Fragment
) : RecyclerView.Adapter<FavoriteAdapter.Holder>() {

    private val database = Firebase.database
    private val myRef = database.getReference("restaurant")
    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            restaurants.clear() // 기존 데이터를 모두 제거
            for (ds in snapshot.children) {
                val id: String = ds.key.toString()
                val type: String? = ds.child("info").child("foodtype").value as? String
                val location: String? = ds.child("info").child("location").value as? String

                val addData = Favorite_restaurant(id, type.orEmpty(), location.orEmpty())
                restaurants.add(addData)
            }
            notifyDataSetChanged()
        }

        override fun onCancelled(error: DatabaseError) {
            // 오류 처리
        }
    }

    init {
        myRef.addValueEventListener(valueEventListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, fragment)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(restaurants[position])
    }

    override fun getItemCount() = restaurants.size

    class Holder(private val binding: ListFavoriteBinding, private val fragment: Fragment) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurants: Favorite_restaurant) {
            binding.Name.text = restaurants.name
            binding.typefood.text = restaurants.type
            binding.location.text = restaurants.where

            binding.goToRestaurant.setOnClickListener {
                val bundle = Bundle()
                val myString = restaurants.name
                bundle.putString("key", myString)
                fragment.findNavController().navigate(R.id.action_favoriteFragment_to_restaurantFragment, bundle)
            }
        }
    }

    // Fragment 소멸 시 ValueEventListener 해제
    fun onDestroy() {
        myRef.removeEventListener(valueEventListener)
    }
}
