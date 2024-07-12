package com.example.grey.ui.user.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.grey.data.remote.Result
import com.example.grey.data.remote.response.Repository
import com.example.grey.data.remote.source.DefaultGitHubRepository
import com.example.grey.ui.navigation.UserRoute
import com.example.grey.ui.utils.asSingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val gitHubRepository: DefaultGitHubRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(UserDetailState())
    var repos: Flow<PagingData<Repository>> = flowOf()
    val state = _state.asStateFlow()

    private val username by lazy {
        savedStateHandle.get<String>(UserRoute.UserDetail.ARG_USERNAME)
    }

    init {
        username?.let {
            getUserDetails(it)
            getPagingRepo(it)
        }
    }

    private fun getUserDetails(username: String) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            when(val result = gitHubRepository.getUserDetails(username)) {
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = (result.exception.message ?: "An error occurred please try again").asSingleEvent()
                        )
                    }
                }
                Result.Loading -> Unit
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userDetailResponse = result.data
                        )
                    }
                }
            }
        }
    }

    private fun getPagingRepo(username: String) {
        repos = Pager(
            PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            initialKey = 1
        ) {
            return@Pager gitHubRepository.getUserRepositoryPagingSource(username)
        }
            .flow.map {
                return@map it
            }
            .cachedIn(viewModelScope)
    }
}
