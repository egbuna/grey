package com.example.grey.ui.user

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.grey.R
import com.example.grey.data.remote.response.SearchUser
import com.example.grey.ui.component.EmptyState
import com.example.grey.ui.navigation.LocalNavigation
import com.example.grey.ui.navigation.Navigation
import com.example.grey.ui.navigation.Root
import com.example.grey.ui.navigation.UserRoute
import com.example.grey.ui.repository.LoadingDialogView
import com.example.grey.ui.repository.RepositoryItem
import com.example.grey.ui.theme.BorderGrey
import com.example.grey.ui.theme.Grey
import com.example.grey.ui.theme.GreyTypography
import com.example.grey.ui.theme.NameColor
import com.example.grey.ui.theme.TextGrey

@Composable
fun UserPage() {
    val vm = hiltViewModel<SearchUserViewModel>()
    val state by vm.state.collectAsStateWithLifecycle(initialValue = UserSearchState())
    val pagingUser = vm.users.collectAsLazyPagingItems()

    val navigator = LocalNavigation.current

    UserScreen(
        state = state,
        pagingUser = pagingUser,
        onValueChange = vm::onSearchQueryChanged,
        search = vm::search,
        onSearchItemClicked = {
            navigator.navigate(
                Navigation.Forward(
                    UserRoute.UserDetail(it)
                )
            )
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserScreen(
    state: UserSearchState,
    pagingUser: LazyPagingItems<SearchUser>,
    onValueChange: (String) -> Unit,
    search: () -> Unit,
    onSearchItemClicked: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .padding(16.dp),
        containerColor = Color.White
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(text = "Users", style = GreyTypography.h1)

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
                    Text(
                        text = "Search for users...",
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
                    Image(
                        painter = painterResource(id = R.drawable.search_small),
                        contentDescription = "search"
                    )
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

            if (pagingUser.itemCount == 0 && state.searchQuery.isNullOrBlank()) {
                EmptyState(
                    label = "Search Github for users..."
                )
            }

            if (pagingUser.itemCount == 0 && state.searchQuery?.isNotBlank() == true
                && pagingUser.loadState.refresh == LoadState.NotLoading(false)
            ) {
                EmptyState(
                    label = "We've searched the ends of the earth, user not found, please try again"
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (pagingUser.loadState.append is LoadState.Error) {
                    item {
                        EmptyState(
                            label = pagingUser.loadState.refresh.toString()
                        )
                    }
                }

                if (pagingUser.loadState.refresh == LoadState.Loading
                    && state.searchQuery.isNullOrBlank().not()
                ) {
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
                items(pagingUser.itemCount) {
                    val item = pagingUser[it]
                    item?.let {
                        UserItem(
                            modifier = Modifier.clickable {
                                onSearchItemClicked(item.login)
                            },
                            searchUser = item
                        )
                    }

                }

                if (pagingUser.loadState.append == LoadState.Loading) {
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

                if (pagingUser.loadState.append is LoadState.Error) {
                    item {
                        EmptyState(
                            label = pagingUser.loadState.refresh.toString()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    searchUser: SearchUser
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
        Column(Modifier.padding(16.dp)) {
            Row {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(searchUser.avatarUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.user),
                    error = painterResource(id = R.drawable.user),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(20.dp)
                        .fillMaxWidth()
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = searchUser.login,
                        style = GreyTypography.tabB,
                        color = NameColor
                    )

                    Text(
                        text = "DynamicWebPage", color = Color.Black,
                        style = GreyTypography.normalXSm
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "This is random bio, which will be replcaed with actual content",
                        style = GreyTypography.mediumS
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        Text(
                            text = "Lagos, Nigeria",
                            color = TextGrey,
                            style = GreyTypography.mediumXS
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "momoko@gmail.com", color = TextGrey,
                            style = GreyTypography.mediumXS
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_5")
@Composable
private fun UserScreenPreview() {
    //UserScreen(goto = {})
}

@Preview
@Composable
private fun UserItemPreview() {
    //UserItem()
}
