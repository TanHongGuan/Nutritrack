package com.fit2081.HongGuan35100621

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import com.fit2081.HongGuan35100621.ui.theme.A1_35100621_TanHongGuanTheme
import com.fit2081.hongGuan_35100621_nutritrack.data.FoodIntake.FoodIntake
import com.fit2081.hongGuan_35100621_nutritrack.data.FoodIntake.FoodIntakeViewModel
import java.util.Calendar


class QuestionnairePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val foodIntakeViewModel: FoodIntakeViewModel = ViewModelProvider(this,
            FoodIntakeViewModel.FoodIntakeViewModelFactory(
                this@QuestionnairePage))[FoodIntakeViewModel::class.java]
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A1_35100621_TanHongGuanTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    {
                        TopBarMenu()
                    }

                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        val categorySelection = checkboxes(foodIntakeViewModel)

                        Persona()

                        HorizontalDivider(
                            modifier = Modifier.padding(8.dp)
                        )

                        val chosenPersona = choosePersona(foodIntakeViewModel)

                        val timePrefs = timings(foodIntakeViewModel)

                        // Save button which saves data and bring to HomePage
                        SaveButton(categorySelection, chosenPersona, timePrefs, foodIntakeViewModel)
                    }
                }
            }
        }
    }
}



// Composable function to display the topBarMenu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarMenu() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // The onBackPressedDispatcher is used to handle the back button press in the app
    val onBackPressedCallback = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher


    CenterAlignedTopAppBar(
        // color to customize appearance of TopAppBar
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        // Title displayed in center of app bar
        title = {
            Text(
                "Food Intake Questionnaire",
                maxLines = 1,
                // Ellipsis property used to truncate the text if it exceeds the available space
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    color = Color.DarkGray,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp)
            )
        },

        // Navigation icon (back button) with appropriate behavior
        navigationIcon = {
            IconButton(onClick = {
                // onBackPressDispatcher used to handle back button press in app
                // it takes current activity out of the back stack and shows the previous activity
                onBackPressedCallback?.onBackPressed()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to HomePage"
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

// Composable function to display all food categories checkboxes
@Composable
fun checkboxes(foodIntakeViewModel: FoodIntakeViewModel): MutableMap<String, Boolean> {
    val context = LocalContext.current
    val userID = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE)
        .getString("USER_ID", "Default User") ?: "Default User"

    var latestIntake by remember { mutableStateOf<FoodIntake?>(null) }
    LaunchedEffect(userID) {
        latestIntake = foodIntakeViewModel.getLatestFoodIntake(userID)

    }

    val foodCategories = listOf(
        "Fruits",
        "Red Meat",
        "Fish",
        "Vegetables",
        "Seafood",
        "Eggs",
        "Grains",
        "Poultry",
        "Nuts/Seeds")
    val stateOfCheckbox = remember(latestIntake) {
        mutableStateMapOf<String, Boolean>().apply {
            put("Fruits", latestIntake?.fruits ?: false)
            put("Vegetables", latestIntake?.vegetables ?: false)
            put("Grains", latestIntake?.grains ?: false)
            put("Red Meat", latestIntake?.redMeat ?: false)
            put("Seafood", latestIntake?.seafood ?: false)
            put("Poultry", latestIntake?.poultry ?: false)
            put("Fish", latestIntake?.fish ?: false)
            put("Eggs", latestIntake?.eggs ?: false)
            put("Nuts/Seeds", latestIntake?.nutsOrseeds ?: false)
        }
    }


    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Tick all the food categories you can eat", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (i in 0 until 3) {
                Column {
                    for (j in 0 until 3) {
                        val index = i * 3 + j
                        val label = foodCategories[index]
                        CheckboxWithLabel(label, stateOfCheckbox)
                    }
                }
            }
        }
    }

    return stateOfCheckbox
}

// Composable function to display checkboxes with their corresponding label
@Composable
fun CheckboxWithLabel(label : String, checkedMap: MutableMap<String, Boolean>) {

    val checked = checkedMap[label]

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (checked != null) {
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checkedMap[label] = it
                }
            )
        }
        Text(text = label)
    }
}

// Composable function for layout of Persona Selection Section
@Composable
fun Persona() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
    {
        Text(
            text = "Your Persona",
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            ),
            modifier = Modifier.padding(bottom=4.dp)
        )

        Text(
            text = "People can be broadly classified into 6 different types based opn their eating preferences. Click on each button below to find out the different types, and select the type that best fits you.",
            style = TextStyle(
                fontSize = 14.sp
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        PersonaButtons()

    }
}

// Composable function to display all personas as buttons for users to view each description
@Composable
fun PersonaButtons() {

    val persona1 = painterResource(id = R.drawable.persona1)
    val caption1 = "I’m passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy.\n"

    val persona2 = painterResource(id = R.drawable.persona2)
    val caption2 = "I’m health-conscious and being healthy and eating healthy is important to me. Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. I look for new recipes and healthy eating information on social media.\n"

    val persona3 = painterResource(id = R.drawable.persona3)
    val caption3 = "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go.\n"

    val persona4 = painterResource(id = R.drawable.persona4)
    val caption4 = "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go.\n"

    val persona5 = painterResource(id = R.drawable.persona5)
    val caption5 = "I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics about what it means to be healthy, but it doesn’t seem relevant to me right now. I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life.\n"

    val persona6 = painterResource(id = R.drawable.persona6)
    val caption6 = "I’m not bothered about healthy eating. I don’t really see the point and I don’t think about it. I don’t really notice healthy eating tips or recipes and I don’t care what I eat."

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        ) {
            PersonaButton("Health Devotee", caption1, persona1)
            PersonaButton("Mindful Eater", caption2, persona2)
            PersonaButton("Wellness Striver", caption3, persona3)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            PersonaButton("Balance Seeker", caption4, persona4)
            PersonaButton("Health Procrastinator", caption5, persona5)
            PersonaButton("Food Carefree", caption6, persona6)
        }

    }
}

