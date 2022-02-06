package com.yt.graduation.data.model

class User(
    name:String = "",
    nickname:String = "",
    mail:String = "",
    phoneNumber:String = "",

)
{
    companion object{
        private var id = 0
    }

    private var name = ""
    private var nickname = ""
    private var mail = ""
    private var products = ArrayList<Product>()
    private var phoneNumber = ""
    private var comments = ArrayList<String>()
    private var favoriteProducts = ArrayList<Int>() //Urunlerin id'sini tut


}