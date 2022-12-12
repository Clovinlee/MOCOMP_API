package com.example.chrisuascat

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chrisuascat.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.*

class MainActivity : AppCompatActivity() {

    private lateinit var b : ActivityMainBinding
    private lateinit var db : AppDatabase
    private lateinit var fHome : HomeFragment
    private lateinit var fCart : CartFragment
    private lateinit var rc : Retrofit
    private lateinit var listCategory : ArrayList<String>
    private lateinit var listProducts : List<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        listCategory = arrayListOf()
        listProducts = listOf()
        db = AppDatabase.build(this)
        rc = RetrofitClient.getRetrofit()

        fHome = HomeFragment(this, listCategory,listProducts,rc, db)
        fCart = CartFragment(this, db, rc)
        swapFragment(fHome)

        b.btmNav.setOnItemSelectedListener {
            if(it.itemId == R.id.btmNavProduct){
                swapFragment(fHome)
            }else if(it.itemId == R.id.btmNavCart){
                swapFragment(fCart)
            }
            return@setOnItemSelectedListener true
        }
    }

    fun refreshCart(){
        fCart.initData()
    }

    fun swapFragment(f: Fragment){
        var ft : FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frLayout, f)
        ft.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.optionmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menurefresh){
            fHome.refresh()
        }
        else if(item.itemId == R.id.menusort_asc){
            fHome.sort("asc")
        }
        else if(item.itemId == R.id.menusort_desc){
            fHome.sort("desc")
        }
        return super.onOptionsItemSelected(item)
    }
}