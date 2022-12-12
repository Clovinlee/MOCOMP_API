package com.example.chrisuascat

import com.google.gson.annotations.SerializedName

data class ProductResults(
    var products : List<Product>,
    var limit: Int,
    var skip: Int,
    var total: Int
)
