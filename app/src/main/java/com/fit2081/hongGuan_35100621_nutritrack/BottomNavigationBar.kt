package com.fit2081.HongGuan35100621

import FruityViceViewModel
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fit2081.HongGuan35100621.ui.theme.A1_35100621_TanHongGuanTheme
import com.fit2081.hongGuan_35100621_nutritrack.data.FruityVice.FruityViceAPI
import com.fit2081.hongGuan_35100621_nutritrack.data.GenAI.GenAIViewModel
import com.fit2081.hongGuan_35100621_nutritrack.data.NutriCoachTips.NutriCoachTipsViewModel
import com.fit2081.hongGuan_35100621_nutritrack.data.GenAI.UiState
import com.fit2081.hongGuan_35100621_nutritrack.data.patient.PatientViewModel
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import coil.compose.AsyncImage


class BottomNavigationBar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val patientViewModel: PatientViewModel = ViewModelProvider(
            this,
            PatientViewModel.PatientViewModelFactory(this)
        )[PatientViewModel::class.java]

        val fruityViceViewModel: FruityViceViewModel = ViewModelProvider(
            this,
            FruityViceViewModel.FruityViceViewModelFactory()
        )[FruityViceViewModel::class.java]

        val userID = getSharedPreferences("CurrentUser", Context.MODE_PRIVATE)
            .getString("USER_ID", "DefaultUser")


        val nutriCoachTipsViewModel: NutriCoachTipsViewModel = ViewModelProvider(
            this,
            NutriCoachTipsViewModel.NutriCoachTipsViewModelFactory(this, userID!!)
        )[NutriCoachTipsViewModel::class.java]

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A1_35100621_TanHongGuanTheme {
                val navController: NavHostController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        MyBottomAppBar(navController)
                    }) { innerPadding ->
                    MyNavHost(innerPadding, userID, navController, patientViewModel, fruityViceViewModel, nutriCoachTipsViewModel)

                }
            }
        }
    }
}


// MyNavHost Composable function for navigation within the app
@Composable
fun MyNavHost(innerPadding: PaddingValues,
              userID : String,
              navController: NavHostController,
              patientViewModel: PatientViewModel,
              fruityViceViewModel: FruityViceViewModel,
              nutriCoachTipsViewModel: NutriCoachTipsViewModel) {
    // NavHost composable to define the navigation graph
    NavHost(
        // Use provided NavHostController
        navController = navController,

        // Set start destination to "Home"
        startDestination = "Home",

        ){
        // Define composable for "Home" route
        composable("Home") {
            HomePage(innerPadding, navController)
        }

        // Define composable for "insights" route
        composable("Insight") {
            InsightPage(innerPadding, navController)
        }

        // Define composable for "NutriCoach" route
        composable("NutriCoach") {
            NutriCoachPage(innerPadding, userID, patientViewModel,fruityViceViewModel, nutriCoachTipsViewModel)
        }

        // Define composable for "Settings" route
        composable("Setting") {
            SettingPage(innerPadding, userID ,patientViewModel)
        }

    }
}

// Composable function for creating the bottom navigation bar
@Composable
fun MyBottomAppBar(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // State to track currently selected item in bottomm navigation bar
    var selectedItem by remember { mutableStateOf(0)}

    // List of Navigation items: "Home", "Insight", "NutriCoach", "Setting"
    val items = listOf(
        "Home",
        "Insight",
        "NutriCoach",
        "Setting"
    )

    // NavigationBar composable to define bottom navigation bar
    NavigationBar {
        // Iterate through each item in the 'items' list along with its index
        items.forEachIndexed { index, item ->
            // NavigationBarItem for each item in list
            NavigationBarItem(
                icon = {
                    when (item) {
                        "Home" -> Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home")

                        "Insight" -> Icon(
                            painter = painterResource(id = R.drawable.insightslogo),
                            contentDescription = "Insight",
                            modifier = Modifier.size(24.dp))

                        "NutriCoach" -> Icon(
                            painter = painterResource(id = R.drawable.nutricoachlogo),
                            contentDescription = "NutriCoach",
                            modifier = Modifier.size(24.dp))

                        "Setting" -> Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Setting"
                        )
                    }
                },
                label = {Text(item)},
                selected = currentRoute == item , // highlights the icon no matter the route you take to go to the current screen
                onClick = {
                    selectedItem = index
                    navController.navigate(item)
                }
            )
        }
    }
}

