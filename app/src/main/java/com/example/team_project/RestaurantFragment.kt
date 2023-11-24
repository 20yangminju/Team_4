package com.example.team_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.team_project.databinding.FragmentRestaurantBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var binding : FragmentRestaurantBinding? = null
    private lateinit var databaseReference: DatabaseReference



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantBinding.inflate(inflater)
        val receivedBundle = arguments
        val receivedString = receivedBundle?.getString("key", "맥도날드")
        readData(receivedString)
        return binding?.root
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



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RestaurantFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RestaurantFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}