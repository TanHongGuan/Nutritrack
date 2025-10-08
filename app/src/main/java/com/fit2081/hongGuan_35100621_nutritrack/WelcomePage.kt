package com.fit2081.HongGuan35100621

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import java.io.BufferedReader
import java.io.InputStreamReader
import com.fit2081.HongGuan35100621.ui.theme.A1_35100621_TanHongGuanTheme
import com.fit2081.hongGuan_35100621_nutritrack.data.NutriTrackDatabase
import com.fit2081.hongGuan_35100621_nutritrack.data.loadCSVFirstLaunch
import com.fit2081.hongGuan_35100621_nutritrack.data.patient.PatientViewModel
import kotlinx.coroutines.launch


class WelcomePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val patientViewModel: PatientViewModel = ViewModelProvider(
            this,
            PatientViewModel.PatientViewModelFactory(this)
        )[PatientViewModel::class.java]
        // SharedPreferences to check if user first launch app
        val sharedPrefLaunch = getSharedPreferences("firstLaunch", Context.MODE_PRIVATE)
        val isFirstLaunch = sharedPrefLaunch.getBoolean("isFirstLaunch", true)

        if (isFirstLaunch) {
            // Get database instance
            val database = NutriTrackDatabase.getDatabase(applicationContext)

            // Launch coroutine in lifecycle scope
            lifecycleScope.launch {
                loadCSVFirstLaunch(applicationContext, database)

                // Mark as not first launch
                sharedPrefLaunch.edit().putBoolean("isFirstLaunch", false).apply()

                // Check how many patients were loaded
                val patientViewModel = ViewModelProvider(
                    this@WelcomePage,
                    PatientViewModel.PatientViewModelFactory(this@WelcomePage)
                )[PatientViewModel::class.java]

            }
        }

        enableEdgeToEdge()
        setContent {
            A1_35100621_TanHongGuanTheme {
                welcomeScreen(patientViewModel)
            }

        }

    }
}