// Composable function for displaying the Home screen.
@Composable
fun HomePage(innerPadding: PaddingValues, navController: NavHostController) {

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        // Welcome Text
        WelcomeHeading()

        // Section for users to edit questionnaire
        EditQuestionnaire()

        // Image
        Image(
            painter = painterResource(id = R.drawable.homepage_picture),
            contentDescription = "Home Page Picture",
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Section to see user's score
        UserScore(navController)

        // Food Quality Score Display
        FoodQualityScore()

        HorizontalDivider(
            modifier = Modifier.padding(8.dp)
        )

        // Section to explain what is Food Quality Score
        FoodQualityExplanation()
    }
}

// Composable function for displaying the Insights screen.
@Composable
fun InsightPage(innerPadding: PaddingValues, navController: NavHostController) {

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Heading
        Heading()

        // Sliders
        Sliders()

        // Total Food Quality Score Display
        TotalFoodQuality()

        // Button to share result
        ShareButton()

        // Button to improve diet
        ImproveDietButton(navController)
    }

}

/**
 * These are the functions for the NutriCoach screen
 */
@Composable
fun NutriCoachPage(innerPadding: PaddingValues,
                   userID : String,
                   patientViewModel: PatientViewModel,
                   fruityViceViewModel: FruityViceViewModel,
                   nutriCoachTipsViewModel : NutriCoachTipsViewModel) {
    val genAIViewModel = viewModel<GenAIViewModel>()
    var showModal by remember { mutableStateOf(false) }
    var shouldShowScoreUI by remember { mutableStateOf(false) }


    // Collect past tips from the viewmodel and convert list of strings
    val pastTips by nutriCoachTipsViewModel.getTipsForUser().collectAsState(initial = emptyList())
    val pastTipsStrings = pastTips.map { it.tip }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "NutriCoach",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // If fruitServe or fruitVariety == 0, it's non-optimal

            LaunchedEffect(Unit) {
                val fruitServe = patientViewModel.getFruitServe(userID)
                val fruitVariety = patientViewModel.getFoodVariety(userID)

                if (fruitServe?.toFloatOrNull() == 0f || fruitVariety?.toFloatOrNull() == 0f) {
                    shouldShowScoreUI = true
                }
            }

            if (shouldShowScoreUI) {
                generateFruitScores(fruityViceViewModel)
            } else{
                AsyncImage(
                    model = "https://picsum.photos/300/200",
                    contentDescription = "Random image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }

            // Motivational Quote Generator
            quoteGenerator(userID, genAIViewModel, nutriCoachTipsViewModel)
        }

        // Show All Tips Button
        Button(
            onClick = { showModal = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .height(56.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "Show All Tips",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Show All Tips")
        }

        if (showModal) {
            AITipsModal(
                aiTips = genAIViewModel.aiTips.collectAsState().value,
                pastTips = pastTipsStrings,
                onDismiss = { showModal = false }
            )
        }
    }
}



@Composable
fun generateFruitScores(fruityViceViewModel: FruityViceViewModel){
    var fruitName by remember { mutableStateOf("") }
    val fruitData by fruityViceViewModel.fruitData.collectAsState()
    val error by fruityViceViewModel.error.collectAsState()
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = fruitName,
            onValueChange = { fruitName = it },
            label = { Text("Fruit Name") },
            placeholder = { Text("Enter fruit...") },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                fruityViceViewModel.fetchFruitData(fruitName.lowercase())
            },
            modifier = Modifier.height(56.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "Search"
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Details")
        }
    }

    LaunchedEffect(error) {
        if (!error.isNullOrBlank()) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    NutrientRow("Family", fruitData?.family ?: "")
    NutrientRow("Calories", fruitData?.nutritions?.calories?.toString() ?: "")
    NutrientRow("Fat", fruitData?.nutritions?.fat?.toString() ?: "")
    NutrientRow("Sugar", fruitData?.nutritions?.sugar?.toString() ?: "")
    NutrientRow("Carbohydrates", fruitData?.nutritions?.carbohydrates?.toString() ?: "")
    NutrientRow("Protein", fruitData?.nutritions?.protein?.toString() ?: "")


}

@Composable
fun quoteGenerator(userID: String, genAIViewModel: GenAIViewModel, nutriCoachTipsViewModel: NutriCoachTipsViewModel) {
    val uiState by genAIViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var nutriCoachTip by remember { mutableStateOf("") }
    val userDetails = getTargetUserDetails(context, userID!!)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                genAIViewModel.sendPrompt(
                    "This is the user details. " +
                            "\n ${userDetails}. \n " +
                            "Generate a straightforward short encouraging message or advice to help them improve their fruit intake.")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(R.drawable.forum),
                contentDescription = "Motivational quote"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Motivational Message (AI)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState is UiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            val message = when (uiState) {
                is UiState.Success -> {
                    nutriCoachTip = (uiState as UiState.Success).outputText
                    nutriCoachTip
                }
                is UiState.Error -> (uiState as UiState.Error).errorMessage
                else -> "Press the button above to get a healthy tip!"
            }

            Text(
                text = message,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }

        // After the tip is generated, add it to the database
        LaunchedEffect(nutriCoachTip) {
            if (nutriCoachTip.isNotEmpty()) {
                // Check if the tip is already in the database, if not insert it
                nutriCoachTipsViewModel.insertNewTip(nutriCoachTip)
            }
        }

        // Show a toast when there's an error
        LaunchedEffect(uiState) {
            if (uiState is UiState.Error) {
                Toast.makeText(context, (uiState as UiState.Error).errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable

fun getTargetUserDetails(context: Context, targetUserID: String): String {
    try {
        val inputStream = context.assets.open("user_data.csv")
        val reader = inputStream.bufferedReader()

        val headers = reader.readLine()?.split(",") ?: return "CSV header missing."

        reader.useLines { lines ->
            lines.forEach { line ->
                val values = line.split(",")
                if (values.getOrNull(1) == targetUserID) {  // assuming userID is at index 1
                    val result = StringBuilder()
                    for (i in headers.indices) {
                        val header = headers.getOrNull(i) ?: continue
                        val value = values.getOrNull(i) ?: "N/A"
                        result.append("$header: $value\n")
                    }
                    return result.toString()
                }
            }
        }
        return "User ID $targetUserID not found."
    } catch (e: Exception) {
        return "Error reading CSV: ${e.message}"
    }
}


@Composable
fun AITipsModal(pastTips: List<String>, aiTips: List<String>, onDismiss: () -> Unit) {
    val combinedTips = (pastTips + aiTips).distinct()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)) // Dimmed background
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp)) // Modal card
                .padding(24.dp)
        ) {
            Text(
                text = "AI Tips",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                itemsIndexed(combinedTips) { _, tip ->
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = tip,
                            modifier = Modifier.padding(16.dp),
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Done")
            }
        }
    }
}


