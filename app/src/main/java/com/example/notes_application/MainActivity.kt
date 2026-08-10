package com.example.notes_application

import android.R.attr.description
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.notes_application.NotesViewModel
import androidx.room.Room
import androidx.compose.runtime.collectAsState
import kotlin.jvm.java
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val database = remember {
                Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "notes_database"
                ).build()
            }
            val repository = NoteRepository(
                database.noteDao()
            )
            val factory = NotesViewModelFactory(repository)
            val navController = rememberNavController()
            val viewModel: NotesViewModel = viewModel(
                factory = factory
            )
            val notes by viewModel.notes.collectAsState()
            var selectedNote by remember {
                mutableStateOf<Note?>(null)
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
                            viewModel.deleteNote(note)
                        },
                        onEdit = {note ->
                            selectedNote = note
                            navController.navigate("add")
                        })
                }
                composable("add"){
                    AddNote(navController,
                        selectedNote = selectedNote,
                        onEditComplete = {
                        selectedNote = null
                    },
                        viewModel = viewModel)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController,
         notes: List<Note>,
         onDelete: (Note) -> Unit,
         onEdit: (Note) -> Unit
) {
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    var noteToDelete by remember {
        mutableStateOf<Note?>(null)
    }
    var searchText by remember {
        mutableStateOf("")
    }
    val filteredNotes = notes.filter {
        it.title.contains(searchText, ignoreCase = true) ||
                it.description.contains(searchText, ignoreCase = true)
    }

    if(showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text(
                    text = "Delete Note",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete \"${noteToDelete?.title}\"?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        noteToDelete?.let {
                            onDelete(it)
                        }
                        showDeleteDialog = false
                        noteToDelete = null
                    }
                ) {
                    Text(
                        text = "Delete",
                        color = Color.Red
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = "My Notes",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${notes.size} Notes",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 20.dp, horizontal = 5.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = {
                    Text("Search notes...")
                },
                singleLine = true
            )
            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Notes Available",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn() {
                    items(filteredNotes) { note ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            )
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
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Date",
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Spacer(
                                        modifier = Modifier.width(6.dp)
                                    )

                                    Text(
                                        text = note.date,
                                        fontSize = 14.sp
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {

                                    IconButton(
                                        onClick = {
                                            onEdit(note)
                                        },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit"
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            noteToDelete = note
                                            showDeleteDialog = true
                                        }
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
        }
    }
}


@Composable
fun AddNote(navController: NavController,
            selectedNote: Note?,
            onEditComplete: () -> Unit,
            viewModel: NotesViewModel){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope();
    var title by remember(selectedNote) {
        mutableStateOf(selectedNote?.title ?: "")
    }
    val currentDate = SimpleDateFormat(
        "dd MMMM yyyy",
        Locale.getDefault()
    ).format(Date())
    var des by remember(selectedNote) {
        mutableStateOf(selectedNote?.description ?: "")
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
                        }else{
                            if(selectedNote == null){
                                viewModel.addNote(
                                    Note(
                                    title = title,
                                    description = des,
                                    date = currentDate
                                )
                                )
                            }
                            else{
                                viewModel.updateNote(
                                    Note(
                                        id = selectedNote.id,
                                        title = title,
                                        description = des,
                                        date = currentDate
                                    )
                                )
                            }
                            onEditComplete()
                            navController.popBackStack()
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