// Composable function to display welcome screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun welcomeScreen(patientViewModel: PatientViewModel) {
    var showLoginSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val sharedPrefLogin = context.getSharedPreferences("loggedIn", Context.MODE_PRIVATE)
    val loggedIn = sharedPrefLogin.getBoolean("loggedIn", false)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Application Name
            Text(
                text = "NutriTrack",
                style = TextStyle(
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
            )
            // NutriTrack Logo
            Image(
                painter = painterResource(id = R.drawable.logo),  // Replace with actual image
                contentDescription = "NutriTrack Logo",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App Description
            Text(
                text = "This app provides general health and nutrition information for educational purposes only. It is not intended as medical advice, diagnosis, or treatment. Always consult a qualified healthcare professional before making any changes to your diet, exercise, or health regimen. Use this app at your own risk. If you’d like to an Accredited Practicing Dietitian (APD), please visit the Monash Nutrition/Dietetics Clinic (discounted rates for students): https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(16.dp),
                style = TextStyle(lineHeight = 24.sp)
            )


            Spacer(modifier = Modifier.height(24.dp))

            // Login Button (Opens Modal)
            Button(
                onClick = {
                    if (loggedIn) {
                        context.startActivity(Intent(context, BottomNavigationBar::class.java))
                    } else {
                        sharedPrefLogin.edit().putBoolean("loggedIn", true).apply()
                        showLoginSheet = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Login")
            }
        }

        // Show Login Modal if Button is Clicked
        if (showLoginSheet) {
            ModalBottomSheet(
                onDismissRequest = { showLoginSheet = false },
                sheetState = bottomSheetState
            ) {

                // Pass the showLoginSheet function to loginPage to handle dismissal
                loginPage(patientViewModel)

            }
        }
    }
}

// Composable function to display pop up modal for Login
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun loginPage(patientViewModel: PatientViewModel) {  // Receive the showLoginSheet function to control modal dismissal
    var userID by remember { mutableStateOf("userID") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    var showRegisterPage by remember { mutableStateOf(false) }  // Control the register page visibility
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(Modifier.height(16.dp))

            // Header
            Text(
                text = "Login Page",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold
                ))

            Spacer(modifier = Modifier.height(16.dp))

            val userIDs = loadUserIDs()
            userID = usernameTextField(userIDs)

            // Password Text field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "This app is only for pre-registered users. Please have your ID and phone number handy before continuing",
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Login button

            val coroutineScope = rememberCoroutineScope()

            Button(onClick = {
                coroutineScope.launch {
                    val storedPassword = patientViewModel.getUserPassword(userID)
                    val username = patientViewModel.getUserName(userID)
                    val phoneNumber = patientViewModel.getPhoneNumber(userID)
                    if (storedPassword == password) {
                        // Save current user details
                        val currentUser = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE)
                        val currentUserEditor = currentUser.edit()
                        currentUserEditor.putString("USER_ID", userID)
                        currentUserEditor.putString("PHONE_NUMBER", phoneNumber)
                        currentUserEditor.putString("USERNAME", username)

                        currentUserEditor.apply()

                        context.startActivity(Intent(context, QuestionnairePage::class.java))
                    } else {
                        Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text("Continue")
            }


            // Register button
            Button(
                onClick = {
                    showRegisterPage = true  // Show the register page
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Register")
            }

            // Show Register Modal if Button is Clicked
            if (showRegisterPage) {
                ModalBottomSheet(
                    onDismissRequest = { showRegisterPage = false },
                    sheetState = bottomSheetState
                ) {
                    registerPage(patientViewModel)
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun registerPage(patientViewModel: PatientViewModel) {
    var showLoginSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var userID by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("")}

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(Modifier.height(16.dp))

            // Header
            Text(
                text = "Register Page",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            val userIDs = loadUserIDs()
            userID = usernameTextField(userIDs)

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Number Text Field
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text(text = "Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Username Text Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))


            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(text = "Confirm Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "This app is only for pre-registered users. Please enter your ID, phone number and password to claim your account.",
                modifier = Modifier.padding(16.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Register button
            val context = LocalContext.current
            val userDetails = loadUserDetails()


            Button(
                onClick = {
                    if (!userDetails.containsKey(userID)) {
                        Toast.makeText(context, "User not found. Please register", Toast.LENGTH_LONG).show()

                    } else if (userDetails[userID] != phoneNumber) {
                        Toast.makeText(context, "Phone Number does not match", Toast.LENGTH_LONG).show()

                    } else if (userID.isBlank() || phoneNumber.isBlank() || password.isBlank() || confirmPassword.isBlank() || username.isBlank()) {
                        Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()

                    } else if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show()

                    } else {
                        // Update user's name and password
                        patientViewModel.updateNameAndPassword(userID, username, password)

                        Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()

                    }

                },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Register")
            }

            Spacer(Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = {
                    showLoginSheet = true
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)


            ) {
                Text("Log In")
            }
        }

        // Show Login Modal if Button is Clicked
        if (showLoginSheet) {
            ModalBottomSheet(
                onDismissRequest = { showLoginSheet = false },
                sheetState = bottomSheetState
            ) {
                loginPage(patientViewModel)
            }

        }
    }
}



// Composable function to load phone numbers and usernames into a list
@Composable
fun loadUserDetails(): MutableMap<String,String> {

    val userDetails = mutableMapOf<String, String>() // Map userIDs to phone numbers
    val context = LocalContext.current
    val assets = context.assets // Get asset manager

    // try to open CSV file and read it line by line
    try {
        val inputStream = assets.open("user_data.csv") // Open file from assets

        // Create buffered reader for efficient reading
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.useLines { lines ->
            lines.drop(1).forEach { line -> // Skip header row
                val values = line.split(",") // Split each line into values
                // Get the user id -> the second column
                val phoneNumber = values[0]
                val userID = values[1]
                userDetails[userID] = phoneNumber
            }
        }
    } catch (e: Exception) {
        // To handle exceptions
        Toast.makeText(context, "User File Does Not Exist", Toast.LENGTH_LONG).show()
    }
    // Return list of usernames
    return userDetails
}

// Load phone numbers and usernames into a list
@Composable
fun loadUserIDs(): MutableList<String> {

    val userIDs = mutableListOf<String>()
    val context = LocalContext.current
    val assets = context.assets // Get asset manager

    // try to open CSV file and read it line by line
    try {
        val inputStream = assets.open("user_data.csv") // Open file from assets
        // Create buffered reader for efficient reading
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.useLines { lines ->
            lines.drop(1).forEach { line -> // Skip header row
                val values = line.split(",") // Split each line into values
                // Get the user id -> the second column
                val userID = values[1]
                userIDs.add(userID)
            }
        }
    } catch (e: Exception) {

        // To handle exceptions
        Toast.makeText(context, "User File Does Not Exist", Toast.LENGTH_LONG).show()
    }
    // Return list of usernames
    return userIDs
}

// Composable function to display usernameTextField
@Composable
fun usernameTextField(usernames : List<String>): String {

    var expanded by remember { mutableStateOf(false)}
    var selectedUsername by remember { mutableStateOf("")}

    Box{
        OutlinedTextField(
            value = selectedUsername,
            onValueChange = {
                selectedUsername = it
                expanded = true
            },
            label = { Text("User ID")},
            modifier = Modifier.fillMaxWidth(),
            readOnly = false
        )

        DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
            usernames.forEach { username ->
                DropdownMenuItem(onClick = {
                    selectedUsername = username
                    expanded = false
                },
                    text = { Text(username) }
                )
            }
        }
    }
    return selectedUsername
}
