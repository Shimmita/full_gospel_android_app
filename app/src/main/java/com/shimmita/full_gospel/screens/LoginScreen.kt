import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
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
import com.shimmita.full_gospel.R

@Composable
fun LoginScreen(handleNavigateRegistration: () -> Unit) {
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }


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

        OutlinedTextField(value = emailText, onValueChange = {
            emailText = it
        }, label = {
            Text(text = "Email")
        }, leadingIcon = {
            Icon(imageVector = Icons.Filled.Email, contentDescription = "Email")
        }, placeholder = {
            Text(text = "annabelle@gmail.com")
        }, modifier = Modifier.padding(bottom = 15.dp, start = 10.dp, top = 10.dp, end = 10.dp))


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
        }, modifier = Modifier.padding(bottom = 30.dp, start = 10.dp, top = 10.dp, end = 10.dp))

        Button(onClick = {}) {
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
            ) {
                Text(
                    text = "Create New Account",

                    )
            }

            TextButton(
                onClick = {},
            ) {
                Text(
                    text = "Reset Password",

                    )
            }
        }

    }


}