@Composable
fun NutrientRow(label: String, value: String) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("$label:")
            Text(value)
        }
    }
}


/**
 * These are the functions for the Settings Screen
 */


// Composable function for displaying the Settings screen.
@Composable
fun SettingPage(innerPadding: PaddingValues, userID: String, patientViewModel: PatientViewModel) {
    var showClinicianLogin by remember { mutableStateOf(false) }
    var showClinicianDashboard by remember { mutableStateOf(false) }

    if (showClinicianDashboard) {
        clinicianDashboard(
            onDone = {
                showClinicianDashboard = false
                showClinicianLogin = false
            } ,
            patientViewModel = patientViewModel)
    } else if (showClinicianLogin) {
        clinicianLogin(
            onBack = { showClinicianLogin = false },
            onLoginSuccess = { showClinicianDashboard = true }
        )
    }  else {
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Settings",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
            )

            // Account section
            accountInfo(userID)

            HorizontalDivider(modifier = Modifier.padding(8.dp))

            // Other settings section
            otherSettings(onClinicianLogin = {
                showClinicianLogin = true
            })
        }
    }
}
@Composable
fun otherSettings(onClinicianLogin: () -> Unit) {
    val context = LocalContext.current
    val sharedPrefLogin = context.getSharedPreferences("loggedIn", Context.MODE_PRIVATE)

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "OTHER SETTINGS", color = Color.DarkGray)

        settingsRow(icon = R.drawable.logout, "Logout") {

            // set loggedIn to false
            sharedPrefLogin.edit().putBoolean("loggedIn", false).apply()


            // Navigate to login page
            context.startActivity(Intent(context, WelcomePage::class.java))
        }

        settingsRow(icon = R.drawable.account_logo, "Clinician Login") {
            onClinicianLogin()
        }
    }
}

