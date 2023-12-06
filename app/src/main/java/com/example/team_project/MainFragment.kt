package com.example.team_project

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.team_project.databinding.FragmentMainBinding
import com.example.team_project.viewmodel.SettingViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.widget.Toast


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class MainFragment : Fragment() {

    var binding: FragmentMainBinding? = null
    private lateinit var databaseReference: DatabaseReference
    private val viewModel: SettingViewModel by activityViewModels()
    private var searchView :SearchView? = null

    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private val searchResultsList = mutableListOf<SearchResultItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        databaseReference = FirebaseDatabase.getInstance().getReference("restaurant")

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchView = binding?.searchView

        searchResultsRecyclerView =binding?.searchRecyclerView ?: return
        searchResultsAdapter = SearchResultsAdapter(searchResultsList, this)
        searchResultsRecyclerView.adapter = searchResultsAdapter
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())




        binding?.RandomGet?.setOnClickListener {
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot){
                        val keys = snapshot.children.map { it.key }.toMutableList()
                        val filteredKeys = keys.filter { key ->
                            val isfavorite = snapshot.child(key.toString()).child("isfavorite").getValue() as? Boolean
                            val delivery = snapshot.child(key.toString()).child("info").child("delivery").value as? Boolean
                            val foodType = snapshot.child(key.toString()).child("info").child("foodtype").getValue()?.toString()
                            val location = snapshot.child(key.toString()).child("info").child("location").getValue()?.toString()

                            val isFavoriteCheck = isfavorite != null && viewModel.isFavor && isfavorite == false
                            val deliveryCheck = delivery != null && viewModel.isDelivery && delivery == false
                                // 조건에 맞지 않으면 true 반환하여 리스트에 포함되지 않도록 함.
                            !(!viewModel.isJapanese && foodType == "japanese" ||
                                    !viewModel.isKorean && foodType == "korean" ||
                                    !viewModel.isChinese && foodType == "chinese" ||
                                    !viewModel.isWestern && foodType == "western" ||
                                    !viewModel.isFast && foodType == "fastfood" ||
                                    isFavoriteCheck ||
                                    deliveryCheck ||
                                    !viewModel.isHaeng && location == "행신" ||
                                    !viewModel.isHong && location == "홍대" ||
                                    !viewModel.isHwa && location == "화전" ||
                                    key == "Error"
                                    )
                            }
                            val randomKey = filteredKeys.randomOrNull() ?: "Error"
                            val bundle = Bundle()
                            bundle.putString("key", randomKey)
                            findNavController().navigate(R.id.action_mainFragment_to_restaurantFragment, bundle)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (newText.isNullOrBlank()) {
                            searchResultsList.clear()
                            updateSearchResults(searchResultsList)

                        } else {
                            searchResultsList.clear()
                            for (child in snapshot.children) {
                                val rastaurantName = child.key.toString()
                                rastaurantName?.let {
                                    if (it.contains(newText, ignoreCase = true)) {
                                        val searchResultItem = SearchResultItem(it)
                                        searchResultsList.add(searchResultItem)
                                    }
                                }
                            }
                            updateSearchResults(searchResultsList)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                return true
            }
            override fun onQueryTextSubmit(query: String?) : Boolean{
                val processedQuery = query?.trim()?.replace("\\s".toRegex(), "")
                if (!processedQuery.isNullOrBlank()) {
                    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.hasChild(processedQuery)) {
                                showToast("검색 결과가 없습니다.")
                            } else {
                                val bundle = Bundle()
                                bundle.putString("key", processedQuery)
                                if (findNavController().currentDestination?.id == R.id.mainFragment) {
                                    findNavController().navigate(R.id.action_mainFragment_to_restaurantFragment, bundle)
                                }
                                }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                } else {
                    showToast("검색어를 입력해주세요.")
                }
                return true
            }

            private fun showToast(message: String) {
                val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                toast.show()
            }

        })




        binding?.imageButton?.setOnClickListener {
            // imageButton 버튼이 눌리면 MainFragment -> SettingFragment로 화면 전환
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }
        binding?.imageButton2?.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_favoriteFragment)
        }
        binding?.imageButton3?.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_analysisFragment)
        }
    }

    private fun updateSearchResults(results: List<SearchResultItem>){
        searchResultsAdapter.notifyDataSetChanged()
        searchResultsRecyclerView.visibility = if(results.isNotEmpty()) View.VISIBLE else View.GONE
    }


}