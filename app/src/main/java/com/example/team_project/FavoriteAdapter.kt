package com.example.team_project

import androidx.navigation.fragment.findNavController
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.team_project.databinding.ListFavoriteBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FavoriteAdapter (private var restaurants : Array<Favorite_restaurant>, private val fragment: Fragment)
    :RecyclerView.Adapter<FavoriteAdapter.Holder>() {

    init {
        val database = Firebase.database
        val myRef = database.getReference("restaurant")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val id: String = ds.key.toString()
                    val type: String = ds.child("info").child("foodtype").value as String
                    val location: String = ds.child("info").child("location").value as String

                    val add_Data = Favorite_restaurant(id, type, location)

                    restaurants = restaurants.plus(add_Data)
                }
                notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListFavoriteBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding, fragment)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(restaurants[position])


    }

    override fun getItemCount()=restaurants.size

    class Holder(private val binding: ListFavoriteBinding, private val fragment: Fragment) : RecyclerView.ViewHolder(binding.root){
        fun bind(restaurants: Favorite_restaurant){
            binding.Name.text = restaurants.name
            binding.typefood.text = restaurants.type.toString()
            binding.location.text = restaurants.where.toString()
        }

        init {
            binding?.goToRestaurant?.setOnClickListener {
                fragment.findNavController().navigate(R.id.action_favoriteFragment_to_restaurantFragment)
            }
            }
        }
    }
