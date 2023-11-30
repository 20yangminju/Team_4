package com.example.team_project.repository

import androidx.lifecycle.MutableLiveData
import com.example.team_project.RecentRestaurant
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class AnalysisRepository {
    val analysisList = ArrayList<RecentRestaurant>()

    val database = Firebase.database
    val ref = database.getReference("analysis")

    fun observeAnalysis(recent: MutableLiveData<ArrayList<RecentRestaurant>>){
        ref.limitToLast(10).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val analysisList = ArrayList<RecentRestaurant>()
                for (ds in snapshot.children) {
                    val name = ds.child("name").value.toString()
                    val type = ds.child("type").value.toString()
                    val menu = ds.child("menu").value.toString()
                    val price = ds.child("price").value.toString()
                    val addData = RecentRestaurant(name, type, menu, price)
                    analysisList.add(addData)
                }
                recent.postValue(analysisList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun addRecent(newValue: RecentRestaurant, idx: Int){
        val idxRef = ref.child(idx.toString())
        idxRef.child("menu").setValue(newValue.menu)
        idxRef.child("name").setValue(newValue.name)
        idxRef.child("price").setValue(newValue.price)
        idxRef.child("type").setValue(newValue.type)
        analysisList.add(newValue)
    }
}