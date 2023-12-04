package com.example.team_project.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class SettingRepository {
    private val ref = Firebase.database.getReference("설정")

    // firebase 값을 저장할 배열 (post를 위해)
    val foodList = arrayListOf(true, true, true, true, true)
    val localList = arrayListOf(true, true, true)

    // firebase의 변화 발생 시 viewModel의 Livedata 갱신
    fun observeSetting(favorite: MutableLiveData<Boolean>, delivery: MutableLiveData<Boolean>, local: MutableLiveData<ArrayList<Boolean>>, food: MutableLiveData<ArrayList<Boolean>>){
        ref.child("favorite").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favorite.postValue(snapshot.value as Boolean)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        ref.child("delivery").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                delivery.postValue(snapshot.value as Boolean)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        ref.child("food").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { dataSnapshot ->
                    when (dataSnapshot.key.toString()) {
                        "한식" -> {
                            foodList[0] = dataSnapshot.value as Boolean
                            food.postValue(foodList)
                        }
                        "중식" -> {
                            foodList[1] = dataSnapshot.value as Boolean
                            food.postValue(foodList)
                        }
                        "일식" -> {
                            foodList[2] = dataSnapshot.value as Boolean
                            food.postValue(foodList)
                        }
                        "양식" -> {
                            foodList[3] = dataSnapshot.value as Boolean
                            food.postValue(foodList)
                        }
                        "분식" -> {
                            foodList[4] = dataSnapshot.value as Boolean
                            food.postValue(foodList)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        // firebase내 local 설정값 변경 적용
        ref.child("local").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {dataSnapshot ->
                    when (dataSnapshot.key.toString()){
                        "화전" -> {
                            localList[0] = dataSnapshot.value as Boolean
                            local.postValue(localList)
                        }
                        "홍대" -> {
                            localList[1] = dataSnapshot.value as Boolean
                            local.postValue(localList)
                        }
                        "행신" -> {
                            localList[2] = dataSnapshot.value as Boolean
                            local.postValue(localList)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // viewModel 수정 -> firebase 수정
    fun modify(setting: Int, index: Int, newValue: Boolean) {
        when(setting) {
            0 -> ref.child("favorite").setValue(newValue)
            1 -> ref.child("delivery").setValue(newValue)
            2 -> when(index) {
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
            3 -> when(index) {
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
                4 -> {
                    ref.child("food").child("분식").setValue(newValue)
                    foodList[4] = newValue
                }
            }
        }
    }
}