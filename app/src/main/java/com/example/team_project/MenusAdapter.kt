package com.example.team_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.team_project.databinding.ListMenusBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MenusAdapter(val menus: ArrayList<Menu>, val fragment: Fragment, val restaurant: String)
    : RecyclerView.Adapter<MenusAdapter.Holder>() {

    private  val database = Firebase.database
    private val myRef = database.getReference("restaurant")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListMenusBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding, fragment, restaurant)

    }

    override fun onBindViewHolder(holder: MenusAdapter.Holder, position: Int) {
        holder.bind(menus[position])
    }

    override fun getItemCount() = menus.size

    class Holder(private val binding: ListMenusBinding, private val fragment: Fragment, private val restaurant: String):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu){
            binding.menuName.text = menu.name
            binding.menuPrice.text = menu.price

            binding.Buy.setOnClickListener{
                val bundle = Bundle()
                bundle.putString("name", restaurant)
                bundle.putString("price", menu.price)
                bundle.putString("menu", menu.name)
                fragment.findNavController().navigate(R.id.action_menuFragment_to_analysisFragment, bundle)
            }
        }

    }
}
