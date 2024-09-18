package com.example.eac11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.eac11.ui.theme.EAC11Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EAC11Theme {
                // Setup Navigation
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "nameScreen") {
                    composable(route = "nameScreen") { NameScreen(navController = navController) }
                    composable(route = "ageScreen") { AgeScreen(navController = navController) }
                    composable(route = "destinationScreen") { DestinationScreen(navController = navController) }
                    composable(route = "summaryScreen") { SummaryScreen(navController = navController) }
                }
            }
        }
    }
}

@Composable
fun NameScreen(navController: NavController) {
    var nom by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = nom,
            onValueChange = { newText -> nom = newText },
            label = { Text("Nom") }
        )
        Button(onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set("nom", nom)
            navController.navigate("ageScreen")
                         },
            enabled = nom.isNotEmpty()
        ) {
            Text("Continuar")
        }
    }
}

@Composable
fun AgeScreen(navController: NavController) {
    var edat by remember { mutableStateOf("") }
    val nom = navController.previousBackStackEntry?.savedStateHandle?.get<String>("nom") ?: ""

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = edat,
            onValueChange = { newText -> edat = newText },
            label = { Text("Edat") }
        )
        Button(onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set("nom", nom)
            navController.currentBackStackEntry?.savedStateHandle?.set("edat", edat)
            navController.navigate("destinationScreen")
        },
                enabled = edat.isNotEmpty()
        ) {
            Text("Continuar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationScreen(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    var selectedDestination by remember { mutableStateOf("") }
    val destinations = listOf("Mart", "Lluna", "Júpiter", "Saturn")


    val nom = navController.previousBackStackEntry?.savedStateHandle?.get<String>("nom") ?: ""
    val edat = navController.previousBackStackEntry?.savedStateHandle?.get<String>("edat") ?: ""

    Column(modifier = Modifier.padding(16.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedDestination,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar destinació") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor()
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                destinations.forEach { destination ->
                    DropdownMenuItem(
                        text = { Text(destination) },
                        onClick = {
                            selectedDestination = destination
                            expanded = false
                        }
                    )
                }
            }
        }
        Button(onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set("nom", nom)
            navController.currentBackStackEntry?.savedStateHandle?.set("edat", edat)
            navController.currentBackStackEntry?.savedStateHandle?.set("selectedDestination", selectedDestination)
            navController.navigate("summaryScreen")
        },
                enabled = selectedDestination.isNotEmpty()
        ) {
            Text("Mostrar Resumen")
        }
    }
}

@Composable
fun SummaryScreen(navController: NavController) {
    // Retrieve values from savedStateHandle
    val nom = navController.previousBackStackEntry?.savedStateHandle?.get<String>("nom") ?: ""
    val edat = navController.previousBackStackEntry?.savedStateHandle?.get<String>("edat") ?: ""
    val selectedDestination = navController.previousBackStackEntry?.savedStateHandle?.get<String>("selectedDestination") ?: ""

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Resum:")
        Text(text = "Nom: $nom")
        Text(text = "Edat: $edat")
        Text(text = "Destinació Espacial: $selectedDestination")
        Button(onClick = { navController.popBackStack("nameScreen", inclusive = false) }) {
            Text("Iniciar de nuevo")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EAC11Theme {
        NameScreen(navController = rememberNavController())
    }
}
