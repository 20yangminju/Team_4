package com.example.team_project.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class SettingRepository {
    val database = Firebase.database
    val ref = database.getReference("restaurant").child("설정")
    val foodList = arrayListOf(true, true, true, true)
    val localList = arrayListOf(true, true, true)
    fun observeSetting(favorite: MutableLiveData<Boolean>, local: MutableLiveData<ArrayList<Boolean>>, food: MutableLiveData<ArrayList<Boolean>>){
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                when (snapshot.key) {
                    "favorite" -> favorite.postValue(snapshot.value as Boolean)
                    "화전" -> {
                        localList[0] = snapshot.value as Boolean
                        local.postValue(localList)
                    }
                    "홍대" -> {
                        localList[1] = snapshot.value as Boolean
                        local.postValue(localList)
                    }
                    "행신" -> {
                        localList[2] = snapshot.value as Boolean
                        local.postValue(localList)
                    }
                    "한식" -> {
                        foodList[0] = snapshot.value as Boolean
                        food.postValue(foodList)
                    }
                    "중식" -> {
                        foodList[1] = snapshot.value as Boolean
                        food.postValue(foodList)
                    }
                    "일식" -> {
                        foodList[2] = snapshot.value as Boolean
                        food.postValue(foodList)
                    }
                    "양식" -> {
                        foodList[3] = snapshot.value as Boolean
                        food.postValue(foodList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun modify(setting: Int, index: Int, newValue: Boolean) {
        when(setting) {
            0 -> ref.child("favorite").setValue(newValue)
            1 -> when(index) {
                0 -> {
                    ref.child("local").child("화전").setValue(newValue)
                    localList[0] = newValue
                }

                1 -> {
                    ref.child("local").child("홍대").setValue(newValue)
                    localList[1] = newValue
                }

                2 -> {
                    ref.child("local").child("행신").setValue(newValue)
                    localList[2] = newValue
                }
            }
            2 -> when(index) {
                0 -> {
                    ref.child("food").child("한식").setValue(newValue)
                    foodList[0] = newValue
                }
                1 -> {
                    ref.child("food").child("중식").setValue(newValue)
                    foodList[1] = newValue
                }
                2 -> {
                    ref.child("food").child("일식").setValue(newValue)
                    foodList[2] = newValue
                }
                3 -> {
                    ref.child("food").child("양식").setValue(newValue)
                    foodList[3] = newValue
                }
            }
        }
    }
}