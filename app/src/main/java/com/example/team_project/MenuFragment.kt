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
class MenuFragment : Fragment() {//파이어베이스에서 레스토랑 메뉴를 읽어서 리사이클러뷰로 표현

    private var receivedString: String? = null //프래그먼트 전달 레스토랑 키
    private lateinit var databaseReference: DatabaseReference

    val menus = ArrayList<Menu>()

    private var _binding : FragmentMenuBinding? = null //recycler view 바인딩
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        val receivedBundle = arguments
        receivedString = receivedBundle?.getString("key", "defaultRestaurant")

        binding.recMenus.layoutManager = LinearLayoutManager(context)
        readData(receivedString)
        binding.recMenus.adapter = receivedString?.let { MenusAdapter(menus, this, it) }


        return view
    }

    fun readData(menukey: String?){ //firebase에서 메뉴 데이터
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

                    }//정보 읽어 Menu 객체 생성 후 리스트 추가

                    val menusAdapter = MenusAdapter(menus, this@MenuFragment, menukey)
                    binding.recMenus.adapter = menusAdapter//어댑터 사용으로 리사이클러뷰
                }

                override fun onCancelled(error: DatabaseError) {
                }
        })
    }
}}

