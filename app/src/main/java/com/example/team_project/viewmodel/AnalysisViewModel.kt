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

    private var _total = 0f
    private var _koreanPrice = 0f
    private var _chinesePrice = 0f
    private var _japanesePrice = 0f
    private var _westernPrice = 0f

    val koreanPrice get() =  _koreanPrice
    val japansePrice get() = _japanesePrice
    val chinesePrice get() = _chinesePrice
    val westernPrice get() = _westernPrice

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
                "korean" -> _koreanPrice += curPrice
                "japanese" -> _japanesePrice += curPrice
                "chinese" -> _chinesePrice += curPrice
                "western" -> _westernPrice += curPrice
            }
            _total += curPrice
        }
        if(_total != 0f) {
            _koreanPrice /= _total
            _japanesePrice /= _total
            _chinesePrice /= _total
            _westernPrice /= _total
        }
    }
}