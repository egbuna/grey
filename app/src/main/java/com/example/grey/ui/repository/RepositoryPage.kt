package com.example.grey.ui.repository

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.grey.R
import com.example.grey.data.remote.response.Repository
import com.example.grey.ui.component.EmptyState
import com.example.grey.ui.theme.BorderGrey
import com.example.grey.ui.theme.Grey
import com.example.grey.ui.theme.GreyTypography
import com.example.grey.ui.theme.TagBackground
import com.example.grey.ui.theme.TagTextColor
import com.example.grey.ui.theme.TechGreenColor
import com.example.grey.ui.theme.UsernameColor
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun RepositoryPage() {
    val vm = hiltViewModel<RepositoryViewModel>()
    val state by vm.state.collectAsStateWithLifecycle(initialValue = RepositoryState())
    val pagingRepo = vm.repos.collectAsLazyPagingItems()
    RepositoryScreen(
        state = state,
        pagingRepo = pagingRepo,
        onValueChange = vm::onSearchQueryChanged,
        search = vm::search
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RepositoryScreen(
    state: RepositoryState,
    pagingRepo: LazyPagingItems<Repository>,
    onValueChange: (String) -> Unit,
    search: () -> Unit
) {
    Scaffold(modifier = Modifier
        .systemBarsPadding()
        .padding(16.dp),
        containerColor = Color.White
        ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(text = "Repository", style = GreyTypography.h1)

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(6.dp),
                        color = BorderGrey
                    ),
                value = state.searchQuery.orEmpty(),
                onValueChange = onValueChange,
                placeholder = {
                    Text(text = "Search for repositories...",
                        style = GreyTypography.tab,
                        color = Grey
                    )
                },
                trailingIcon = {
                    Row {
                        Button(
                            onClick = search,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            ),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 26.dp, 8.dp)
                        ) {
                            Text(text = "Search")
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                    }
                },
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.search_small), contentDescription = "search")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.White
                )
            )
            
            Spacer(modifier = Modifier.height(10.dp))

            if (pagingRepo.itemCount == 0 &&
                state.searchQuery?.isNotBlank() == true
                && pagingRepo.loadState.refresh == LoadState.NotLoading(false)) {
                EmptyState(
                    label = "We've searched the ends of the earth, repository not found, please try again"
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (pagingRepo.loadState.refresh == LoadState.Loading
                    && state.searchQuery.isNullOrBlank().not()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {

                            LoadingDialogView()
                        }
                    }
                }
                if (pagingRepo.loadState.refresh is LoadState.Error) {
                    item {
                        EmptyState(
                            label = pagingRepo.loadState.refresh.toString()
                        )
                    }
                }

                items(pagingRepo.itemCount) {
                    val item = pagingRepo[it]
                    item?.let {
                        RepositoryItem(repo = item)
                    }

                }

                if (pagingRepo.loadState.append == LoadState.Loading) {
                    item {
                        if (state.searchQuery?.isBlank()?.not() == true) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {

                                LoadingDialogView()
                            }
                        }
                    }
                }

                if (pagingRepo.loadState.append is LoadState.Error) {
                    item {
                        EmptyState(
                            label = pagingRepo.loadState.refresh.toString()
                        )
                    }
                }
            }

            if (pagingRepo.itemCount == 0 && state.searchQuery.isNullOrBlank()) {
                EmptyState()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RepositoryItem(modifier: Modifier = Modifier, repo: Repository) {
    val title = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = UsernameColor,

            )
        ) {
            append("${repo.owner.login}/")
        }

        withStyle(
            style = SpanStyle(
                color = Color.Black
            )
        ) {
            append(repo.name)
        }
    }
    Card(onClick = {}, modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = BorderGrey
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(repo.owner.avatarUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.user),
                    error = painterResource(id = R.drawable.user),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = GreyTypography.normalSm,
                    modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = painterResource(id = R.drawable.repo_star), contentDescription = null)
                    Text(text = repo.stars.toString())

                    Spacer(modifier = Modifier.width(20.dp))

                    Box(modifier = Modifier
                        .background(
                            color = TechGreenColor,
                            shape = CircleShape
                        )
                        .size(15.dp))

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(text = repo.language.orEmpty(),
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(60.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = repo.description.orEmpty(),
                style = GreyTypography.normalSm)

            Spacer(modifier = Modifier.height(16.dp))

            FlowRow(modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                repo.topics.forEach {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.background(
                            color = TagBackground,
                            shape = RoundedCornerShape(10.dp)
                        )
                    ) {
                        Text(text = it,
                            color = TagTextColor,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
private fun RepositoryScreenPreview() {
    RepositoryScreen(state = RepositoryState(),
        pagingRepo =
        flowOf(PagingData.empty<Repository>()
        ).collectAsLazyPagingItems(),
        onValueChange = {},
        search = {}
        )
}

@Preview
@Composable
private fun RepositoryItemPreview() {
    RepositoryItem(repo = Repository(id = 1L,
        name = "Something",
        description = "jwnfkr",
        updatedAt = "", stars = 10, owner = Repository.Owner(
            login = "sub", id = 1L, avatarUrl = ""
        ), language = "Python"))
}


@Composable
fun LoadingDialogView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(
            color = Color.Red
        )
    }
}
