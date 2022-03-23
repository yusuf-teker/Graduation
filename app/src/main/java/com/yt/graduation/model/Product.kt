package com.yt.graduation.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Product(
    var productName: String="",
    var productPrice: Int=-1,
    var productDescription: String="",
    var productCategory: String="Other",
    var productUploadDate: String="",
    var productOwner: String="",
    var productImage: String="",
    var productState: Boolean =true,
    var productKey: String? = null) : Parcelable {


}