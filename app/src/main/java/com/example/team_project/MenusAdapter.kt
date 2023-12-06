package com.example.team_project

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.team_project.databinding.ListMenusBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.net.URLEncoder

class MenusAdapter(//리사이클러뷰에 메뉴 표현 어댑터
    val menus: ArrayList<Menu>,
    val fragment: Fragment,
    val restaurant: String
) : RecyclerView.Adapter<MenusAdapter.Holder>() {

    private val database = Firebase.database
    private val myRef = database.getReference("restaurant")
    private val storage = Firebase.storage

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListMenusBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding, fragment, restaurant, storage, myRef)
    }

    override fun onBindViewHolder(holder: MenusAdapter.Holder, position: Int) {
        holder.bind(menus[position])
    }

    override fun getItemCount() = menus.size

    class Holder(val binding: ListMenusBinding, private val fragment: Fragment, private val restaurant: String, private val storage: FirebaseStorage, private val myRef: DatabaseReference):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu) {
            binding.menuName.text = menu.name
            binding.menuPrice.text = menu.price

            val menuRef = myRef.child(restaurant).child("menu").child(menu.name)
            menuRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {//파이어베이스에서 url 읽어와 피카소로 구현
                    val imageUrl = dataSnapshot.child("imageUrl").getValue(String::class.java)
                    if (imageUrl != null && imageUrl.isNotEmpty()) {
                        Log.d("MenuAdapter", "Image URL: $imageUrl")
                        Picasso.get()
                            .load(imageUrl)
                            .into(binding.menuImage)
                    } else {
                        Log.e("MenuAdapter", "Image URL not found")
                    }
                }override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("MenuAdapter", "Failed to get image URL", databaseError.toException())
                }
            })

            binding.Buy.setOnClickListener{//메뉴 정보를 분석으로 전달, 이동
                val bundle = Bundle()
                bundle.putString("name", restaurant)
                bundle.putString("price", menu.price)
                bundle.putString("menu", menu.name)
                fragment.findNavController().navigate(R.id.action_menuFragment_to_analysisFragment, bundle)
            }
        }

    }
}
