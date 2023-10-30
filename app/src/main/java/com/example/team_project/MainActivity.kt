package com.example.team_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.team_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)

        // frg_nav의 navController를 가져옴
        val navController = binding.fragmentContainerView3.getFragment<NavHostFragment>().navController

        setupActionBarWithNavController(navController) // frg_nav와 액션바를 연결
        setContentView(binding.root)
    }

    // 뒤로가기 버튼 작동 함수
    override fun onSupportNavigateUp(): Boolean {
        val navController = binding.fragmentContainerView3.getFragment<NavHostFragment>().navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}