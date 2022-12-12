package com.example.chrisuascat

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chrisuascat.databinding.FragmentCartBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class CartFragment(var activity: MainActivity, var db : AppDatabase, var rc : Retrofit) : Fragment() {

    private lateinit var b : FragmentCartBinding
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var ctx : Context
    private lateinit var adapterCart : RVAdapterCart
    private lateinit var listCart : List<Cart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listCart = arrayListOf()
        ctx = view.context
        initData()

    }

    fun initData(){
        coroutine.launch {
            listCart = db.cartDao.fetchCart()

            activity.runOnUiThread {
                initRV()
            }
        }
    }

    fun initRV(){
        adapterCart = RVAdapterCart(activity,listCart,R.layout.rv_layout_carts, db, rc)
        b.rvCart.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        b.rvCart.adapter = adapterCart
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentCartBinding.inflate(inflater, container, false)
        return b.root
    }
}