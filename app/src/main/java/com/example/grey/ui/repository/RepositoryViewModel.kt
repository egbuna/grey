package com.example.grey.ui.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.grey.data.remote.Result
import com.example.grey.data.remote.response.Repository
import com.example.grey.data.remote.source.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)
    private val _state = MutableStateFlow(RepositoryState())
    var repos: Flow<PagingData<Repository>> = flowOf()

    val state = combine(_searchQuery, _state) { query, state ->
        state.copy(
            searchQuery = query,
            isLoading = false,
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, RepositoryState())

    private fun getPagingRepo(query: String) {
        repos = Pager(
            PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            initialKey = 1
        ) {
            return@Pager gitHubRepository.searchRepositoryPagingSource(query)
        }
            .flow.map {
                return@map it
            }
            .cachedIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _searchQuery.debounce(300)
                .onEach {
                    if (it?.isNotBlank() == true)
                        getPagingRepo(_searchQuery.value.orEmpty())
                }.launchIn(viewModelScope)
        }
    }

    fun search() {
        if (_searchQuery.value.isNullOrBlank().not())
            getPagingRepo(_searchQuery.value.orEmpty())
    }

}
