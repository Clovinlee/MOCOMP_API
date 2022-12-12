package com.example.chrisuascat

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "carts")
class Cart(
    @PrimaryKey
    var id : Int,
    var id_item : Int,
    var qty : Int,
    var subtotal : Float
) : Parcelable {
}