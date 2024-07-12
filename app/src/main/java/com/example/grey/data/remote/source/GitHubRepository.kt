package com.example.grey.data.remote.source

import com.example.grey.data.paging.RepositoryPagingSource
import com.example.grey.data.paging.UserPagingSource
import com.example.grey.data.paging.UserRepositoryPagingSource
import com.example.grey.data.remote.Result
import com.example.grey.data.remote.response.Repository
import com.example.grey.data.remote.response.SearchRepositoryResponse
import com.example.grey.data.remote.response.SearchUser
import com.example.grey.data.remote.response.SearchUserResponse
import com.example.grey.data.remote.response.UserDetailResponse

interface GitHubRepository {

    suspend fun searchUser(query: String): Result<List<SearchUser>>

    suspend fun searchRepository(query: String): Result<List<Repository>>

    fun searchRepositoryPagingSource(query: String): RepositoryPagingSource

    fun searchUserPagingSource(query: String): UserPagingSource

    suspend fun getUserDetails(username: String): Result<UserDetailResponse>

    fun getUserRepositoryPagingSource(userName: String): UserRepositoryPagingSource
}
