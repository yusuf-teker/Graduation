package com.yt.graduation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var name: String = "",
    var email: String = "",
    var registrationDate: String = "",
    var image: String = "default",
    var favoriteProducts: ArrayList<String>? = ArrayList<String>(),
    var uid: String = "") : Parcelable {

}
