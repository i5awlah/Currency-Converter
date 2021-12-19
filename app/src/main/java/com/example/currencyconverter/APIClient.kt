package com.example.currencyconverter

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {

    companion object
    {
        var retrofit: Retrofit? = null
        fun getClient(): Retrofit?
        {
            if( retrofit == null )
            {
                synchronized(APIClient::class.java)
                {
                    retrofit = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/")
                        .build()
                }
            }
            return retrofit

        }
    }
//    private var retrofit: Retrofit? = null
//
//    fun getClient(): Retrofit? {
//        retrofit = Retrofit.Builder()
//            .baseUrl("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        return retrofit
//    }
}