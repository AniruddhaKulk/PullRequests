package com.ani.kulk.pullrequests.di

import com.ani.kulk.pullrequests.utils.NetworkService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {

    fun provideHttpClient(
        interceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(interceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    fun provideInterceptor(): Interceptor {
        return Interceptor.invoke {
            val url =
                it.request().url.newBuilder().build()
            val request = it.request()
                .newBuilder()
                .url(url)
                .build()
            it.proceed(request)
        }
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(client)
            .baseUrl("https://api.github.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    single { provideRetrofit(get()) }
    single { provideHttpClient(get(), get()) }
    single { provideInterceptor() }
    single { provideHttpLoggingInterceptor() }
    single { get<Retrofit>().create(NetworkService::class.java) }
}