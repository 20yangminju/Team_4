package com.example.team_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.team_project.databinding.ItemSearchResultBinding

class SearchResultsAdapter(private val results: List<SearchResultItem>, private val fragment: Fragment) :
    RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(resultItem: SearchResultItem) {
            binding.searchResultTextView.text = resultItem.name
            binding.searchResultTextView.setOnClickListener{
                val bundle = Bundle()
                bundle.putString("key", resultItem.name)
                fragment.findNavController().navigate(R.id.action_mainFragment_to_restaurantFragment, bundle)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resultItem = results[position]
        holder.bind(resultItem)
    }

    override fun getItemCount(): Int {
        return results.size
    }
}
