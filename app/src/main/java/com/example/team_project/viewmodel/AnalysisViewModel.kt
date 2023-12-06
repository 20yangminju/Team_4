package com.example.team_project.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.team_project.RecentRestaurant
import com.example.team_project.repository.AnalysisRepository

class AnalysisViewModel : ViewModel() {
    private val _recent = MutableLiveData(ArrayList<RecentRestaurant>())
    val recent: LiveData<ArrayList<RecentRestaurant>> get() = _recent

    // 각 인덱스에 가격 정보 저장 (reset을 위해 var 사용)
    private var _priceList = arrayListOf(0f, 0f ,0f ,0f ,0f, 0f) // [total, koreanPrice, chinesePrice, japanesePrice, westernPrice, fastPrice]
    val priceList: ArrayList<Float> get() =  _priceList

    private val repository = AnalysisRepository()
    init {
        repository.observeAnalysis(_recent)
    }

    fun reset() {
        _recent.value = ArrayList()
        _priceList = arrayListOf(0f, 0f ,0f ,0f ,0f, 0f)
        repository.reset()
    }

    fun setRecent(newValue: RecentRestaurant){
        repository.addRecent(newValue, _recent.value?.size ?: 0)
    }

    // 변경된 _recent를 이용해 값을 _priceList를 갱신해주는 함수
    fun setGraph() {
        _recent.value?.forEach { recentRestaurant ->
            val curPrice = recentRestaurant.price.replace(",", "").toFloatOrNull() ?: 0f // 가격에 ,가 들어가므로 형식에 맞게 변경
            when (recentRestaurant.type) {
                "korean" -> _priceList[1] += curPrice
                "japanese" -> _priceList[3] += curPrice
                "chinese" -> _priceList[2] += curPrice
                "western" -> _priceList[4] += curPrice
                "fastfood" -> _priceList[5] += curPrice
            }
            _priceList[0] += curPrice // 총 소비 가격 계산
        }
        // 분모가 0이 되는 경우 제외
        if(_priceList[0] != 0f) {
            for (i in 1..5) {
                _priceList[i] /= _priceList[0]
            }
        }
    }
}
