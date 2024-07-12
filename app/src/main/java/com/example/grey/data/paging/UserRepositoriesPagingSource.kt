package com.example.grey.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.grey.data.remote.response.Repository
import com.example.grey.data.remote.response.UserRepositoryResponse
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

abstract class UserRepositoryPagingSource : PagingSource<Int, Repository>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
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
                data = result.items.map {
                    val today = LocalDate.now()
                    val updatedAt = LocalDate.parse(it.updatedAt, DateTimeFormatter.ISO_DATE_TIME)
                    val daysPeriod = Period.between(updatedAt, today)
                    val updatedValue = if (daysPeriod.days < 10) {
                        "${daysPeriod.days} day(s) ago"
                    } else updatedAt
                    it.copy(
                        updatedAt = "Updated $updatedValue"
                    )
                },
                prevKey = previousPageNUmber,
                nextKey = nextPageNUmber
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? = null

    abstract suspend fun loadPage(page: Int, pageSize: Int): UserRepositoryResponse
}
