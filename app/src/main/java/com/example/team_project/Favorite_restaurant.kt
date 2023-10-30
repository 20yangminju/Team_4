package com.example.team_project


enum class food_type{
    Korean, Japanese, Chinese, Fastfood, western
}

enum class location{
    화전, 홍대, 행신, 배달
}
class Favorite_restaurant (val name: String, val type : food_type, val where : location){

}