@Composable
fun settingsRow(icon: Int, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(R.drawable.forward_arrow),
            contentDescription = "Forward arrow",
            modifier = Modifier.size(16.dp),
        )
    }
}
@Composable
fun clinicianLogin(onBack: () -> Unit, onLoginSuccess: () -> Unit) {
    var clinicianKey by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onBack) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Clinician Login", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = clinicianKey,
            onValueChange = { clinicianKey = it },
            label = { Text("Clinician Key") },
            placeholder = { Text("Enter your clinician key") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (clinicianKey == "dollar-entry-apples") {
                    onLoginSuccess()
                } else {
                    Toast.makeText(context, "Incorrect clinicianKey", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logout),
                contentDescription = "Login icon",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Clinician Login")
        }
    }
}

@Composable
fun clinicianDashboard(onDone: () -> Unit, patientViewModel: PatientViewModel) {
    val genAIViewModel = viewModel<GenAIViewModel>()
    var avgMaleScore by remember { mutableStateOf(0f) }
    var avgFemaleScore by remember { mutableStateOf(0f) }

    val aiTips by genAIViewModel.aiTips.collectAsState()
    val uiState by genAIViewModel.uiState.collectAsState()

    val prompt = createPrompt()

    LaunchedEffect(Unit) {
        avgMaleScore = patientViewModel.getAverageHEIFAMale()
        avgFemaleScore = patientViewModel.getAverageHEIFAFemale()
    }


    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Clinician Dashboard",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        averageHEIFARow(label = "Average HEIFA (Male)", value = avgMaleScore)
        Spacer(modifier = Modifier.height(8.dp))

        averageHEIFARow(label = "Average HEIFA (Female)", value = avgFemaleScore)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {

                genAIViewModel.sendPrompt(prompt)
                genAIViewModel.sendPrompt(prompt)
                genAIViewModel.sendPrompt(prompt)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Find Pattern",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Find Data Pattern")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState is UiState.Loading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
        }

        dataCards(aiTips)

        Spacer(Modifier.height(8.dp))

        //"Done" button at the bottom
        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Done")
        }
    }
}

@Composable
fun dataCards(aiTips: List<String>){
    // Scrollable card list
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)

    ) {
        items(aiTips) { tip ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = tip,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}


@Composable
fun createPrompt(): String{
    val context = LocalContext.current
    val csvText = context.assets.open("user_data.csv")
        .bufferedReader()
        .use { it.readText() }

    val prompt = "$csvText\n\n Identify and write a key trend from the above data in around 30 words "
    return prompt
}
@Composable
fun averageHEIFARow(label: String, value: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label  :",
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = String.format("%.1f", value),
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun accountInfo(userID: String){

    // Retrieve userID and Phone Number
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE)
    val username = sharedPreferences.getString("USERNAME", "NULL")
    val phoneNumber = sharedPreferences.getString("PHONE_NUMBER", "000000000")

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text("ACCOUNT")

        Spacer(Modifier.height(16.dp))

        // Account phone number
        accountRow(icon = R.drawable.account_logo, text = username!!)
        accountRow(icon = R.drawable.phone_logo, text = phoneNumber!!)
        accountRow(icon = R.drawable.id_card, text = userID!!)

    }
}

@Composable
fun accountRow(icon : Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top=8.dp)
    ) {
        Icon(
            painter= painterResource(id = icon),
            contentDescription = "User information icon",
            modifier = Modifier
                .size(30.dp)
                .padding(end = 8.dp)
        )
        Text(text = text)
    }
}


/**
 * These are the functions for the Insights Screen
 */

@Composable
fun Heading(){
    Text(
        text = "Insights : Food Score",
        style = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        )
    )
}

// Composable function to retrieve the first row of the 'user_data.csv' file
@Composable
fun loadHeader(): List<String> {
    val context = LocalContext.current

    val inputStream = context.assets.open("user_data.csv") // Use assets folder
    val bufferedReader = inputStream.bufferedReader()
    val headerLine = bufferedReader.readLine()
    return headerLine.split(",")

}

