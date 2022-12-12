package com.example.chrisuascat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chrisuascat.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class HomeFragment(var activity: MainActivity,
                   var listCategory : ArrayList<String>,
                   var listProducts : List<Product>,
                   var rc : Retrofit,
                   var db : AppDatabase) : Fragment() {

    private lateinit var b : FragmentHomeBinding
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var ctx : Context
    private lateinit var adapterProducts : RVAdapterProducts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ctx = view.context

        initData();

        b.btnFilter.setOnClickListener {
            var q : String = b.txtProductName.text.toString()

            listProducts = listProducts.filter {
                it -> it.title == q
            }
            initRV()
        }

        b.btnSearchCategory.setOnClickListener {
            var category : String = b.cmbCategory.selectedItem.toString()
            var rc_productCategory = rc.create(ProductsAPI::class.java)
            rc_productCategory.getProductCategories(category).enqueue(object:
                Callback<ProductResults?> {
                override fun onResponse(
                    call: Call<ProductResults?>,
                    response: Response<ProductResults?>
                ) {

                    var responseBody = response.body()

                    if(responseBody == null){
                        Log.d("CCD","Return null")
                        return
                    }

                    listProducts = responseBody.products
                    initRV()
                }

                override fun onFailure(call: Call<ProductResults?>, t: Throwable) {
                    Log.d("CCD","error product category")
                }
            })
        }

        b.btnSearchName.setOnClickListener {
            var q : String = b.txtProductName.text.toString()
            var rc_searchProduct : ProductsAPI = rc.create(ProductsAPI::class.java)
            rc_searchProduct.searchProducts(q).enqueue(object : Callback<ProductResults?> {
                override fun onResponse(
                    call: Call<ProductResults?>,
                    response: Response<ProductResults?>
                ) {
                    var responseBody = response.body()
                    if(responseBody == null){
                        Log.d("CCD","Error null")
                        return
                    }

                    listProducts = responseBody.products

                    initRV()
                }

                override fun onFailure(call: Call<ProductResults?>, t: Throwable) {
                    Log.d("CCD","ERRor search product")
                }
            })
        }
    }

    fun refresh(){
        b.txtProductName.setText("")
        initData()
    }

    fun sort(mode : String){
        if(mode == "asc"){
            listProducts = listProducts.sortedBy {
                it.price
            }
        }else{
            listProducts = listProducts.sortedByDescending {
                it.price
            }
        }
        initRV()
    }

    fun initRV(){
        adapterProducts = RVAdapterProducts(activity,listProducts,R.layout.rv_layout_products, db)
        b.rvProducts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        b.rvProducts.adapter = adapterProducts
    }

    fun initData(){
        var rc_categories : CategoryAPI = rc.create(CategoryAPI::class.java)
        rc_categories.getAllCategories().enqueue(object: Callback<List<String>?>{
            override fun onResponse(call: Call<List<String>?>, response: Response<List<String>?>) {
                val responseBody = response.body()

                if(responseBody == null){
                    Log.d("CCD","ERROR CATEGORY NULL")
                    return
                }

                for(c in responseBody){
                    var c_capitalize = c
                    c_capitalize = c_capitalize.replaceFirstChar { c -> c.uppercase() }
                    listCategory.add(c_capitalize)
                }

                val arrAdapter : ArrayAdapter<String> = ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_dropdown_item, listCategory)
                b.cmbCategory.adapter = arrAdapter
            }

            override fun onFailure(call: Call<List<String>?>, t: Throwable) {
                Log.d("CCD","ERROR GETTING CATEGORY DATA")
                Log.d("CCD",t.message.toString())
            }
        })

        var rc_products : ProductsAPI = rc.create(ProductsAPI::class.java)
        rc_products.getAllProducts().enqueue(object: Callback<ProductResults?>{
            override fun onResponse(
                call: Call<ProductResults?>,
                response: Response<ProductResults?>
            ) {
                val responseBody = response.body()

                if(responseBody == null){
                    Log.d("CCD","Response Null")
                    return
                }

                listProducts = responseBody.products

                initRV()
            }

            override fun onFailure(call: Call<ProductResults?>, t: Throwable) {
                Log.d("CCD","ERROR GETTING PRODUCT DATA")
                Log.d("CCD",t.message.toString())
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentHomeBinding.inflate(inflater, container, false)
        return b.root
    }
}