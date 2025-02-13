package com.demo.mye_kart.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.mye_kart.R
import com.demo.mye_kart.adapter.ProductAdapter
import com.demo.mye_kart.databinding.ActivityProductsBinding
import com.demo.mye_kart.models.ModelProducts
import com.demo.mye_kart.utils.APIClient
import com.demo.mye_kart.utils.APIInterface
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsActivity : AppCompatActivity() {
    lateinit var binding: ActivityProductsBinding
    private lateinit var mRecyclerView: RecyclerView
    var arrayList: ArrayList<ModelProducts.ModelProductsItem> = ArrayList()
    lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (binding.swipeProducts.visibility == View.VISIBLE) binding.swipeProducts.visibility =
            View.GONE
        if (binding.rlLoad.visibility == View.GONE) binding.rlLoad.visibility = View.VISIBLE
        android.os.Handler().postDelayed({
            initiate()
        }, 3000)

        mRecyclerView = findViewById(R.id.recyclerView_products)
        adapter = ProductAdapter(arrayList, this)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = adapter

        binding.swipeProducts.setOnRefreshListener {
            if (binding.swipeProducts.visibility == View.VISIBLE) binding.swipeProducts.visibility =
                View.GONE
            if (binding.rlLoad.visibility == View.GONE) binding.rlLoad.visibility = View.VISIBLE
            android.os.Handler().postDelayed({
                initiate()
            }, 3000)

        }

        binding.etSearch.doAfterTextChanged { text ->
            val str = text.toString().trim()
            if (str.isNotEmpty() && str.length > 2) {

                val copyList: List<ModelProducts.ModelProductsItem> = ArrayList(arrayList)
                val arrayList1: ArrayList<ModelProducts.ModelProductsItem> = ArrayList()
                if (arrayList1.size > 0) arrayList1.clear()
                for (model in copyList) {
                    if (model.title.contains(str)) {
                        arrayList1.add(model)
                    }
                }
                if (arrayList1.size > 0) {
                    mRecyclerView.removeAllViewsInLayout()
                    adapter = ProductAdapter(arrayList1, this)
                    mRecyclerView.layoutManager = LinearLayoutManager(this)
                    mRecyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.swipeProducts.visibility = View.VISIBLE
                    binding.rlLoad.visibility = View.GONE
                } else {
                    binding.swipeProducts.visibility = View.GONE
                    binding.rlLoad.visibility = View.VISIBLE
                }
            } else {
                if (arrayList.size > 0) arrayList.clear()
                initiate()
            }
        }

    }

    private fun initiate() {
        if (binding.swipeProducts.isRefreshing) binding.swipeProducts.isRefreshing = false
        if (arrayList.size > 0) arrayList.clear()

        val apiInterface = APIClient().getClient().create(APIInterface::class.java)
        val call = apiInterface.getProducts()
        call.enqueue(object : Callback<ModelProducts> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ModelProducts>,
                response: Response<ModelProducts>
            ) {
                if (response.code() == 200) {
                    if (response.isSuccessful) {
                        val resource = response.body()
                        if (resource != null) {
                            if (resource.isNotEmpty()) {
                                for (element in resource) {
                                    arrayList.add(element)
                                }
                                if (arrayList.size > 0) {
                                    mRecyclerView.removeAllViewsInLayout()
                                    adapter = ProductAdapter(arrayList, this@ProductsActivity)
                                    mRecyclerView.layoutManager = LinearLayoutManager(this@ProductsActivity)
                                    mRecyclerView.adapter = adapter
                                    adapter.notifyDataSetChanged()
                                    binding.swipeProducts.visibility = View.VISIBLE
                                    binding.rlLoad.visibility = View.GONE
                                }

                            } else {
                                binding.swipeProducts.visibility = View.GONE
                                binding.rlLoad.visibility = View.VISIBLE
                            }
                        } else {
                            binding.swipeProducts.visibility = View.GONE
                            binding.rlLoad.visibility = View.VISIBLE
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ModelProducts>, t: Throwable) {
                println(t)
                Snackbar.make(
                    binding.root,
                    "Something went wrong", Snackbar.LENGTH_SHORT
                ).show()
            }

        })
    }
}