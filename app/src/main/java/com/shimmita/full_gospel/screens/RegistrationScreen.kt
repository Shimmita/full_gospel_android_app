package com.shimmita.full_gospel.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.shimmita.full_gospel.R

@Composable
fun RegistrationScreen(handleNavigateLogin: () -> Unit) {
    var emailText by remember { mutableStateOf("") }
    var nameText by remember { mutableStateOf("") }
    var phoneText by remember { mutableStateOf("") }
    var genderText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

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

    // Create a string value to store the selected city
    var selectedRole by remember { mutableStateOf("") }


    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "ACCOUNT REGISTRATION",
            modifier = Modifier.padding(bottom = 15.dp),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )


        Text(
            text = "VICTORY BELONGS TO JESUS",
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall

        )
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
        }, modifier = Modifier.padding(bottom = 10.dp))



        OutlinedTextField(value = phoneText, onValueChange = {
            phoneText = it
        }, label = {
            Text(text = "Phone")
        }, leadingIcon = {
            Icon(imageVector = Icons.Filled.Call, contentDescription = "Phone")
        }, placeholder = {
            Text(text = "+2547123456789")
        }, modifier = Modifier.padding(bottom = 10.dp))



        OutlinedTextField(value = genderText, onValueChange = {
            genderText = it
        }, label = {
            Text(text = "Gender")
        }, leadingIcon = {
            Icon(imageVector = Icons.Filled.Face, contentDescription = "Gender")
        }, placeholder = {
            Text(text = "Male or Female")
        }, modifier = Modifier.padding(bottom = 10.dp))




        OutlinedTextField(value = emailText, onValueChange = {
            emailText = it
        }, label = {
            Text(text = "Email")
        }, leadingIcon = {
            Icon(imageVector = Icons.Filled.Email, contentDescription = "Email")
        }, placeholder = {
            Text(text = "annabelle@gmail.com")
        }, modifier = Modifier.padding(bottom = 10.dp))


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
        }, modifier = Modifier.padding(bottom = 10.dp))


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
            singleLine = true
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


        Button(
            onClick = {
                handLeRegister(
                    nameText,
                    phoneText,
                    genderText,
                    emailText,
                    passwordText,
                    selectedRole
                )
            },
            modifier = Modifier.padding(top = 10.dp),
            enabled = nameText.isNotEmpty() && phoneText.isNotEmpty() && genderText.isNotEmpty() && emailText.isNotEmpty() && passwordText.isNotEmpty() && selectedRole.isNotEmpty()
        ) {
            Text(
                text = "Click Me To Register",
            )
        }


        TextButton(onClick = { handleNavigateLogin.invoke() }) {
            Text(
                text = "Back to Login",
            )
        }


    }
}

fun handLeRegister(
    nameText: String,
    phoneText: String,
    genderText: String,
    emailText: String,
    passwordText: String,
    selectedRole: String
) {


}


@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun RegisterPreview() {
    RegistrationScreen { }
}