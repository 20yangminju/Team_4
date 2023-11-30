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

    private val _priceList = arrayListOf(0f, 0f ,0f ,0f ,0f, 0f) // [total, koreanPrice, chinesePrice, japanesePrice, westernPrice, fastPrice]
    val priceList: ArrayList<Float> get() =  _priceList

    init {
        repository.observeAnalysis(_recent)
    }
    val recent: LiveData<ArrayList<RecentRestaurant>> get() = _recent

    fun setRecent(newValue: RecentRestaurant){
        repository.addRecent(newValue, _recent.value?.size ?: 0)
    }

    fun setGraph() {
        _recent.value?.forEach { recentRestaurant ->
            val curPrice = recentRestaurant.price.replace(",", "").toFloatOrNull() ?: 0f
            when (recentRestaurant.type) {
                "korean" -> _priceList[1] += curPrice
                "japanese" -> _priceList[3] += curPrice
                "chinese" -> _priceList[2] += curPrice
                "western" -> _priceList[4] += curPrice
                "fastfood" -> _priceList[5] += curPrice
            }
            _priceList[0] += curPrice
        }
        if(_priceList[0] != 0f) {
            for (i in 1..5) {
                _priceList[i] /= _priceList[0]
            }
        }
    }
}
