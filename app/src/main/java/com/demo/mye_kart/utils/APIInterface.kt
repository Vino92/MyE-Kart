package com.demo.mye_kart.utils

import com.demo.mye_kart.models.ModelProducts
import retrofit2.Call
import retrofit2.http.GET

interface APIInterface {

    @GET("/products")
    fun getProducts(): Call<ModelProducts>

}