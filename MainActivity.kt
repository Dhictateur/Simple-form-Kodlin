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
                // Añadimos el navegador para movernos entre las cuatro pantallas
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "nameScreen") {
                    // Ponemos variables ID para ejecutar cada pantalla
                    composable(route = "nameScreen") { NameScreen(navController = navController) }
                    composable(route = "ageScreen") { AgeScreen(navController = navController) }
                    composable(route = "destinationScreen") { DestinationScreen(navController = navController) }
                    composable(route = "summaryScreen") { SummaryScreen(navController = navController) }
                }
            }
        }
    }
}

// Pantalla para escribir el nombre
@Composable
fun NameScreen(navController: NavController) {
    // Creamos variable mutableState
    var nom by remember { mutableStateOf("") }

    // En la columna añadimos el espacio para escribir
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = nom,
            onValueChange = { newText -> nom = newText },
            label = { Text("Nom") }
        )

        // Accion cuando se pulsa el boton
        Button(onClick = {
            // Guardamos el valor introducido de nombre
            navController.currentBackStackEntry?.savedStateHandle?.set("nom", nom)
            // Navegamos a la siguiente pantalla pulsando el boton
            navController.navigate("ageScreen")
                         },
            // Solo esta activo si no esta vacio
            enabled = nom.isNotEmpty()
        ) {
            Text("Continuar")
        }
    }
}

//Pantalla de la edad
@Composable
fun AgeScreen(navController: NavController) {
    var edat by remember { mutableStateOf("") }
    //Recordamos la variable de la anterior pantalla
    val nom = navController.previousBackStackEntry?.savedStateHandle?.get<String>("nom") ?: ""

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = edat,
            onValueChange = { newText -> edat = newText },
            label = { Text("Edat") }
        )
        Button(onClick = {
            // Guardamos los dos valores porque sino perderiamos el nombre
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

// Pantalla desplegable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationScreen(navController: NavController) {
    // Variable para verificar si se desplega
    var expanded by remember { mutableStateOf(false) }
    var selectedDestination by remember { mutableStateOf("") }
    // Lista de los destinos
    val destinations = listOf("Mart", "Lluna", "Júpiter", "Saturn")

    //Recordamos las variables de las pantallas anteriores
    val nom = navController.previousBackStackEntry?.savedStateHandle?.get<String>("nom") ?: ""
    val edat = navController.previousBackStackEntry?.savedStateHandle?.get<String>("edat") ?: ""


    Column(modifier = Modifier.padding(16.dp)) {
        // Desplegable
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedDestination,
                onValueChange = {},
                // Solo se puede elegir
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
                // Cogemos la lista para que se introduzca el valor que pulsamos en la varible del texto
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
            // Guardamos todos los valores
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

//Pantalla del resumen
@Composable
fun SummaryScreen(navController: NavController) {
    // Recuperamos los valores introducidos en cada pantalla por su identificador String
    val nom = navController.previousBackStackEntry?.savedStateHandle?.get<String>("nom") ?: ""
    val edat = navController.previousBackStackEntry?.savedStateHandle?.get<String>("edat") ?: ""
    val selectedDestination = navController.previousBackStackEntry?.savedStateHandle?.get<String>("selectedDestination") ?: ""

    //Mostramos los datos a partir de las varaibles
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Resum:")
        Text(text = "Nom: $nom")
        Text(text = "Edat: $edat")
        Text(text = "Destinació Espacial: $selectedDestination")

        //Boton para resetear el formulario y sobreescribir los datos
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
