package com.yt.graduation.model

import android.net.Uri
import java.net.URI

data class Product(
    var productName: String="",
    var productPrice: Int=-1,
    var productDescription: String="",
    var productCategory: String="Other",
    var productUploadDate: String="",
    var productOwner: String="",
    var productImage: String="",
    val productState: Boolean=true) {


}