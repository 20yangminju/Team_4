package com.example.team_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.team_project.databinding.FragmentFavoriteBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FavoriteFragment : Fragment() {
    lateinit var binding: FragmentFavoriteBinding



    var restaurants = arrayOf(
        Favorite_restaurant("삼호정", "Chinese", "화전"),
        Favorite_restaurant("부탄츄", "Japanese", "홍대"),
        Favorite_restaurant("꼬기꼬기", "Korean", "화전"),
        Favorite_restaurant("로쏘", "western", "홍대"),
        Favorite_restaurant("막저", "Korean", "행신"),
        Favorite_restaurant("쭈꾸미삼겹살", "Korean", "화전")
    )

    val database = Firebase.database
    val myRef = database.getReference("restorant")




    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)

}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val restaurant = snapshot
                    for(item in restaurant.children){
                        val id : String = item.key.toString()
                        val type : String = item.child("info").child("foodtype").value as String
                        val location : String = item.child("info").child("address").value as String

                        val add = Favorite_restaurant(id, type, location)

                        restaurants += add
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        binding.recFavorite.layoutManager = LinearLayoutManager(context)
        binding.recFavorite.adapter = FavoriteAdapter(restaurants)
        return binding.root
    }




}