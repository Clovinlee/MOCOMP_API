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
import java.text.DecimalFormat

class RVAdapterProducts(private val activity: MainActivity,
                        private val listProducts: List<Product>,
                        private val layout: Int,
                        private val db : AppDatabase) : RecyclerView.Adapter<RVAdapterProducts.CustomViewHolder>(){

    private val coroutine = CoroutineScope(Dispatchers.IO)

    class CustomViewHolder(var view: View) : RecyclerView.ViewHolder(view)
    {
        var imgProduct : ImageView = view.findViewById(R.id.imgProduct)
        var txtTitle : TextView = view.findViewById(R.id.txtTitleProduct)
        var txtDescription : TextView = view.findViewById(R.id.txtDescriptionProduct)
        var txtPrice : TextView = view.findViewById(R.id.txtPriceProduct)
        var btnAddCart : ImageButton = view.findViewById(R.id.btnAddCart)
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
        val item = listProducts[position]

        var decim : DecimalFormat = DecimalFormat("#,###.##")

        holder.txtTitle.text = item.title
        holder.txtDescription.text = item.description
        holder.txtPrice.text = "Rp "+decim.format(item.price.toFloat()).toString()

        holder.btnAddCart.setOnClickListener {
            var c : Cart? = null
            coroutine.launch {
                c = db.cartDao.getCartByProduct(item.id.toString())
                if(c == null){
                    var lastCart : Cart? = db.cartDao.getLastCart()
                    var idCart : Int = 0;
                    if(lastCart != null){
                        idCart = lastCart.id.toInt()+1
                    }
                    db.cartDao.insertCart(Cart(idCart, item.id, 1, item.price.toFloat()))
                }
                else if(c != null){
                    c!!.qty+=1
                    c!!.subtotal += item.price.toFloat()
                    db.cartDao.updateCart(c!!)
                }
                activity.refreshCart()
                activity.runOnUiThread{
                    Toast.makeText(activity, "Items successfully added to cart", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return listProducts.size
    }
}