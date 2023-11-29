package com.example.team_project.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.team_project.RecentRestaurant
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.snapshots

class AnalysisRepository {
    val database = Firebase.database
    val ref = database.getReference("analysis")

    fun observeAnalysis(recent: MutableLiveData<ArrayList<RecentRestaurant>>) {
        ref.limitToLast(10).addValueEventListener(object : ValueEventListener {
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

    fun setPrice(): ArrayList<Float> {
        val priceList = arrayListOf(0f, 0f, 0f, 0f, 0f)
        ref.limitToLast(10).addListenerForSingleValueEvent(object : ValueEventListener {
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

                analysisList.forEach { recentRestaurant ->
                    val curPrice = recentRestaurant.price.replace(",", "").toFloat()
                    when (recentRestaurant.type) {
                        "korean" -> priceList[1] += curPrice
                        "chinese" -> priceList[2] += curPrice
                        "japanese" -> priceList[3] += curPrice
                        "western" -> priceList[4] += curPrice
                    }
                    priceList[0] += curPrice
                }
                if (priceList[0] != 0f) {
                    for (i in 1..4) {
                        priceList[i] /= priceList[0]
                    }
                }
                Log.d("LOG2", "${priceList.joinToString()}")
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return priceList
    }

    fun addRecent(newValue: RecentRestaurant, idx: Int){
        val idxRef = ref.child(idx.toString())
        idxRef.child("menu").setValue(newValue.menu)
        idxRef.child("name").setValue(newValue.name)
        idxRef.child("price").setValue(newValue.price)
        idxRef.child("type").setValue(newValue.type)
    }
}