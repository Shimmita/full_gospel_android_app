package com.shimmita.full_gospel.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shimmita.full_gospel.R
import kotlinx.coroutines.launch


data class DrawerActivities(
    val activityName: String,
    val activityIcon: Int,
    val routePath: String

)


val activityList = listOf(
    DrawerActivities(
        activityName = "Post Prayer of the Day",
        activityIcon = R.drawable.baseline_self_improvement_24,
        routePath = "Daily-Prayer",
    ),

    DrawerActivities(
        activityName = "Post Verse of the Week",
        activityIcon = R.drawable.baseline_menu_book_24,
        routePath = "Weekly-Verse"
    ),
    DrawerActivities(
        activityName = "Post and Share Testimony",
        activityIcon = R.drawable.baseline_emoji_people_24,
        routePath = "Share-Testimony"
    ),

    DrawerActivities(
        activityName = "Post About Announcement",
        activityIcon = R.drawable.baseline_notification_add_24,
        routePath = "Announce"
    ),

    DrawerActivities(
        activityName = "Post About Event or Mission",
        activityIcon = R.drawable.baseline_event_available_24,
        routePath = "Event-Mission"
    ),

    DrawerActivities(
        activityName = "Post About Naturing of Talent",
        activityIcon = R.drawable.baseline_queue_music_24,
        routePath = "Nature-Talent"
    ),

    DrawerActivities(
        activityName = "Post About Last Sunday Service",
        activityIcon = R.drawable.baseline_church_24,
        routePath = "Last-Sunday"
    ),

    )

@Composable
fun DrawerScreen(
    drawerState: DrawerState,
    navController: NavHostController,
    handleAlterNavIndex: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(.8f)
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.surfaceContainerHigh),
        verticalArrangement = Arrangement.Center

    ) {


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 50.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.apostle),
                contentDescription = "image",
                Modifier
                    .clip(CircleShape)
                    .size(160.dp)
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "church apostle",
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = "+2541234567890",
                modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = "Apostle David Uche",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.padding(3.dp))
            Text(
                text = "daviduche@gmail.com",
            )
            Spacer(modifier = Modifier.padding(4.dp))

            Button(onClick = {}) {
                Text(
                    text = "Update Details",
                )
            }

        }

        HorizontalDivider(Modifier.padding(30.dp))

        activityList.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable {
                        //navigate to the path
                        navController.navigate(route = it.routePath)

                        //close the drawer
                        coroutineScope.launch {
                            drawerState.close()
                        }

                        //alter the index of the bottom nav bar to highest value
                        handleAlterNavIndex.invoke()
                    }
            ) {
                Icon(
                    painter = painterResource(it.activityIcon),
                    contentDescription = it.activityName,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(20.dp), tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = it.activityName,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
        }

    }

}





