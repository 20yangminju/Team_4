package com.example.team_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.team_project.databinding.FragmentFavoriteBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class FavoriteFragment : Fragment() {
    lateinit var binding: FragmentFavoriteBinding
    private lateinit var database: DatabaseReference




    var restaurants = arrayOf<Favorite_restaurant>()






    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)

}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        database = Firebase.database.reference
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        binding.recFavorite.layoutManager = LinearLayoutManager(context)
        binding.recFavorite.adapter = FavoriteAdapter(restaurants)

        return binding.root
    }




}