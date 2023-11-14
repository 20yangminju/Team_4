package com.example.team_project.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {
    private val _favorite = MutableLiveData(true)

    private val _local = MutableLiveData(arrayListOf(true, true, true))
    // [화전, 홍대, 행신]
    private val _food = MutableLiveData(arrayListOf(true, true, true, true))
    // [한식, 중식, 일식, 양식]

    val favorite: LiveData<Boolean> get() = _favorite
    val local: LiveData<ArrayList<Boolean>> get() = _local
    val food: LiveData<ArrayList<Boolean>> get() = _food

    val isfavor get() = _favorite.value ?: false

    val isHwa get() = _local.value?.get(0) == true
    val isHong get() = _local.value?.get(1) == true
    val isHaeng get() = _local.value?.get(2) == true

    val isKorean get() = _food.value?.get(0) == true
    val isChinese get() = _food.value?.get(1) == true
    val isJapanese get() = _food.value?.get(2) == true
    val isWestern get() = _food.value?.get(3) == true

    fun setFavor(newValue: Boolean) {
        _favorite.value = newValue
    }
    fun setHwa(newValue: Boolean) {
        _local.value?.let {
            it[0] = newValue
        }
    }
    fun setHong(newValue: Boolean) {
        _local.value?.let {
            it[1] = newValue
        }
    }
    fun setHaeng(newValue: Boolean) {
        _local.value?.let {
            it[2] = newValue
        }
    }
    fun setKorean(newValue: Boolean) {
        _food.value?.let {
            it[0] = newValue
        }
    }
    fun setChinese(newValue: Boolean) {
        _food.value?.let {
            it[1] = newValue
        }
    }
    fun setJapanese(newValue: Boolean) {
        _food.value?.let {
            it[2] = newValue
        }
    }
    fun setWestern(newValue: Boolean) {
        _food.value?.let {
            it[3] = newValue
        }
    }
}