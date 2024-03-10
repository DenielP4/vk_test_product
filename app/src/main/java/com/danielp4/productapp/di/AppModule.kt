package com.danielp4.productapp.di

import com.danielp4.productapp.data.remote.ProductApi
import com.danielp4.productapp.repository.ProductRepository
import com.danielp4.productapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpBuilder = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)

    @Singleton
    @Provides
    fun provideProductRepository(
        api: ProductApi
    ) = ProductRepository(api)


    @Singleton
    @Provides
    fun provideProductApi(): ProductApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpBuilder.build())
            .build()
            .create(ProductApi::class.java)
    }

}