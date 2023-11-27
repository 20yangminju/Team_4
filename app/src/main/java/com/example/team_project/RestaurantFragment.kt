package com.example.team_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.team_project.databinding.FragmentRestaurantBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.values

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantFragment : Fragment() {

    private var binding : FragmentRestaurantBinding? = null
    private lateinit var databaseReference: DatabaseReference
    private var receivedString: String? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantBinding.inflate(inflater)
        val receivedBundle = arguments
        receivedString = receivedBundle?.getString("key", "맥도날드")
        readData(receivedString)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.favButton?.setOnClickListener{
            receivedString?.let{
                restaurantKey ->
                val isFavoriteRef = FirebaseDatabase.getInstance().getReference("restaurant").child(restaurantKey).child("isfavorite")

                isFavoriteRef.runTransaction(object : Transaction.Handler{
                    override fun doTransaction(currentData: MutableData): Transaction.Result {
                        if (currentData.value == null || !(currentData.value is Boolean))
                            currentData.value = false
                        else
                            currentData.value = !(currentData.value as Boolean)
                        return Transaction.success(currentData)
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        currentData: DataSnapshot?
                    ) {
                        if (committed){
                            val isFavorite = currentData?.getValue(Boolean::class.java)
                            isFavorite?.let {
                                isFavoriteRef.setValue(it)
                            }
                        } else{
                            error?.toException()?.let {
                                Log.e("Firebase", "Transaction failed", it)
                            }
                        }
                    }
                })
            }
        }
        binding?.menuButton?.setOnClickListener{
            val bundle = Bundle()
            val myString = receivedString
            bundle.putString("key", myString)
            findNavController().navigate(R.id.action_restaurantFragment_to_menuFragment, bundle)
        }
    }





    fun readData(restaurantKey: String?) {
        if (restaurantKey != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("restaurant")
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val restaurantInfo = snapshot.child(restaurantKey).child("info")

                    val restaurantName = restaurantKey
                    val hours = restaurantInfo.child("hours").value
                    val contacts = restaurantInfo.child("contacts").value
                    val del = restaurantInfo.child("delivery").value
                    val addr = restaurantInfo.child("address").value

                    // 데이터 체크 및 UI 갱신을 메인 스레드에서 수행
                    binding?.restaurantdel?.post {
                        if (del is Boolean && del) {
                            binding?.restaurantdel?.text = "배달 가능"
                        } else {
                            binding?.restaurantdel?.text = "배달 불가능"
                        }
                    }

                    binding?.restaurantadd?.post {
                        binding?.restaurantadd?.text = addr?.toString() ?: ""
                    }

                    binding?.restaurantName?.post {
                        binding?.restaurantName?.text = restaurantName ?: ""
                    }

                    binding?.restauranthours?.post {
                        binding?.restauranthours?.text = hours?.toString() ?: ""
                    }

                    binding?.restaurantcon?.post {
                        binding?.restaurantcon?.text = contacts?.toString() ?: ""
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // 에러 처리
                }
            })
        }
    }




}