package com.example.grey.ui.repository

import com.example.grey.data.remote.response.Repository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RepositoryState(
    val searchQuery: String? = null,
    val isLoading: Boolean = false,
    val repos: ImmutableList<Repository> = persistentListOf()
)
