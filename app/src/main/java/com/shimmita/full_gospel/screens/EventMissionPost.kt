package com.shimmita.full_gospel.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shimmita.full_gospel.firebase.FirebaseConfig

@Composable
fun EventMissionPost(handleNavigateHome: () -> Unit) {


    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val fireStore = FirebaseFirestore.getInstance()
    var missionTitle by remember { mutableStateOf("") }
    var missionDetails by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {

        //progress dialog is processing
        if (isProcessing) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,

                ) {
                CircularProgressIndicator(modifier = Modifier.size(30.dp))

            }
        }

        Spacer(modifier = Modifier.padding(vertical = 5.dp))


        Text(text = "Post Mission or Event that is Upcoming")
        Spacer(modifier = Modifier.padding(5.dp))

        Text(
            text = "upcoming missions or events helps the members of the church to get well prepared for the goodness of spreading the word.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.padding(10.dp))


        Text(
            text = "Provide Title",
            style = MaterialTheme.typography.bodyMedium, modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Start)
                .padding(start = 3.dp)
        )
        OutlinedTextField(
            minLines = 2,
            value = missionTitle,
            onValueChange = {
                missionTitle = it
            },
            label = {
                Text(text = "Mission or Event Title")
            },

            placeholder = {
                Text(text = "write the title here...")
            },
            enabled = !isProcessing,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Provide Description",
            style = MaterialTheme.typography.bodyMedium, modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Start)
                .padding(start = 3.dp)
        )
        OutlinedTextField(
            minLines = 10,
            value = missionDetails,
            onValueChange = {
                missionDetails = it
            },
            label = {
                Text(text = "Mission or Event Description")
            },

            placeholder = {
                Text(text = "write the description here...")
            },
            enabled = !isProcessing,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(vertical = 5.dp))

        Button(
            onClick = {
                // processing true
                isProcessing = true

                //upload data to firestore under Announcement
                val currentUserID = currentUser?.uid
                //hashmap
                val keyTitle = "title"
                val keyDetails = "Details"

                val hashMap = hashMapOf(keyTitle to missionTitle, keyDetails to missionDetails)

                if (currentUserID != null) {
                    fireStore.collection(FirebaseConfig.fireStoreEventsMission)
                        .document(currentUserID)
                        .set(hashMap).addOnCompleteListener {
                            if (it.isSuccessful) {
                                //false is processing
                                isProcessing = false

                                //toast message
                                Toast.makeText(context, "posted successfully", Toast.LENGTH_LONG)
                                    .show()

                                //navigate home
                                handleNavigateHome.invoke()

                            }

                            if (!it.isSuccessful) {
                                //false is processing
                                isProcessing = false

                                //failed message
                                Toast.makeText(context, "Failed to Post", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                }

            },
            modifier = Modifier.align(alignment = Alignment.End),
            enabled = !isProcessing && missionDetails.isNotEmpty()
        ) {
            Text(text = "Post")
        }
    }
}