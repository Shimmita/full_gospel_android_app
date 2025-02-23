package com.shimmita.full_gospel.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shimmita.full_gospel.R

@Composable
fun StarterScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(start = 5.dp)
            .verticalScroll(rememberScrollState()),


        ) {


        Text(
            text = "Today's Prayer",
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp, start = 10.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold

        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 4.dp, top = 8.dp, bottom = 30.dp)

        ) {
            Column(
                Modifier
                    .weight(.32f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Apst. David Uche",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary


                )
                Text(
                    text = "Our heavenly father we thank you for your priceless abundant loving and kind ...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
                    softWrap = true
                )

                OutlinedButton(onClick = {}) {
                    Text("Lets Pray")
                }


            }
            Image(
                painter = painterResource(R.drawable.apostle),
                contentDescription = "image",
                Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .size(200.dp),
                contentScale = ContentScale.Crop
            )


        }



        Text(
            text = "Weekly Manna",
            modifier = Modifier
                .padding(bottom = 20.dp, start = 10.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold

        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 4.dp, top = 8.dp, bottom = 30.dp)

        ) {
            Column(
                Modifier
                    .weight(.32f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Mark 4:6",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary


                )
                Text(
                    text = "But when the sun was up, it was scorched; and because it had no root, it withered.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
                    softWrap = true
                )

                OutlinedButton(onClick = {}) {
                    Text("Consume")
                }


            }
            Image(
                painter = painterResource(R.drawable.grow),
                contentDescription = "image",
                Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .size(200.dp),
                contentScale = ContentScale.Crop
            )

        }


        Text(
            text = "Sunday Diaries",
            modifier = Modifier
                .padding(bottom = 20.dp, start = 10.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 4.dp, top = 8.dp, bottom = 2.dp)

        ) {
            Column(
                Modifier
                    .weight(.32f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Power of Worship",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary


                )
                Text(
                    text = "At times we often encounter matured raging waves of life and start conflicting with ...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
                    softWrap = true
                )

                OutlinedButton(onClick = {}) {
                    Text("Lets Review")
                }

            }

            Image(
                painter = painterResource(R.drawable.mbonyi_1),
                contentDescription = "image",
                Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .size(200.dp),
                contentScale = ContentScale.Crop
            )


        }



        Text(
            text = "Nature Talents",
            modifier = Modifier
                .padding(bottom = 20.dp, start = 10.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 4.dp, top = 8.dp, bottom = 2.dp)

        ) {
            Column(
                Modifier
                    .weight(.32f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Israel Mbonyi",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary


                )
                Text(
                    text = "Meet and promote the upcoming gospel artist who is getting widened air-waves ...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
                    softWrap = true
                )

                OutlinedButton(onClick = {}) {
                    Text("Lets Nature")
                }

            }

            Image(
                painter = painterResource(R.drawable.mbonyi_2),
                contentDescription = "image",
                Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .size(200.dp),
                contentScale = ContentScale.Crop
            )


        }



        Text(
            text = "Events Calendar",
            modifier = Modifier
                .padding(bottom = 20.dp, start = 10.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 4.dp, top = 8.dp, bottom = 2.dp)

        ) {
            Column(
                Modifier
                    .weight(.32f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Changamwe Mission",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary


                )
                Text(
                    text = "Prepare for the upcoming mission, come lets support this marvelous event by ...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
                    softWrap = true
                )

                OutlinedButton(onClick = {}) {
                    Text("View Event")
                }

            }

            Image(
                painter = painterResource(R.drawable.cross),
                contentDescription = "image",
                Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .size(200.dp),
                contentScale = ContentScale.Crop
            )


        }


        Text(
            text = "Top Testimonials",
            modifier = Modifier
                .padding(bottom = 20.dp, start = 10.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 4.dp, top = 8.dp, bottom = 2.dp)

        ) {
            Column(
                Modifier
                    .weight(.32f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Kavita Millicent",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary


                )
                Text(
                    text = "I take this opportunity to glorify our Lord and Saviour Jesus Christ for what He...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
                    softWrap = true
                )

                OutlinedButton(onClick = {}) {
                    Text("Lets Read")
                }

            }

            Image(
                painter = painterResource(R.drawable.way),
                contentDescription = "image",
                Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .size(200.dp),
                contentScale = ContentScale.Crop
            )


        }

    }


}


//starter topBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarterTopBar(
    bellIconClicked: () -> Unit,
    registerAccountClicked: () -> Unit,
    shareApp: () -> Unit,
    handleToggleDrawer: () -> Unit
) {

    TopAppBar(title = {
        Text(
            "Full Gospel Church".uppercase(), style = MaterialTheme.typography.titleMedium
        )
    }, actions = {

        IconButton(onClick = {
            bellIconClicked.invoke()
        }) {
            Icon(
                imageVector = Icons.Rounded.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .size(22.dp)
            )
        }

        IconButton(onClick = {
            registerAccountClicked.invoke()
        }) {
            Icon(
                painter = painterResource(R.drawable.baseline_person_add_alt_1_24),
                contentDescription = "account",
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .size(22.dp)


            )
        }

        IconButton(onClick = {
            shareApp.invoke()
        }) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "account",
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .size(22.dp)

            )
        }
    },
        navigationIcon = {
            IconButton(onClick = { handleToggleDrawer.invoke() }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "open-drawer")
            }
        },

        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.scrim,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        )
    )

}


//nav items Bottom Nav
data class NavItems(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)

