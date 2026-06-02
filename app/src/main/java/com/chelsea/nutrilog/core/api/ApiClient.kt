package com.chelsea.nutrilog.core.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit

class TokenInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("token", null)
        
        val newRequest = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }
        
        return chain.proceed(newRequest)
    }
}

object ApiClient {
    private const val BASE_URL = "http://192.168.1.1:8000/api/" // Ganti dengan IP server Anda
    
    fun createRetrofit(context: Context): Retrofit {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()
        
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(TokenInterceptor(context))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    fun getApiService(context: Context): ApiService {
        return createRetrofit(context).create(ApiService::class.java)
    }
}
