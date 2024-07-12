package com.example.grey.data.remote.repo

import com.example.grey.data.remote.response.Repository
import com.example.grey.data.remote.response.SearchRepositoryResponse
import com.example.grey.data.remote.response.SearchUserResponse
import com.example.grey.data.remote.response.UserDetailResponse
import com.example.grey.data.remote.response.UserRepositoryResponse
import retrofit2.http.Query

interface GitHubRemoteDataSource {

    suspend fun searchUsers(query: String, perPage: Int?, page: Int?): SearchUserResponse

    suspend fun searchRepositories(query: String, perPage: Int?, page: Int?): SearchRepositoryResponse

    suspend fun getUserDetails(username: String): UserDetailResponse

    suspend fun getUserRepositories(username: String, perPage: Int?, page: Int?): UserRepositoryResponse
}
