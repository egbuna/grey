package com.example.grey.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.grey.R
import com.example.grey.ui.theme.GreyTypography

@Composable
fun EmptyState(modifier: Modifier = Modifier,
               @DrawableRes icon: Int = R.drawable.search_empty_state,
               label: String = "Search Github for repositories, issues and pull requests!") {

    Column(modifier = modifier.fillMaxSize()
        .padding(horizontal = 60.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = icon), contentDescription = null)

        Spacer(modifier = Modifier.height(35.dp))

        Text(text = label,
            textAlign = TextAlign.Center,
            color = Color.Gray, style = GreyTypography.medium)
    }
}

@Preview(showBackground = true, device = "id:pixel_5", showSystemUi = true
)
@Composable
fun EmptyStatePreview(modifier: Modifier = Modifier) {
    EmptyState()
}
