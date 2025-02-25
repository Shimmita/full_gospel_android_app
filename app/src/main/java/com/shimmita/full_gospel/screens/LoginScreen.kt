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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.shimmita.full_gospel.R

@Composable
fun LoginScreen(
    handleNavigateRegistration: () -> Unit,
    handleNavigateHomeLoginSuccess: () -> Unit,
    handleLoginFailed: () -> Unit
) {
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }


    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Text(
            text = "ACCOUNT LOGIN SECTION",
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
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 15.dp)
        )

        OutlinedTextField(
            value = emailText,
            onValueChange = {
                emailText = it
            },
            label = {
                Text(text = "Email")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Email, contentDescription = "Email")
            },
            placeholder = {
                Text(text = "annabelle@gmail.com")
            },
            modifier = Modifier.padding(bottom = 15.dp, start = 10.dp, top = 10.dp, end = 10.dp),
            enabled = !isProcessing
        )


        OutlinedTextField(
            value = passwordText,
            onValueChange = {
                passwordText = it
            },
            label = {
                Text(text = "Password")
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_lock_24),
                    contentDescription = "Email"
                )
            },
            placeholder = {
                Text(text = "*******")
            },
            modifier = Modifier.padding(bottom = 30.dp, start = 10.dp, top = 10.dp, end = 10.dp),
            enabled = !isProcessing
        )

        Button(onClick = {
            // is processing true
            isProcessing = true
            //init firebase auth
            val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener {
                if (it.isSuccessful) {
                    //false processing
                    isProcessing = false
                    Log.i("Login", "handleLogin: success ")

                    //login successful navigate to home
                    handleNavigateHomeLoginSuccess.invoke()

                } else if (!it.isSuccessful) {
                    //false processing
                    isProcessing = false

                    Log.i("Login", "handleLogin: failed ")
                    //handle login failed
                    handleLoginFailed.invoke()
                }
            }
        }, enabled = !isProcessing) {
            Text(
                text = "Click Me To Login",
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(top = 10.dp)
        ) {

            TextButton(
                onClick = { handleNavigateRegistration.invoke() },
                enabled = !isProcessing

            ) {
                Text(
                    text = "Create New Account",

                    )
            }

            TextButton(
                onClick = {},
                enabled = !isProcessing
            ) {
                Text(
                    text = "Reset Password",

                    )
            }
        }


        Spacer(modifier = Modifier.padding(10.dp))

        //progress dialog is processing
        if (isProcessing) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,

                ) {
                CircularProgressIndicator(modifier = Modifier.size(30.dp))

            }
        }

    }


}






