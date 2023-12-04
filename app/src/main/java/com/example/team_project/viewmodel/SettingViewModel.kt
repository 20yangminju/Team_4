package com.example.team_project.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.team_project.repository.SettingRepository

class SettingViewModel : ViewModel() {
    private val _favorite = MutableLiveData(true)
    val favorite: LiveData<Boolean> get() = _favorite

    private val _delivery = MutableLiveData(true)
    val delivery: LiveData<Boolean> get() = _delivery

    private val _local = MutableLiveData(arrayListOf(true, true, true)) // [화전, 홍대, 행신]
    val local: LiveData<ArrayList<Boolean>> get() = _local

    private val _food = MutableLiveData(arrayListOf(true, true, true, true, true)) // [한식, 중식, 일식, 양식, 분식]
    val food: LiveData<ArrayList<Boolean>> get() = _food

    private val repository = SettingRepository()
    init {
        repository.observeSetting(_favorite, _delivery, _local, _food)
    }

    // viewModel -> Fragment로 전달
    val isFavor get() = _favorite.value == true

    val isDelivery get() = _delivery.value == true

    val isHwa get() = _local.value?.get(0) == true
    val isHong get() = _local.value?.get(1) == true
    val isHaeng get() = _local.value?.get(2) == true

    val isKorean get() = _food.value?.get(0) == true
    val isChinese get() = _food.value?.get(1) == true
    val isJapanese get() = _food.value?.get(2) == true
    val isWestern get() = _food.value?.get(3) == true
    val isFast get() = _food.value?.get(4) == true

    // setting - index
    // 0 - 0: setFavor
    // 1 - 0: setDeliver
    // 2 - 0: setHwa, 2 - 1: setHong, 2 - 2: setHaeng
    // 3 - 0: setKorean, 3 - 1: setChinese, 3- 2: setJapanese 3 - 3: setWestern

    // Fragment -> viewModel로 전달 (인덱스로 수정 대상 지정)
    fun setFavor(newValue: Boolean) {
        _favorite.value = newValue
        repository.modify(0, 0, newValue)
    }

    fun setDelivery(newValue: Boolean) {
        _delivery.value = newValue
        repository.modify(1, 0, newValue)
    }

    fun setHwa(newValue: Boolean) {
        _local.value?.set(0, newValue)
        repository.modify(2, 0, newValue)
    }
    fun setHong(newValue: Boolean) {
        _local.value?.set(1, newValue)
        repository.modify(2, 1, newValue)
    }
    fun setHaeng(newValue: Boolean) {
        _local.value?.set(2, newValue)
        repository.modify(2, 2, newValue)
    }

    fun setKorean(newValue: Boolean) {
        _food.value?.set(0, newValue)
        repository.modify(3, 0, newValue)
    }
    fun setChinese(newValue: Boolean) {
        _food.value?.set(1, newValue)
        repository.modify(3, 1, newValue)
    }
    fun setJapanese(newValue: Boolean) {
        _food.value?.set(2, newValue)
        repository.modify(3, 2, newValue)
    }
    fun setWestern(newValue: Boolean) {
        _food.value?.set(3, newValue)
        repository.modify(3, 3, newValue)
    }
    fun setFast(newValue: Boolean) {
        _food.value?.set(4, newValue)
        repository.modify(3, 4, newValue)
    }
}