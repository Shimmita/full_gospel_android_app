package com.shimmita.full_gospel.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shimmita.full_gospel.R

@Preview(showBackground = true)
@Composable
fun NotificationView() {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "notification icon",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))

            Text(
                text = "Recent Announcement",
            )
        }
        Spacer(modifier = Modifier.padding(bottom = 20.dp))
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            CardContent()
            CardContent()
            CardContent()
            CardContent()
            CardContent()
            CardContent()
            CardContent()
        }

    }
}


@Composable
fun CardContent() {
    Card(modifier = Modifier.padding(5.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.apostle),
                contentDescription = "poster-imager",
                modifier = Modifier
                    .size(70.dp)
                    .weight(.2f)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Crop,

                )


            Column(
                modifier = Modifier.weight(.8f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Harvey Stewart".uppercase(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = "~ church apostle ~", style = MaterialTheme.typography.titleSmall)
                HorizontalDivider(modifier = Modifier.padding(10.dp))

                Text(
                    text = "We have now fully integrated our services through digital transformation program. You can now actively access all of our services online.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = {}) {
                        Text(text = "Mark Read", style = MaterialTheme.typography.bodySmall)
                    }
                    Text(
                        text = "27/02/2025",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

            }
        }
    }
}




