package com.example.team_project.repository

import androidx.lifecycle.MutableLiveData
import com.example.team_project.RecentRestaurant
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class AnalysisRepository {
    // 파이어 베이스 참조
    private val ref = Firebase.database.getReference("analysis")

    // firebase의 변화 발생 시 viewModel의 Livedata 갱신
    fun observeAnalysis(recent: MutableLiveData<ArrayList<RecentRestaurant>>){
        ref.limitToLast(10).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newAnalysisList = ArrayList<RecentRestaurant>()
                snapshot.children.forEach { dataSnapshot ->
                    val name = dataSnapshot.child("name").value.toString()
                    val type = dataSnapshot.child("type").value.toString()
                    val menu = dataSnapshot.child("menu").value.toString()
                    val price = dataSnapshot.child("price").value.toString()
                    val url = dataSnapshot.child("imageURL").value.toString()
                    val addData = RecentRestaurant(name, type, menu, price, url)
                    newAnalysisList.add(addData)
                }
                newAnalysisList.reverse() // 새로 추가한 메뉴가 맨 위에 오도록 뒤집기
                recent.postValue(newAnalysisList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun addRecent(newValue: RecentRestaurant, idx: Int){
        // 파이어 베이스에 새로운 데이터 추가
        val idxRef = ref.child(idx.toString())
        idxRef.child("menu").setValue(newValue.menu)
        idxRef.child("name").setValue(newValue.name)
        idxRef.child("price").setValue(newValue.price)
        idxRef.child("type").setValue(newValue.type)
        idxRef.child("imageURL").setValue(newValue.url)
    }

    fun reset() {
        // analysis 데이터 전체 삭제
        ref.removeValue()
            .addOnSuccessListener {}
            .addOnCanceledListener {}
    }
}