// Composable function to display a button that when pressed displays a modal containing a picture and description
@Composable
fun PersonaButton(name: String, caption: String, image: Painter) {

    var showDialog by remember { mutableStateOf(false) }
    Button(
        onClick = { showDialog = true },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    )
    {
        Text(
            text = name,
            style = TextStyle(
                fontSize = 11.sp
            )
        )
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Persona Image
                    Image(
                        painter = image,
                        contentDescription = "$name Image",
                        modifier = Modifier
                            .size(100.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Persona Title
                    Text(
                        text = name,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Persona Caption
                    Text(
                        text = caption,
                        style = TextStyle(
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

// Composable function with a dropdown text field for user to select a persona
@Composable
fun choosePersona(foodIntakeViewModel: FoodIntakeViewModel): String {
    val context = LocalContext.current
    val userID = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE)
        .getString("USER_ID", "Default User") ?: "Default User"

    var latestIntake by remember { mutableStateOf<FoodIntake?>(null) }
    LaunchedEffect(userID) {
        latestIntake = foodIntakeViewModel.getLatestFoodIntake(userID)
    }

    var selectedPersona by remember(latestIntake) { mutableStateOf(latestIntake?.persona ?: "Select Option") }
    val personaList = listOf("Health Devotee", "Mindful Eater", "Wellness Striver", "Balance Seeker", "Health Procrastinator", "Food Carefree")

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Which persona best fits you?", fontWeight = FontWeight.SemiBold)
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            OutlinedTextField(
                value = selectedPersona,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth().clickable { expanded = true },
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Show dropdown")
                    }
                },
                textStyle = TextStyle(fontSize = 14.sp)
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                personaList.forEach { persona ->
                    DropdownMenuItem(text = { Text(persona) }, onClick = {
                        selectedPersona = persona
                        expanded = false
                    })
                }
            }
        }
    }

    return selectedPersona
}



// Composable function to display the timeInput() UI
@Composable
fun timings(foodIntakeViewModel: FoodIntakeViewModel): MutableMap<String, String> {
    val context = LocalContext.current
    val userID = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE)
        .getString("USER_ID", "Default User") ?: "Default User"

    var latestIntake by remember { mutableStateOf<FoodIntake?>(null) }
    LaunchedEffect(userID) {
        latestIntake = foodIntakeViewModel.getLatestFoodIntake(userID)
    }

    val timeMap = remember(latestIntake) {
        mutableStateMapOf(
            "What time of day approx. do you normally eat your biggest meal?" to (latestIntake?.biggestMeal ?: "00:00"),
            "What time of day approx. do you go to sleep at night?" to (latestIntake?.sleepTime ?: "00:00"),
            "What time of day approx. do you wake up in the morning?" to (latestIntake?.wakeTime ?: "00:00")
        )
    }

    Column(modifier = Modifier.padding(8.dp)) {
        Text("Timings", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        timeMap.keys.forEach { question ->
            TimeInput(question, timeMap)
        }
    }

    return timeMap
}


// Composable function to display the question and allow user to select a time from time dialog
@Composable
fun TimeInput(question: String, questionMap: MutableMap<String, String>) {
    var showTimePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = question,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            style = TextStyle(fontSize = 14.sp)
        )

        // ✅ OutlinedButton instead of TextField
        OutlinedButton(
            onClick = { showTimePicker = true },
            modifier = Modifier
                .width(80.dp)
                .height(40.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = questionMap[question] ?: "00:00",
                style = TextStyle(fontSize = 12.sp, textAlign = TextAlign.Center)
            )
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, selectedHour: Int, selectedMinute: Int ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                questionMap[question] = selectedTime
                showTimePicker = false
            },
            hour, minute, false
        ).show()
    }
}



// Composable function to display save button and to save questionnaire answers
@Composable
fun SaveButton(
    foodCategories: MutableMap<String, Boolean>,
    chosenPersona: String,
    timePrefs: MutableMap<String, String>,
    foodIntakeViewModel: FoodIntakeViewModel
) {
    val context = LocalContext.current
    val userID = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE)
        .getString("USER_ID", "Default User") ?: "Default User"

    Button(
        onClick = {

            val foodIntake = FoodIntake(
                userID = userID,
                fruits = foodCategories["Fruits"] ?: false,
                vegetables = foodCategories["Vegetables"] ?: false,
                grains = foodCategories["Grains"] ?: false,
                redMeat = foodCategories["Red Meat"] ?: false,
                seafood = foodCategories["Seafood"] ?: false,
                poultry = foodCategories["Poultry"] ?: false,
                fish = foodCategories["Fish"] ?: false,
                eggs = foodCategories["Eggs"] ?: false,
                nutsOrseeds = foodCategories["Nuts/Seeds"] ?: false,
                persona = chosenPersona,
                biggestMeal = timePrefs["What time of day approx. do you normally eat your biggest meal?"] ?: "",
                sleepTime = timePrefs["What time of day approx. do you go to sleep at night?"] ?: "",
                wakeTime = timePrefs["What time of day approx. do you wake up in the morning?"] ?: ""
            )
            foodIntakeViewModel.insert(foodIntake)

            context.startActivity(Intent(context, BottomNavigationBar::class.java))
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Save")
    }
}