package com.shimmita.full_gospel.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shimmita.full_gospel.R


data class DrawerActivities(
    val activityName: String,
    val activityIcon: Int,

    )


val activityList = listOf(
    DrawerActivities(
        activityName = "Today's Prayer",
        activityIcon = R.drawable.baseline_self_improvement_24
    ),

    DrawerActivities(
        activityName = "Weekly Verse",
        activityIcon = R.drawable.baseline_menu_book_24
    ),
    DrawerActivities(
        activityName = "Testimony",
        activityIcon = R.drawable.baseline_emoji_people_24
    ),

    DrawerActivities(
        activityName = "Announcement",
        activityIcon = R.drawable.baseline_notification_add_24
    ),

    DrawerActivities(
        activityName = "Add Event",
        activityIcon = R.drawable.baseline_event_available_24
    ),

    DrawerActivities(
        activityName = "Nature Talent",
        activityIcon = R.drawable.baseline_queue_music_24
    ),

    DrawerActivities(
        activityName = "Last Sunday Remarks",
        activityIcon = R.drawable.baseline_church_24
    ),

    )

@Composable
fun DrawerScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center

    ) {


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(start = 5.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.apostle),
                contentDescription = "image",
                Modifier
                    .clip(CircleShape)
                    .size(100.dp)
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(start = 20.dp)) {
                Text(
                    text = "David Uche",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Church Apostle", modifier = Modifier.padding(top = 5.dp))
                Text(text = "+254123456789", modifier = Modifier.padding(top = 5.dp))
                Text(
                    text = "Daviduche@gmail.com",
                    modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)
                )
            }
        }

        OutlinedButton(
            onClick = {},
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        ) {
            Text(text = "update profile")
        }

        Spacer(modifier = Modifier.padding(30.dp))

        Text(
            text = "Post An Activity",
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally), fontWeight = FontWeight.Bold
        )


        Spacer(modifier = Modifier.padding(10.dp))


        activityList.forEach { it ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Icon(
                    painter = painterResource(it.activityIcon),
                    contentDescription = it.activityName,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(30.dp), tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = it.activityName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
        }

    }

}





