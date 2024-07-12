package com.example.grey.data.di

import android.content.Context
import com.example.grey.BuildConfig
import com.example.grey.data.remote.ApiService
import com.example.grey.data.remote.repo.DefaultGitHubRemoteDataSource
import com.example.grey.data.remote.repo.GitHubRemoteDataSource
import com.example.grey.data.remote.source.DefaultGitHubRepository
import com.example.grey.data.remote.source.GitHubRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [DataModuleBinds::class])
object DataModule {
    private const val CONNECT_READ_WRITE_TIMEOUT = 60000L

    @Singleton
    @Provides
    fun provideApiOkHttpClient(
        okHttpClientCache: Cache
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
            .apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
        return OkHttpClient.Builder()
            .cache(okHttpClientCache)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(CONNECT_READ_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(CONNECT_READ_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(CONNECT_READ_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitApi(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit {
        val baseUrl: String = "https://api.github.com"

        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(
        retrofit: Retrofit
    ): ApiService {
        return retrofit
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClientCache(@ApplicationContext context: Context): Cache = Cache(context.cacheDir, OK_HTTP_CLIENT_CACHE_SIZE)


    @Singleton
    @Provides
    fun provideRemoteDataSource(
        apiService: ApiService,
    ): GitHubRemoteDataSource {
        return DefaultGitHubRemoteDataSource(apiService)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

}

@Module
@DisableInstallInCheck
abstract class DataModuleBinds {

    @Singleton
    @Binds
    abstract fun bindGithubRepository(repo: DefaultGitHubRepository): GitHubRepository
}

private const val OK_HTTP_CLIENT_CACHE_SIZE = 20L * 1024L * 1024L // 20 MB
