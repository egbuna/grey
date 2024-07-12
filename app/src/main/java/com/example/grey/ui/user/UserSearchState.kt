package com.example.grey.ui.user

import androidx.paging.PagingData
import com.example.grey.data.remote.response.SearchUser
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UserSearchState(
    val searchQuery: String? = null,
    val isLoading: Boolean = false,
    val repos: ImmutableList<SearchUser> = persistentListOf(),
    val userPagingList: PagingData<SearchUser> = PagingData.empty()
)
