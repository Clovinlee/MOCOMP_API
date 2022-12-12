package com.example.chrisuascat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.*
import java.text.DecimalFormat

class RVAdapterCart(private val activity: MainActivity,
                    private val listCarts : List<Cart>,
                    private val layout: Int,
                    private val db : AppDatabase,
                    private val rc : Retrofit) : RecyclerView.Adapter<RVAdapterCart.CustomViewHolder>(){

    private var coroutine = CoroutineScope(Dispatchers.IO)

    class CustomViewHolder(var view: View) : RecyclerView.ViewHolder(view)
    {
        var txtTitle : TextView = view.findViewById(R.id.txtTitleProduct2)
        var txtDescription : TextView = view.findViewById(R.id.txtDescriptionProduct2)
        var txtPrice : TextView = view.findViewById(R.id.txtPriceProduct2)
        var txtQty : TextView = view.findViewById(R.id.txtQty)
        var txtSubtotal : TextView = view.findViewById(R.id.txtSubtotal)

        var btnAdd : Button = view.findViewById(R.id.btnAdd)
        var btnMinus : Button = view.findViewById(R.id.btnMinus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        var itemView = LayoutInflater.from(parent.context)
        return CustomViewHolder(
            itemView.inflate(
                layout, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = listCarts[position]
        var decim : DecimalFormat = DecimalFormat("#,###.##")

        var rc_product : ProductsAPI = rc.create(ProductsAPI::class.java)
        rc_product.getAProduct(item.id_item.toString()).enqueue(object: Callback<Product?> {
            override fun onResponse(call: Call<Product?>, response: Response<Product?>) {
                var responseBody = response.body()
                if(responseBody == null){
                    Log.d("CCD","Get item product null")
                    return
                }

                Log.d("CCD",responseBody.title)

                holder.txtTitle.text = responseBody.title
                holder.txtPrice.text = "Rp"+decim.format(responseBody.price).toString()
                holder.txtDescription.text = responseBody.description
                holder.txtQty.text = item.qty.toString()
                holder.txtSubtotal.text = "Rp"+decim.format(item.subtotal.toFloat()).toString()

                holder.btnAdd.setOnClickListener {
                    var c :Cart = item
                    c.qty++
                    coroutine.launch {
                        db.cartDao.updateCart(c)
                        activity.refreshCart()
                    }
                }

                holder.btnMinus.setOnClickListener {
                    var c:Cart = item
                    c.qty--
                    coroutine.launch {
                        if(c.qty <= 0){
                                db.cartDao.deleteCart(c)
                        }else{
                            db.cartDao.updateCart(c)
                        }
                        activity.refreshCart()
                    }
                }
            }

            override fun onFailure(call: Call<Product?>, t: Throwable) {
                Log.d("CCD","Fail to retrieve item")
                Log.d("CCD",t.message.toString())
            }
        })

    }

    override fun getItemCount(): Int {
        return listCarts.size
    }
}