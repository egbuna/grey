package com.example.grey.ui.user.detail

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.grey.R
import com.example.grey.data.remote.response.Repository
import com.example.grey.data.remote.response.UserDetailResponse
import com.example.grey.ui.component.EmptyState
import com.example.grey.ui.component.observeEvent
import com.example.grey.ui.navigation.LocalNavigation
import com.example.grey.ui.navigation.Navigation
import com.example.grey.ui.repository.LoadingDialogView
import com.example.grey.ui.theme.BorderGrey
import com.example.grey.ui.theme.DividerColor
import com.example.grey.ui.theme.GreyTypography
import com.example.grey.ui.theme.TechGreenColor
import com.example.grey.ui.theme.TextGrey
import com.example.grey.ui.theme.UsernameColor
import kotlinx.coroutines.flow.flowOf

@Composable
fun UserDetailPage() {
    val vm = hiltViewModel<UserDetailViewModel>()
    val state by vm.state.collectAsState()
    val repositories = vm.repos.collectAsLazyPagingItems()
    val navigator = LocalNavigation.current

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    observeEvent(event = state.errorMessage) {
        errorMessage = it
    }

    UserDetailScreen(state = state,
        repos = repositories,
        errorMessage = errorMessage,
        navigateUp = {
            navigator.navigate(Navigation.Back())
        }
    )
}

@Composable
fun UserDetailScreen(
    state: UserDetailState,
    repos: LazyPagingItems<Repository>,
    errorMessage: String?,
    navigateUp: () -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .padding(16.dp)
            .systemBarsPadding(),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Users", style = GreyTypography.tabB)
            }
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState) {
                Box(
                    Modifier.padding(10.dp)
                ) {
                    Snackbar(
                        shape = RoundedCornerShape(8.dp),
                        containerColor = Color.Red
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Warning,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.White,
                                    contentDescription = null
                                )
                                Text(
                                    text = errorMessage.orEmpty(),
                                    style = GreyTypography.h3,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    modifier = Modifier.size(24.dp),
                                    onClick = {
                                        it.dismiss()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        tint = Color.White,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {

            Spacer(modifier = Modifier.height(18.dp))

            HeaderSection(user = state.userDetailResponse, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(20.dp))

            Column {
                Row(
                    modifier = Modifier
                        .onGloballyPositioned {
                            size = it.size
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Repositories", style = GreyTypography.tab)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "200",
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(color = DividerColor, shape = RoundedCornerShape(10.dp))
                            .padding(vertical = 2.dp, horizontal = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                color = Color.Black,
                modifier = Modifier.width((size.width / 2.8).dp)
            )

            Divider(
                color = DividerColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (repos.itemCount == 0 && repos.loadState.refresh != LoadState.Loading) {
                EmptyState(
                    icon = R.drawable.empty_repo,
                    label = "This user doesn't have repositories yet, come back later."
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (repos.loadState.refresh == LoadState.Loading) {
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

                if (repos.loadState.refresh is LoadState.Error) {
                    item {
                        EmptyState(
                            label = repos.loadState.refresh.toString()
                        )
                    }
                }

                items(repos.itemCount) {
                    val item = repos[it]
                    item?.let {
                        UserRepoItem(repo = item)
                    }

                }

                if (repos.loadState.append == LoadState.Loading) {
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

                if (repos.loadState.append is LoadState.Error) {
                    item {
                        EmptyState(
                            label = repos.loadState.refresh.toString()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

        }
    }
}

@Composable
fun HeaderSection(modifier: Modifier = Modifier, user: UserDetailResponse?) {
    Column(modifier = modifier) {
        Row {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user?.avatarUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.user),
                error = painterResource(id = R.drawable.user),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(45.dp)
                    .fillMaxWidth()
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = user?.login.orEmpty(), color = Color.Black,
                    style = GreyTypography.h3
                )

                user?.name?.let {
                    Text(
                        text = it, color = Color.Black,
                        style = GreyTypography.mediumB
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user?.bio.orEmpty(),
            style = GreyTypography.mediumS
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            user?.location?.let {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = it, color = TextGrey,
                    style = GreyTypography.mediumSS
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            user?.blog?.let {
                Icon(
                    painter = painterResource(id = R.drawable.link_icon),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = it, color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = GreyTypography.mediumSS
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.people), contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${user?.followers} followers", color = TextGrey,
                style = GreyTypography.mediumSS
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = ".")
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "${user?.following} following", color = TextGrey,
                style = GreyTypography.mediumSS
            )
        }
    }
}

@Composable
fun UserRepoItem(modifier: Modifier = Modifier, repo: Repository) {

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
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
        ) {
            append(repo.name)
        }
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = BorderGrey
        ),
        shape = RoundedCornerShape(2.dp)
    ) {
        Column(Modifier.padding(vertical = 13.dp, horizontal = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = title,
                        maxLines = 2,
                        style = GreyTypography.normalSm,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.width(9.dp))

                    Text(
                        text = "Public", modifier = Modifier
                            .border(
                                width = 0.5.dp,
                                color = BorderGrey,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(vertical = 3.dp, horizontal = 8.dp),
                        style = GreyTypography.normalSm

                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.repo_star),
                        contentDescription = null
                    )
                    Text(text = "10")

                    Spacer(modifier = Modifier.width(20.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                color = TechGreenColor,
                                shape = CircleShape
                            )
                            .size(15.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(text = repo.language.orEmpty())
                }
            }

            Spacer(modifier = Modifier.height(11.dp))

            Text(
                text = repo.description.orEmpty(),
                style = GreyTypography.normalXSm
            )

            Spacer(modifier = Modifier.height(7.dp))

            Row {
                Text(
                    text = "Forked from discordify", style = GreyTypography.normalXSm,
                    color = TextGrey
                )

                Spacer(modifier = Modifier.width(17.dp))

                Text(
                    text = repo.updatedAt, style = GreyTypography.normalXSm,
                    color = TextGrey
                )
            }
        }
    }
}

@Preview
@Composable
private fun UserRepoItemPreview() {
    UserRepoItem(
        repo = Repository(
            id = 1,
            name = "Chukwudi",
            description = null,
            updatedAt = "knr",
            stars = 10,
            owner = Repository.Owner(
                login = "egbuna",
                id = 1,
                avatarUrl = ""
            ),
            language = "",
            topics = emptyList()
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun HeaderSectionPreview() {
    HeaderSection(user = UserDetailResponse())
}

@Preview(showBackground = true, device = "id:pixel_5", showSystemUi = true)
@Composable
private fun UserDetailScreenPreview() {
    UserDetailScreen(
        state = UserDetailState(),
        repos = flowOf(
            PagingData.empty<Repository>()
        ).collectAsLazyPagingItems(),
        errorMessage = null,
        navigateUp = {}
    )
}
