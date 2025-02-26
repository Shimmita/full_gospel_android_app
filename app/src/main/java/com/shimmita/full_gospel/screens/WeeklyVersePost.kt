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
fun WeeklyVersePost(handleNavigateHome: () -> Unit) {

    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val fireStore = FirebaseFirestore.getInstance()
    var verseContent by remember { mutableStateOf("") }
    var bibleVerse by remember { mutableStateOf("") }
    var verseExplanation by remember { mutableStateOf("") }
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


        Text(text = "Post Weekly Verse")
        Spacer(modifier = Modifier.padding(5.dp))

        Text(
            text = "weekly verse helps members to read their scriptures so that they could interpret the message correctly. You are allowed to give your explanation to the members for interpretation.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Provide Bible Verse",
            style = MaterialTheme.typography.bodyMedium, modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Start)
                .padding(start = 3.dp)
        )



        OutlinedTextField(
            value = bibleVerse,
            onValueChange = {
                bibleVerse = it
            },
            label = {
                Text(text = "Bible Verse")
            },

            placeholder = {
                Text(text = "example John 3:17")
            },
            enabled = !isProcessing,
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.padding(top = 30.dp))

        Text(
            text = "Provide Verse Content",
            style = MaterialTheme.typography.bodyMedium, modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Start)
                .padding(start = 3.dp)
        )

        OutlinedTextField(
            minLines = 3,
            value = verseContent,
            onValueChange = {
                verseContent = it
            },
            label = {
                Text(text = "Contents of the Verse")
            },

            placeholder = {
                Text(text = "write the contents of the verse...")
            },
            enabled = !isProcessing,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(top = 30.dp))



        Text(
            text = "Bible Verse Interpretation",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Start)
                .padding(start = 3.dp)
        )


        OutlinedTextField(
            minLines = 7,
            value = verseExplanation,
            onValueChange = {
                verseExplanation = it
            },
            label = {
                Text(text = "Interpretation")
            },

            placeholder = {
                Text(text = "write interpretation here...")
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
                val keyVerse = "verse"
                val keyVerseContent = "content"
                val keyVerseInterpretation = "interpret"

                val hashMap = hashMapOf(
                    keyVerse to bibleVerse,
                    keyVerseContent to keyVerseContent,
                    keyVerseInterpretation to keyVerseInterpretation
                )

                if (currentUserID != null) {
                    fireStore.collection(FirebaseConfig.fireStoreWeeklyVerse)
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
            enabled = !isProcessing && verseContent.isNotEmpty() && bibleVerse.isNotEmpty() && verseExplanation.isNotEmpty()
        ) {
            Text(text = "Post")
        }
    }
}