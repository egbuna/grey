package com.example.grey.data.remote

import com.example.grey.data.remote.response.Repository
import com.example.grey.data.remote.response.SearchRepositoryResponse
import com.example.grey.data.remote.response.SearchUserResponse
import com.example.grey.data.remote.response.UserDetailResponse
import com.example.grey.data.remote.response.UserRepositoryResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Accept: $HEADER_BACKEND_VERSION")
    suspend fun searchUsers(@Query("q") searchQuery: String,
                            @Query("per_page") perPage: String? = null,
                            @Query("page") page: String? = null
                            ): SearchUserResponse

    @GET("search/repositories")
    @Headers("Accept: $HEADER_BACKEND_VERSION")
    suspend fun searchRepositories(@Query("q") searchQuery: String,
                                   @Query("per_page") perPage: Int? = null,
                                   @Query("page") page: Int? = null) : SearchRepositoryResponse

    @GET("users/{username}/repos")
    @Headers("Accept: $HEADER_BACKEND_VERSION")
    suspend fun userRepos(
        @Path("username") username: String
    )

    @GET("users/{username}")
    @Headers("Accept: $HEADER_BACKEND_VERSION")
    suspend fun getUser(
        @Path("username") username: String
    ): UserDetailResponse

    @GET("users/{username}/repos")
    @Headers("Accept: $HEADER_BACKEND_VERSION")
    suspend fun getUserRepositories(@Path("username")
                                        username: String,
                                    @Query("per_page") perPage: Int? = null,
                                    @Query("page") page: Int? = null): List<Repository>

}

const val HEADER_BACKEND_VERSION = "application/vnd.github+json"
