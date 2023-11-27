package com.example.team_project.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.team_project.RecentRestaurant
import com.example.team_project.repository.AnalysisRepository

class AnalysisViewModel : ViewModel() {
    private val _recent = MutableLiveData(ArrayList<RecentRestaurant>())
    val recent: LiveData<ArrayList<RecentRestaurant>> get() = _recent
    private val repository = AnalysisRepository()
    init {
        repository.observeAnalysis(_recent)
    }

    fun setRecent(newValue: RecentRestaurant){
        if (_recent.value?.size ?: 0 < 10) {
            //_recent.value?.add(newValue)
            repository.addRecent(newValue, _recent.value?.size ?: 0)
        }
        else {
            val idx = (_recent.value?.size ?: 0) % 10
            _recent.value?.let {
                it[idx] = newValue
            }
            repository.replaceRecent(newValue, idx)
        }
    }
}