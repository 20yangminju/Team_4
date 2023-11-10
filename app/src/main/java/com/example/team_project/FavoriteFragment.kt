package com.example.team_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.team_project.databinding.FragmentFavoriteBinding


class FavoriteFragment : Fragment() {
    lateinit var binding: FragmentFavoriteBinding

    val restaurants = arrayOf(
        Favorite_restaurant("삼호정", food_type.Chinese, location.화전),
        Favorite_restaurant("부탄츄", food_type.Japanese, location.홍대),
        Favorite_restaurant("프로메사", food_type.western, location.행신),
        Favorite_restaurant("꼬기꼬기", food_type.Korean, location.화전),
        Favorite_restaurant("맘스터치", food_type.Fastfood, location.배달),
        Favorite_restaurant("로쏘", food_type.western, location.홍대),
        Favorite_restaurant("막저", food_type.Korean, location.행신),
        Favorite_restaurant("쭈꾸미삼겹살", food_type.Korean, location.화전)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        binding.recFavorite.layoutManager = LinearLayoutManager(context)
        binding.recFavorite.adapter = FavoriteAdapter(restaurants)
        return binding.root
    }




}