// Composable function that structures the layout of all the sliders
@Composable
fun Sliders() {

    val userFoodScore : List<Float> = getTargetData()
    val foodCategories = listOf(
        "Vegetables",
        "Fruits",
        "Grainsandcereals",
        "Wholegrains",
        "Meatandalternatives",
        "Dairyandalternatives",
        "Water",
        "UnsaturatedFat",
        "Sodium",
        "Sugar",
        "Alcohol",
        "Discretionary"
    )

    val maxFoodScore : List<Int> = listOf(10, 10, 10, 10, 10, 10, 5, 10, 10, 10, 5, 10 )


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        for (i in 0 until foodCategories.size) {
            val foodScore = userFoodScore[i]
            val food = foodCategories[i]
            val maxScore = maxFoodScore[i]
            SliderWithLabel(food, foodScore,maxScore)
        }
    }
}

// Composable function that displays a slider and user's score side by side
@Composable
fun SliderWithLabel(label : String, displayValue : Float, maxValue : Int){


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = label,
            modifier = Modifier
                .weight(2f)
                .padding(end = 8.dp),
            style = TextStyle(
                fontSize = 14.sp
            )
        )

        Slider(
            value = displayValue,
            onValueChange = {},
            valueRange = 0f..maxValue.toFloat(),
            steps = maxValue-1, // Add tick marks
            enabled = false, // Disable modification
            modifier = Modifier
                .weight(3f)
                .height(8.dp)
        )

        // Display slider value on right of slider
        // SliderValue is float by default
        Text(
            text = "${displayValue}/${maxValue}",
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            style = TextStyle(
                fontSize = 14.sp
            )
        )
    }
}

// Retrieves user data on food categories
@Composable
fun getTargetData(): MutableList<Float> {

    val userDisplayData = mutableListOf<Float>()

    // Declare target headers in order of display
    val foodCategories = listOf(
        "VegetablesHEIFAscore",
        "FruitHEIFAscore",
        "GrainsandcerealsHEIFAscore",
        "WholegrainsHEIFAscore",
        "MeatandalternativesHEIFAscore",
        "DairyandalternativesHEIFAscore",
        "WaterHEIFAscore",
        "UnsaturatedFatHEIFAscore",
        "SodiumHEIFAscore",
        "SugarHEIFAscore",
        "AlcoholHEIFAscore",
        "DiscretionaryHEIFAscore"
    )

    // Retrieve user data
    val userData : List<String> = loadUserData()
    val userSex =  userData[2]

    // Retrieve heading names
    val headers : List<String> = loadHeader()

    // Iterate through target headers
    for (targetHeader in foodCategories) {

        val targetWithSex = targetHeader+userSex
        // Get index of target header
        val index = headers.indexOf(targetWithSex)
        val targetData = userData.get(index)
        userDisplayData.add(targetData.toFloat())
    }

    return userDisplayData
}


// Retrieves all of user's data
@Composable
fun loadUserData(): List<String> {

    val context = LocalContext.current

    // Retrieve userID and Phone Number
    val sharedPreferences = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE)
    // Providing default values in case values don't exist
    val userID = sharedPreferences.getString("USER_ID", "DefaultUser")

    val assets = context.assets // Get asset manager

    var userData = ""

    try {
        val inputStream = assets.open("user_data.csv")

        // Create a buffered reader for efficient reading
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.useLines {lines ->
            lines.drop(1).forEach { line ->
                val values = line.split(",")

                if (values[1] == userID) {
                    userData = line
                }
            }
        }
    } catch (e: Exception) {

        Toast.makeText(context, "No user found", Toast.LENGTH_LONG).show()

    }

    return userData.split(",")
}

// Retrieves the HEIFAScore based on user's gender
@Composable
fun getTotalFoodQualityScore(): Float {
    // Retrieve user data
    val userData = loadUserData()

    // Retrieve HEIFATotalScore
    val totalFoodQuality : Float

    if (userData[2] == "Male") {
        totalFoodQuality = userData[3].toFloat()
    } else {
        totalFoodQuality = userData[4].toFloat()
    }

    return totalFoodQuality
}

