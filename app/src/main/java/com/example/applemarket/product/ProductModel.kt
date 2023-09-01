package com.example.applemarket.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    val id: Int,
    val imgFile: String,
    val title: String,
    val info: String,
    val seller: String,
    val price: Int,
    val locate: String,
    val heart: Int,
    val chat: Int,
    var myFavorite: Boolean = false,
) : Parcelable