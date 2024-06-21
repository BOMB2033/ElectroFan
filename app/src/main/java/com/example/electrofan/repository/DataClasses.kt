package com.example.electrofan.repository
data class DataClass(
    var users: List<User>,
    var products: List<Product>,
)

data class User(
    var uid: String = "",
    var email: String = "",
    var isAdmin:Boolean = false,
    var uidProducts: List<Product> = emptyList(),
)

data class Product(
    var uid:String = "",
    var name:String = "",
    var about:String = "",
    var cast:Int = 0,
)