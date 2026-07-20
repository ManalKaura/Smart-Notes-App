package com.example.notes_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

data class Note(
    val title : String,
    val description : String,
    val date : String
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val notes = remember {
                mutableStateListOf(
                    Note(
                        title = "Android Notes",
                        description = "Learn Navigation",
                        date = "15 July"
                    ),
                    Note(
                        title = "Shopping",
                        description = "Buy Keyboard",
                        date = "16 July"
                    ),
                    Note(
                        title = "DSA",
                        description = "Binary Search",
                        date = "17 July"
                    ),
                    Note(
                        title = "College",
                        description = "Submit Assignment",
                        date = "18 July"
                    )
                )
            }
            NavHost(
                navController = navController,
                startDestination = "login"
            ){
                composable("login"){
                    Login(navController)
                }
                composable ("home"){
                    Home(navController,notes = notes,
                        onDelete = {note ->
                            notes.remove(note)
                        })
                }
                composable("add"){
                    AddNote(navController,notes = notes)
                }
            }
        }
    }
}

@Composable
fun Login(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .padding(15.dp)
                .height(500.dp)
                .width(300.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(35.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Welcome Back",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
                Text(
                    text = "Sign in to continue",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                var mail by remember {
                    mutableStateOf("")
                }
                TextField(
                    value = mail, onValueChange = {
                        mail = it
                    },
                    label = { Text("Enter Your Mail:")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    )
                )
                if (mail.isNotEmpty()&&(!mail.contains("@") || !mail.contains("."))
                ) {
                    Text(
                        text = "Invalid Email",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                var passwordVisible by remember {
                    mutableStateOf(false)
                }
                var pass by remember {
                    mutableStateOf("")
                }
                TextField(value = pass , onValueChange = {
                    pass = it
                },
                    label = {
                        Text("Enter your password: ")
                    },
                    visualTransformation =
                        if(passwordVisible){
                            VisualTransformation.None
                        }
                        else{
                            PasswordVisualTransformation()
                        },
                    trailingIcon = {
                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(
                                imageVector =
                                    if (passwordVisible)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,

                                contentDescription = "Password Visibility"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )
                if (pass.isNotEmpty() && pass.length < 6) {
                    Text(
                        text = "Password must be at least 6 characters",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(onClick = {
                        navController.navigate("home")
                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        enabled = mail.contains("@") && mail.contains(".") && pass.length >=6) {
                        Text("Login")
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text("Forgot Password?")
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Row{
                                Text("Don't have an account?")
                                Spacer(modifier = Modifier.weight(1f))
                                Text("Register")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Home(navController: NavController,
         notes: SnapshotStateList<Note>,
         onDelete: (Note) -> Unit
){
    Box(
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 50.dp, horizontal = 5.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "My Notes",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn() {
                items(notes) { note ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 20.dp
                            )
                        ) {
                            Text(
                                text = note.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = note.description,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = note.date,
                                fontSize = 14.sp
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(
                                    onClick = {onDelete(note)}
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete Note"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(onClick = {
            navController.navigate("add")
        },
            Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 16.dp, vertical = 20.dp)){
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Note"
            )
        }
    }
}

@Composable
fun AddNote(navController: NavController,
            notes: SnapshotStateList<Note>){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope();
    var title by remember {
        mutableStateOf("")
    }
    var des by remember {
        mutableStateOf("")
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 55.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Add Note",
                fontSize = 30.sp,
                color = Color.LightGray,
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Title",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            TextField(value = title, onValueChange = {
                title = it
            }
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Description",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            TextField(
                value = des, onValueChange = {
                    des = it
                },
                modifier = Modifier.height(150.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        if (title.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Title is missing")
                            }
                        } else if (des.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Description is missing")
                            }
                        } else {
                            notes.add(
                                Note(
                                    title = title,
                                    description = des,
                                    date = "19 July"
                                )
                            )
                            title = ""
                            des = ""
                            navController.navigate("home")
                        }
                    },
                    Modifier.width(150.dp)
                ) {
                    Text("Save")
                }
            }
        }
    }
}