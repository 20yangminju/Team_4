package com.example.team_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.team_project.databinding.FragmentAnalysisBinding
import com.example.team_project.databinding.FragmentSettingsBinding
import com.example.team_project.repository.SettingRepository
import com.example.team_project.viewmodel.SettingViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.database.values

class SettingsFragment : Fragment() {
    val viewModel: SettingViewModel by activityViewModels()
    private var binding: FragmentSettingsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // viewModel 변화 발생 시, view 갱신
        viewModel.favorite.observe(viewLifecycleOwner) {
            binding?.favoriteRestaurantSwitch?.isChecked = viewModel.isFavor
        }
        viewModel.local.observe(viewLifecycleOwner) {
            binding?.hwajeonCheckbox?.isChecked = viewModel.isHwa
            binding?.haengsinCheckbox?.isChecked = viewModel.isHaeng
            binding?.hongdaeCheckbox?.isChecked = viewModel.isHong
        }
        viewModel.food.observe(viewLifecycleOwner) {
            binding?.koreanFoodCheckbox?.isChecked = viewModel.isKorean
            binding?.chineseFoodCheckbox?.isChecked = viewModel.isChinese
            binding?.japaneseFoodCheckbox?.isChecked = viewModel.isJapanese
            binding?.westernFoodCheckbox?.isChecked = viewModel.isWestern
        }

        // view 변화 발생 시, viewModel 갱신
        binding?.favoriteRestaurantSwitch?.setOnClickListener {
            viewModel.setFavor(binding?.favoriteRestaurantSwitch?.isChecked ?: false)
        }
        binding?.hwajeonCheckbox?.setOnClickListener {
            viewModel.setHwa(binding?.hwajeonCheckbox?.isChecked ?: false)
        }
        binding?.haengsinCheckbox?.setOnClickListener {
            viewModel.setHaeng(binding?.haengsinCheckbox?.isChecked ?: false)
        }
        binding?.hongdaeCheckbox?.setOnClickListener {
            viewModel.setHong(binding?.hongdaeCheckbox?.isChecked ?: false)
        }
        binding?.koreanFoodCheckbox?.setOnClickListener {
            viewModel.setKorean(binding?.koreanFoodCheckbox?.isChecked ?: false)
        }
        binding?.chineseFoodCheckbox?.setOnClickListener {
            viewModel.setChinese(binding?.chineseFoodCheckbox?.isChecked ?: false)
        }
        binding?.japaneseFoodCheckbox?.setOnClickListener {
            viewModel.setJapanese(binding?.japaneseFoodCheckbox?.isChecked ?: false)
        }
        binding?.westernFoodCheckbox?.setOnClickListener {
            viewModel.setWestern(binding?.westernFoodCheckbox?.isChecked ?: false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}