package com.example.grey.data.remote.repo

import com.example.grey.data.remote.ApiService
import com.example.grey.data.remote.response.SearchRepositoryResponse
import com.example.grey.data.remote.response.SearchUserResponse
import com.example.grey.data.remote.response.UserDetailResponse
import com.example.grey.data.remote.response.UserRepositoryResponse

class DefaultGitHubRemoteDataSource internal constructor(
    private val apiService: ApiService
) : GitHubRemoteDataSource {

    override suspend fun searchUsers(query: String, perPage: Int?, page: Int?): SearchUserResponse = apiService.searchUsers(query)
    override suspend fun searchRepositories(query: String, perPage: Int?, page: Int?): SearchRepositoryResponse = apiService.searchRepositories(query, perPage = perPage, page = page)
    override suspend fun getUserDetails(username: String): UserDetailResponse = apiService.getUser(username)
    override suspend fun getUserRepositories(username: String, perPage: Int?, page: Int?): UserRepositoryResponse {
        val results =  apiService.getUserRepositories(username, perPage, page)
        val response = UserRepositoryResponse()
        response.items = results
        return response
    }
}
