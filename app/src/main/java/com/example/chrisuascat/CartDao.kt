package com.example.chrisuascat

import androidx.room.*

@Dao
interface CartDao {
    @Insert
    suspend fun insertCart(cart: Cart)

    @Update
    suspend fun updateCart(cart: Cart)

    @Delete
    suspend fun deleteCart(cart: Cart)

    @Query("SELECT * FROM carts")
    suspend fun fetchCart():List<Cart>

    @Query("SELECT * FROM carts where id = :id")
    suspend fun getCart(id: String):Cart?

    @Query("SELECT * FROM carts where id_item = :id")
    suspend fun getCartByProduct(id: String):Cart?

    @Query("SELECT * FROM carts ORDER BY id DESC LIMIT 1")
    suspend fun getLastCart() : Cart?
}