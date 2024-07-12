package com.example.grey.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.grey.data.remote.response.Repository
import com.example.grey.data.remote.response.SearchRepositoryResponse
import com.example.grey.data.remote.response.SearchUser
import com.example.grey.data.remote.response.SearchUserResponse

abstract class UserPagingSource : PagingSource<Int, SearchUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchUser> {
        try {
            // Start loading at page 1, if undefined.
            val thisPageNumber = params.key ?: 1
            val result = loadPage(thisPageNumber, params.loadSize)

            val previousPageNUmber: Int? = if (thisPageNumber == 1) {
                null
            } else {
                thisPageNumber - 1
            }
            val nextPageNUmber: Int? = if (thisPageNumber == 20) {
                null
            } else {
                thisPageNumber + 1
            }

            return LoadResult.Page(
                data = result.items,
                prevKey = previousPageNUmber,
                nextKey = nextPageNUmber
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchUser>): Int? = null

    abstract suspend fun loadPage(page: Int, pageSize: Int): SearchUserResponse
}
