package com.example.composablevadisabilitycalc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composablevadisabilitycalc.ui.theme.ComposableVADisabilityCalcTheme
import com.example.composablevadisabilitycalc.ui.theme.dark_green
import com.example.composablevadisabilitycalc.ui.theme.my_primary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposableVADisabilityCalcTheme {
                MyApp(context = this)
            }
        }
    }
    // Make icon view on phone screen and load
    // Make edit function able to change Bilateral
}

@Composable
fun MyApp(context: Context) {
    val navController = rememberNavController()
    // Using Android JetPack controls navigation of multiple screens
    val dbHelper = DatabaseHelper(context)

    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("registration") { RegistrationScreen(navController, dbHelper) }
        composable("DisabilitiesEntry/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toLong()
            if (userId != null) {
                DisabilitiesEntry(navController, dbHelper, userId)
            } else {
                // Handle the case where userId is null
                // Empty values are handled elsewhere
            }
        } // End of composable for DisabilitiesEntry
        composable("calculate_screen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toLong()
            if (userId != null) {
                CalculateScreen(navController, dbHelper, userId)
            } else {
                // Handle the case where userId is null
                // Empty values are handled elsewhere
            }
        } // End of composable for CalculateScreen
    } // End of NavHost
}// End of MyApp

@Composable
fun LoginScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_image2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        ) // This image will be the background image filling in the entire box area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
            // This Column is created within the box and all items are placed within it
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.deelite_5),
                contentDescription = null,
                modifier = Modifier
                    .size(190.dp)
                    .align(Alignment.CenterHorizontally)
            ) // This image is placed on top of the Column
            Text(
                text = stringResource(R.string.welcome_text),
                color = my_primary,
                fontSize = 28.sp
            )
            // To Use My_primary color in this code add the new variable to Color.kt
            // Upon entering the color value here use alt-enter to add it in imports

            Spacer(modifier = Modifier.height(244.dp))

            Text(
                text = stringResource(R.string.intro_instructions),
                color = my_primary,
                fontSize = 16.sp
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    textStyle = TextStyle(color = my_primary)
                ) // text field for user name entry
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    textStyle = TextStyle(color = my_primary),
                    //visualTransformation = PasswordVisualTransformation()
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                        }
                    },
                    singleLine = true
                ) // Text field for password entry

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { // Login Button
                    if (username.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Username and password cannot be empty", Toast.LENGTH_SHORT).show()
                    } else {
                        // Check if the username and password match in the database
                        val databaseHelper = DatabaseHelper(context)
                        val isMatch = databaseHelper.checkUser(username, password)
                        if (isMatch) {
                            val userID = databaseHelper.getUserId((username))
                            if (userID != null ){
                                // Log successful login for debugging
                                Log.d("LoginActivity", "Login successful, navigating to DisabilitiesEntry")
                                // Navigate to DisabilitiesEntry screen if there's a match
                                navController.navigate("DisabilitiesEntry/$userID")
                            } else {
                                Toast.makeText(context, "Error retrieving user ID", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Handle failed login
                            Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Text("Login")
                } // End of Button Login

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { navController.navigate("registration") }) {
                    Text("Register")
                } // End of Button to send to Register
            } // end column

            Spacer(modifier = Modifier.height(24.dp))
            // This spacer adds some distance from bottom of screen under the column
        } // end of Column
    } // End of Box
} // End Function LoginScreen


@Composable
fun RegistrationScreen(navController: NavHostController, dbHelper: DatabaseHelper) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // Background Image
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.arrow_image2),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize()
        )
        // Overlay and form
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to Dee Lite Studios VA Disability Calculator",
                    color = Color.White,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text= "Register your User name (unique) and password",
                    color = Color.White,
                    )
                Spacer(modifier = Modifier.height(8.dp))

                //@Suppress("DEPRECATION")
                TextField(
                    value = username,
                    onValueChange = {
                        username = it
                        showError = false },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    //colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        showError = false },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    //visualTransformation = PasswordVisualTransformation(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                        }
                    },
                    singleLine = true
                    //colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
                )
                if (showError){
                    Text(
                        text = "Username and password must not be empty",
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                /* Handle register */
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        dbHelper.insertUser(username, password)
                        navController.navigate("login")
                    } else {
                        showError = true
                    }},
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text("Register")
                } // Register Button
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {  navController.navigate("login") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Login")
                } // Back to login Button
            } // End of Column
        } // end of internal Box
    } // End of primary Box
}// end of function RegistrationScreen

