package com.shimmita.full_gospel.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.shimmita.full_gospel.R
import com.shimmita.full_gospel.firebase.FirebaseConfig

@Composable
fun RegistrationScreen(
    handleNavigateLogin: () -> Unit,
    handleRegistrationSuccess: () -> Unit,
    handleRegistrationFailed: () -> Unit
) {
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var nameText by remember { mutableStateOf("") }
    var phoneText by remember { mutableStateOf("") }
    var genderText by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }


    val roleText = listOf(
        "Apostle",
        "Reverent",
        "Deacon",
        "Pastor",
        "Youth",
        "Member",
        "Chairman",
        "Treasurer",
        "ChairLady",
        "Mens Chair",
        "Women's Chair",
        "Choir Team",
        "Choir Leader",
        "Youths Chair",
        "Instrumentalist",
    )




    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "ACCOUNT REGISTRATION",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.padding(vertical = 10.dp))


        Text(
            text = "VICTORY BELONGS TO JESUS",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall

        )

        Spacer(modifier = Modifier.padding(vertical = 7.dp))




        Text(
            text = "~ Claim your victory today ~",
            style = MaterialTheme.typography.bodySmall
        )


        OutlinedTextField(value = nameText, onValueChange = {
            nameText = it
        }, label = {
            Text(text = "Name")
        }, leadingIcon = {
            Icon(imageVector = Icons.Filled.Create, contentDescription = "Name")
        }, placeholder = {
            Text(text = "Alice Kawira")
        }, enabled = !isProcessing)

        Spacer(modifier = Modifier.padding(vertical = 7.dp))



        OutlinedTextField(value = phoneText, onValueChange = {
            phoneText = it
        }, label = {
            Text(text = "Phone")
        }, leadingIcon = {
            Icon(imageVector = Icons.Filled.Call, contentDescription = "Phone")
        }, placeholder = {
            Text(text = "+2547123456789")
        }, enabled = !isProcessing)

        Spacer(modifier = Modifier.padding(vertical = 7.dp))


        OutlinedTextField(value = genderText, onValueChange = {
            genderText = it
        }, label = {
            Text(text = "Gender")
        }, leadingIcon = {
            Icon(imageVector = Icons.Filled.Face, contentDescription = "Gender")
        }, placeholder = {
            Text(text = "Male or Female")
        }, enabled = !isProcessing)


        Spacer(modifier = Modifier.padding(vertical = 7.dp))


        OutlinedTextField(value = emailText, onValueChange = {
            emailText = it
        }, label = {
            Text(text = "Email")
        }, leadingIcon = {
            Icon(imageVector = Icons.Filled.Email, contentDescription = "Email")
        }, placeholder = {
            Text(text = "annabelle@gmail.com")
        }, enabled = !isProcessing)

        Spacer(modifier = Modifier.padding(vertical = 7.dp))


        OutlinedTextField(value = passwordText, onValueChange = {
            passwordText = it
        }, label = {
            Text(text = "Password")
        }, leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.baseline_lock_24),
                contentDescription = "Email"
            )
        }, placeholder = {
            Text(text = "*******")
        }, enabled = !isProcessing)

        Spacer(modifier = Modifier.padding(vertical = 7.dp))


        OutlinedTextField(
            value = selectedRole,
            onValueChange = { selectedRole = it },
            label = { Text("Position") },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown icon"
                    )
                }
            },
            readOnly = true, // Prevent manual input
            singleLine = true, enabled = !isProcessing
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            roleText.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    onClick = {
                        selectedRole = option
                        expanded = false
                    }
                )
            }
        }


        Spacer(modifier = Modifier.padding(vertical = 10.dp))



        Button(
            onClick = {
                // processing true
                isProcessing = true
                //instantiate fbAuth
                val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
                //init firestore
                val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

                firebaseAuth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("Reg", "RegistrationScreen: success->email+pass ")
                            // proceed save user in firestore
                            val currentUserID = firebaseAuth.currentUser?.uid

                            val nameKey = "Name"
                            val phoneKey = "Phone"
                            val genderKey = "Gender"
                            val roleKey = "Role"

                            //hashmap
                            val hashmapMembers = hashMapOf(
                                nameKey to nameText,
                                phoneKey to phoneText,
                                genderKey to genderText,
                                roleKey to roleText
                            )

                            //proceed saving user details in the firestore collection/userID
                            if (currentUserID != null) {
                                fireStore.collection(FirebaseConfig.fireStoreCollection)
                                    .document(currentUserID).set(hashmapMembers)
                                    .addOnCompleteListener { storeResponse ->
                                        if (storeResponse.isSuccessful) {

                                            Log.d(
                                                "Reg",
                                                "RegistrationScreen: success->firestore->all done "
                                            )


                                            //false is processing
                                            isProcessing = false
                                            //navigate to login
                                            handleRegistrationSuccess.invoke()
                                        }

                                        if (!storeResponse.isSuccessful) {

                                            Log.d(
                                                "Reg",
                                                "RegistrationScreen: failed->firestore->all done "
                                            )

                                            //false is processing
                                            isProcessing = false
                                            //invoke the failed fun at the parent level
                                            handleRegistrationFailed.invoke()
                                        }
                                    }
                            }

                        }

                        //failed at register email and password
                        if (!it.isSuccessful) {
                            Log.d("Reg", "RegistrationScreen: failed->email+pass->all done ")

                            //false is processing
                            isProcessing = false
                            //invoke the failed fun at the parent level
                            handleRegistrationFailed.invoke()
                        }
                    }

            },
            modifier = Modifier.padding(),
            enabled = nameText.isNotEmpty() && phoneText.isNotEmpty() && genderText.isNotEmpty() && emailText.isNotEmpty() && passwordText.isNotEmpty() && selectedRole.isNotEmpty() && !isProcessing
        ) {
            Text(
                text = "Click Me To Register",
            )
        }


        TextButton(onClick = { handleNavigateLogin.invoke() }, enabled = !isProcessing) {
            Text(
                text = "Back to Login",
            )
        }


        Spacer(modifier = Modifier.padding(5.dp))
        //progress dialog is processing
        if (isProcessing) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,

                ) {
                CircularProgressIndicator(modifier = Modifier.size(25.dp))

            }
        }


    }
}


@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun RegisterPreview() {
    RegistrationScreen(
        handleNavigateLogin = {},
        handleRegistrationSuccess = {},
        handleRegistrationFailed = {}
    )
}