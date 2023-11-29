package com.example.team_project.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.team_project.RecentRestaurant
import com.example.team_project.repository.AnalysisRepository

class AnalysisViewModel : ViewModel() {
    private val _recent = MutableLiveData(ArrayList<RecentRestaurant>())
    private val repository = AnalysisRepository()

    init {
        repository.observeAnalysis(_recent)
    }
    val recent: LiveData<ArrayList<RecentRestaurant>> get() = _recent

    fun setRecent(newValue: RecentRestaurant){
            repository.addRecent(newValue, _recent.value?.size ?: 0)
    }

    fun getPriceList(): ArrayList<Float> = repository.setPrice() // [total, koreanPrice, chinesePrice, japanesePrice, westernPrice]

}