// Displays user's HEIFAscore on a slider and a display
@Composable
fun TotalFoodQuality() {

    val totalFoodQuality = getTotalFoodQualityScore()

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = "Total Food Quality Score",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                value = totalFoodQuality ,
                onValueChange = { },
                valueRange = 0f..100f,
                steps = 9, // Add tick marks
                enabled = false, // Prevent modification
                modifier = Modifier
                    .weight(3f)
                    .height(8.dp)
            )

            Text(
                text = "${totalFoodQuality}/100",
                modifier = Modifier.padding(start=8.dp)
            )
        }
    }
}

// Displays a button for user to share their HEIFAscore to others
@Composable
fun ShareButton(){

    val context = LocalContext.current

    val foodQualityScore = getTotalFoodQualityScore()

    val messageToShare = "Hey! I just got a HEIFA Score of ${foodQualityScore} ! :D"

    Button(
        onClick = {
            // create Intent to share score
            val shareIntent = Intent(ACTION_SEND)

            // Set data type to send
            shareIntent.type = "text/plain"

            // Set data to share
            shareIntent.putExtra(Intent.EXTRA_TEXT, messageToShare)

            // Start activity to share the text, with a chooser to select app
            context.startActivity(Intent.createChooser(shareIntent, "Share result via"))
        }
    ) {
        Text(
            text = "Share with Friends!",
            style = TextStyle(
                fontSize = 12.sp
            )
        )
    }
}

// Displays a button that navigates to the NutriCoach screen
@Composable
fun ImproveDietButton(navController: NavHostController) {

    Button(
        onClick = {navController.navigate("NutriCoach")}
    ) {
        Text(
            text = "Improve my diet",
            style = TextStyle(
                fontSize = 12.sp
            )
        )
    }
}


// Functions for Home Screen

@Composable
fun WelcomeHeading() {

    // Retrieving the data
    val context = LocalContext.current

    // Retrieve current userID and Phone Number
    val sharedPreferences = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE)
    // Providing default values in case values don't exist
    val userID = sharedPreferences.getString("USER_ID", "DefaultUser")

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Hello,",
            style = TextStyle(
                fontSize = 18.sp,
                color = Color.DarkGray
            ),
            modifier = Modifier.padding(bottom=8.dp)
        )

        Text(
            text = userID.toString(),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun EditQuestionnaire(){

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ){
        Text(
            text = "You've filled in your Food Intake Questionnaire, but you can change the details here:",
            modifier = Modifier.weight(1f)
        )

        // Button that allows users to edit their questionnaire
        Button(
            onClick = {context.startActivity(Intent(context, QuestionnairePage::class.java))}
        ) {
            Text(
                text = "Edit"
            )
        }
    }
}

@Composable
fun UserScore(navController: NavHostController){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "My Score",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )


        Button(
            onClick = {navController.navigate("Insight") }
        ) {
            Text("See All Scores")
        }

    }
}

fun getFoodQualityScore(context: Context, fileName: String, targetID: String): String {
    return try {
        val inputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.readLine() // Skip header

        reader.useLines { lines ->
            lines.forEach { line ->
                val values = line.split(",")
                val userID = values[1]
                val sex = values[2]

                if (userID == targetID) {
                    return if (sex == "Male") {
                        values[3]
                    } else {
                        values[4]
                    }
                }
            }
        }
        ""
    } catch (e: Exception) {
        Log.e("CSV", "Error reading food score: ${e.message}")
        ""
    }
}


@Composable
fun FoodQualityScore() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE)
    val userID = sharedPreferences.getString("USER_ID", "DefaultUser")

    Log.d("DEBUG", "Current userID = $userID")

    val userScore = getFoodQualityScore(context, "user_data.csv", userID!!)


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Your Food Score",
            style = TextStyle(fontSize = 18.sp),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = if (userScore.isNotBlank()) "$userScore/100" else "Loading...",
            style = TextStyle(fontSize = 20.sp, color = Color.Green)
        )
    }
}

@Composable
fun FoodQualityExplanation(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "What is Food Quality Score?",
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            ),
            modifier = Modifier.padding(bottom=8.dp)
        )

        Text(
            text = "Your Food Quality Score Provides a snapshot of how well your eating patterns align with established food guidelines, helping you identify both strengths and opportunities for improvement in your diet. \nThis personalized measurement considers various food groups including vegetables, fruits, whole grains, and proteins to give you practical insights for making healthier food choices.",
            style = TextStyle(
                lineHeight = 24.sp
            )
        )
    }
}