@Composable
fun DisabilitiesEntry(navController: NavHostController, dbHelper: DatabaseHelper, userId: Long) {
    var disabilityName by remember { mutableStateOf("") }
    var disabilityPercentage by remember { mutableStateOf("") }
    var disabilities by remember { mutableStateOf(dbHelper.getDisabilities(userId)) }
    var selectedDisability by remember { mutableStateOf<Triple<Long, String, Double>?>(null) }
    var showError by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var bilateralFactorSelected by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_image2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text("Disabilities Entry:", color = dark_green, fontSize = 40.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text("List:")

            LazyColumn {
                items(disabilities) { (id, name, percentage) ->
                    val isSelected = selectedDisability?.first == id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedDisability = Triple(id, name, percentage)
                                disabilityName = name
                                disabilityPercentage = percentage.toString()
                                isEditing = true
                            }
                            .padding(8.dp)
                            .background(if (isSelected) Color.LightGray else Color.Transparent)
                    ) {
                        Text("Disability: $name, Percentage: $percentage%")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("To EDIT select the disability within the list above")

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = disabilityName,
                onValueChange = {
                    disabilityName = it
                    showError = false
                },
                label = { Text("Disability Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = disabilityPercentage,
                onValueChange = {
                    disabilityPercentage = it
                    showError = false
                },
                label = { Text("Disability Percentage") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup()
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = bilateralFactorSelected,
                    onClick = { bilateralFactorSelected = !bilateralFactorSelected },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Column {
                    Text("Bilateral Factor")
                    Text(
                        "Make sure your bilateral factor is only used once per bilateral unit affected.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (showError) {
                Text(
                    text = "Disability name and percentage cannot be empty",
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isEditing) {
                Button(
                    onClick = {
                        if (disabilityName.isNotEmpty() && disabilityPercentage.isNotEmpty()) {
                            dbHelper.updateDisability(selectedDisability!!.first, disabilityName, disabilityPercentage.toDouble())
                            disabilities = dbHelper.getDisabilities(userId)
                            // Check if the bilateral factor radio button is selected
                            if (bilateralFactorSelected) {
                                dbHelper.insertDisability(userId, "$disabilityName bilateral value", 10.0)
                            }
                            disabilities = dbHelper.getDisabilities(userId)
                            disabilityName = ""
                            disabilityPercentage = ""
                            selectedDisability = null
                            isEditing = false
                            bilateralFactorSelected = false
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        selectedDisability = null
                        disabilityName = ""
                        disabilityPercentage = ""
                        isEditing = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            } else {
                Button(
                    onClick = {
                        if (disabilityName.isNotEmpty() && disabilityPercentage.isNotEmpty()) {
                            dbHelper.insertDisability(userId, disabilityName, disabilityPercentage.toDouble())
                            if (bilateralFactorSelected){
                                dbHelper.insertDisability(userId, "$disabilityName bilateral value", 10.0)
                                bilateralFactorSelected = false
                            }
                            disabilities = dbHelper.getDisabilities(userId)
                            disabilityName = ""
                            disabilityPercentage = ""
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Disability")
                } // End of Add button

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        navController.navigate("calculate_screen/$userId")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Calculate")
                } // End of Calc button

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        dbHelper.deleteAllDisabilitiesForUser(userId)
                        disabilities = dbHelper.getDisabilities(userId)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("DELETE ALL")
                } // End of Delete button

            } // End of else for Button Editing
        } // End of Column
    } // End of box
} // End of DisabilitiesEntry function

@Composable
fun CalculateScreen(navController: NavHostController, dbHelper: DatabaseHelper, userId: Long) {
    val context = LocalContext.current
    val calculatorHelper = CalculatorHelper()
    val disabilities = dbHelper.getDisabilities(userId).sortedByDescending { it.third }

    // Adding debugging
    Log.d("CalculateScreen", "Disabilities: $disabilities")

    val finalValue = try {
        calculatorHelper.calculateFinalValue(disabilities.map {
            Disability(it.first, it.second, it.third)
        })
    } catch (e: Exception) {
        Log.e("CalculateScreen", "Error calculating final value: ${e.message}")
        0.0 // Return a default value in case of error
    }
    // Round the final value to the nearest 10th
    val roundedFinalValue = calculatorHelper.roundToNearestTenth(finalValue)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_image2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text("Calculated Results Screen")

            Spacer(modifier = Modifier.height(8.dp))

            Text("Sorted List:")

            LazyColumn {
                items(disabilities) { ( id, disability, percentage) ->
                    // id value must remain in order to correctly display values of dis and percentage
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("Disability: $disability, Percentage: $percentage%")
                    }
                }
            } // End of LazyColumn

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "FINAL VALUE: $finalValue",
                modifier = Modifier.padding(16.dp),
                color = Color.Black,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "VA's FINAL PERCENTAGE: $roundedFinalValue",
                modifier = Modifier.padding(16.dp),
                color = Color.Black,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Create the share intent
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            buildString {
                                append("Disabilities:\n")
                                disabilities.forEach { (_, name, percentage) ->
                                    append("Disability: $name, Percentage: $percentage%\n")
                                }
                                append("\nFINAL VALUE: $finalValue")
                                append("\nVA's FINAL PERCENTAGE: $roundedFinalValue")
                            }
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share results via"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Share Results")
            } // Final value SHARE Button

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            } // Back Button

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Close the app
                    (context as? Activity)?.finish()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("FINISH & EXIT")
            } // Finish Button

            Spacer(modifier = Modifier.height(60.dp))
            DonationMessage()
        } // End of Column
    } // End of Box
}// end CalculateScreen function

@Composable
fun DonationMessage() {
    val uriHandler = LocalUriHandler.current
    Text(
        text = stringResource(R.string.donate_message),
        color = my_primary, // Text in custom color
        fontSize = 16.sp, // 16.sp font size
        modifier = Modifier.padding(bottom = 8.dp)
    )
    Text(
        text = AnnotatedString("https://www.paypal.com/donate/?hosted_button_id=2PR9KAC7C3Y5J"),
        color = Color.Blue, // Color for the link
        textDecoration = TextDecoration.Underline,
        modifier = Modifier
            .clickable { uriHandler.openUri("https://www.paypal.com/donate/?hosted_button_id=2PR9KAC7C3Y5J") }
            .padding(top = 8.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // Add in a dummy context for preview purposes
    //val context = androidx.compose.ui.platform.LocalContext.current
    val context = LocalContext.current
    MyApp(context = context)

}