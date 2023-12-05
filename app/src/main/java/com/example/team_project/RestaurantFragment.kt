package com.example.team_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.team_project.databinding.FragmentRestaurantBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import androidx.core.content.ContextCompat
import com.example.team_project.R

class RestaurantFragment : Fragment() {

    private var binding: FragmentRestaurantBinding? = null
    private lateinit var databaseReference: DatabaseReference
    private var receivedString: String? = null
    private var isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantBinding.inflate(inflater)
        val receivedBundle = arguments
        receivedString = receivedBundle?.getString("key", "default")
        readData(receivedString)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Lifecycle", "onViewCreated() called")

        binding?.favButton?.setOnClickListener {
            receivedString?.let { restaurantKey ->
                val isFavoriteRef =
                    FirebaseDatabase.getInstance().getReference("restaurant").child(restaurantKey)
                        .child("isfavorite")

                isFavoriteRef.runTransaction(object : Transaction.Handler {
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
                        if (committed) {
                            isFavorite = currentData?.getValue(Boolean::class.java) ?: false
                            isFavoriteRef.setValue(isFavorite)
                            // isFavorite 값에 따라 버튼 색상 변경
                            updateFavoriteButtonColor(isFavorite)
                        } else {
                            error?.toException()?.let {
                                Log.e("Firebase", "Transaction failed", it)
                            }
                        }
                    }
                })
            }
        }

        receivedString?.let { restaurantKey ->
            val isFavoriteRef =
                FirebaseDatabase.getInstance().getReference("restaurant").child(restaurantKey)
                    .child("isfavorite")

            isFavoriteRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    isFavorite = snapshot.getValue(Boolean::class.java) ?: false
                    Log.d("Firebase", "Fetched isFavorite: $isFavorite")
                    // isFavorite 값에 따라 버튼 색상 변경
                    updateFavoriteButtonColor(isFavorite)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
        binding?.menuButton?.setOnClickListener {
            val bundle = Bundle()
            val myString = receivedString
            bundle.putString("key", myString)
            findNavController().navigate(R.id.action_restaurantFragment_to_menuFragment, bundle)
        }
    }

    override fun onResume() {
        super.onResume()
        // 프래그먼트가 다시 화면에 나타날 때 버튼 색상 업데이트
        updateFavoriteButtonColor(isFavorite)
    }

    // isFavorite 값에 따라 버튼 색상 변경
    private fun updateFavoriteButtonColor(isFavorite: Boolean) {
        val button = binding?.favButton
        button?.post {
            val colorResId =
                if (isFavorite) R.color.favorite_button_selected else R.color.favorite_button_unselected
            val color = ContextCompat.getColor(requireContext(), colorResId)
            button.setBackgroundColor(color)
            Log.d("UI Update", "Updated button color. isFavorite: $isFavorite")
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
                    val URL =
                        "https://firebasestorage.googleapis.com/v0/b/team-4-a91c7.appspot.com/o/${restaurantName}.png?alt=media"

                    binding?.restaurantdel?.post {
                        if (del is Boolean && del) {
                            binding?.restaurantdel?.text = "배달 가능"
                        } else {
                            binding?.restaurantdel?.text = "배달 불가능"
                        }
                    }

                    binding?.restaurantImage?.let { Glide.with(it.context).load(URL).into(it) }

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
                }
            })
        }
    }
}

