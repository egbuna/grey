package com.example.grey.data.remote.source

import com.example.grey.data.paging.RepositoryPagingSource
import com.example.grey.data.paging.UserPagingSource
import com.example.grey.data.paging.UserRepositoryPagingSource
import com.example.grey.data.remote.Result
import com.example.grey.data.remote.repo.GitHubRemoteDataSource
import com.example.grey.data.remote.response.Repository
import com.example.grey.data.remote.response.SearchRepositoryResponse
import com.example.grey.data.remote.response.SearchUser
import com.example.grey.data.remote.response.SearchUserResponse
import com.example.grey.data.remote.response.UserDetailResponse
import com.example.grey.data.remote.response.UserRepositoryResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultGitHubRepository @Inject constructor(
    private val gitHubRemoteDataSource: GitHubRemoteDataSource,
) : GitHubRepository {
    override suspend fun searchUser(query: String): Result<List<SearchUser>> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val result = gitHubRemoteDataSource.searchUsers(query, 1, 100).items
                Result.Success(result)
            }
            catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun searchRepository(query: String): Result<List<Repository>> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val result = gitHubRemoteDataSource.searchRepositories(query, perPage = 1, page = 100).items
                Result.Success(result)
            }
            catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override fun searchRepositoryPagingSource(query: String): RepositoryPagingSource = object : RepositoryPagingSource() {
        override suspend fun loadPage(page: Int, pageSize: Int): SearchRepositoryResponse = gitHubRemoteDataSource.searchRepositories(query, pageSize, page)
    }

    override fun searchUserPagingSource(query: String): UserPagingSource =
        object : UserPagingSource() {
            override suspend fun loadPage(page: Int, pageSize: Int): SearchUserResponse = gitHubRemoteDataSource.searchUsers(query, pageSize, page)

        }

    override suspend fun getUserDetails(username: String): Result<UserDetailResponse> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val result = gitHubRemoteDataSource.getUserDetails(username)
                Result.Success(result)
            }
            catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override fun getUserRepositoryPagingSource(userName: String): UserRepositoryPagingSource = object : UserRepositoryPagingSource() {
        override suspend fun loadPage(page: Int, pageSize: Int): UserRepositoryResponse = gitHubRemoteDataSource.getUserRepositories(userName, pageSize, page)
    }

}
