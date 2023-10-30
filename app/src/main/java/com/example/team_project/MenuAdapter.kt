package com.example.team_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.team_project.databinding.ListMenuBinding

class MenuAdapter: RecyclerView.Adapter<MenuAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListMenuBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)

    }

    override fun getItemCount() = menus.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(menus[position])

    }

    class Holder(private val binding: ListMenuBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu){
            binding.menuName = menu.name
            binding.menuPrice = menu.price
        }
    }
}