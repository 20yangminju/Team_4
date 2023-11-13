package com.example.team_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.team_project.databinding.ListMenusBinding

class MenusAdapter(val menus: Array<Menu>)
    : RecyclerView.Adapter<MenusAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListMenusBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)

    }

    override fun onBindViewHolder(holder: MenusAdapter.Holder, position: Int) {
        holder.bind(menus[position])
    }

    override fun getItemCount() = menus.size

    class Holder(private val binding: ListMenusBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu){
            binding.menuName.text = menu.name
            binding.menuPrice.text = menu.price
        }
    }
}
