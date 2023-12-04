package com.example.team_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.team_project.databinding.FragmentMenuBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {

    private var receivedString: String? = null
    private lateinit var databaseReference: DatabaseReference

    val menus = ArrayList<Menu>()

    private var _binding : FragmentMenuBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        val receivedBundle = arguments
        receivedString = receivedBundle?.getString("key", "defaultRestaurant")

        binding.recMenus.layoutManager = LinearLayoutManager(context)
        readData(receivedString)
        binding.recMenus.adapter = receivedString?.let { MenusAdapter(menus, this, it) }


        return view
    }

    fun readData(menukey: String?){
        if (menukey != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("restaurant")
            databaseReference.child(menukey).child("menu").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    menus.clear()

                    for (ds in snapshot.children) {
                        val name = ds.key.toString()
                        val price = ds.child("price").getValue(String::class.java)
                        val imageUrl = ds.child("imageUrl").getValue(String::class.java)?:""

                        val menu = Menu(name, price ?: "", imageUrl)
                        menus.add(menu)

                    }

                    val menusAdapter = MenusAdapter(menus, this@MenuFragment, menukey)
                    binding.recMenus.adapter = menusAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
        })
    }
}}

