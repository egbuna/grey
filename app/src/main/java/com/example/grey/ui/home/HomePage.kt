package com.example.grey.ui.home

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.grey.R
import com.example.grey.ui.navigation.LocalNavigation
import com.example.grey.ui.navigation.Navigation
import com.example.grey.ui.navigation.Root
import com.example.grey.ui.theme.GreyTypography
import com.example.grey.ui.theme.LightBlue
import com.example.grey.ui.theme.LightPink

@Composable
fun HomePage() {
    val navigator = LocalNavigation.current

    HomePageScreen(
        onUserClicked = {
            navigator.navigate(Navigation.Forward(Root.User))
        },
        onRepoClicked = {
            navigator.navigate(Navigation.Forward(Root.Repository))
        }
    )
}

@Composable
fun HomePageScreen(
    onUserClicked: () -> Unit,
    onRepoClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .padding(16.dp)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(text = "Home", style = GreyTypography.h1)

            Spacer(modifier = Modifier.height(31.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                HomeItem(
                    modifier = Modifier.weight(0.5f),
                    icon = painterResource(id = R.drawable.user_icon),
                    title = "Users",
                    containerColor = LightBlue,
                    onClick = onUserClicked
                )
                Spacer(modifier = Modifier.width(8.dp))
                HomeItem(
                    modifier = Modifier.weight(0.5f),
                    icon = painterResource(id = R.drawable.ic_repo),
                    title = "Repository",
                    containerColor = LightPink,
                    onClick = onRepoClicked
                )
            }
        }
    }
}

@Composable
fun HomeItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: Painter,
    containerColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable {
            onClick()
        },
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        shape = RoundedCornerShape(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(41.dp)
        ) {
            Image(painter = icon, contentDescription = null)

            Text(text = title, style = GreyTypography.h2)
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
private fun HomeScreenPreview() {
    HomePageScreen(
        onRepoClicked = {},
        onUserClicked = {}
    )
}
