package com.example.keyboard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.keyboard.USBConnectionViewModel

@Composable
fun HomeScreen(navController: NavHostController, viewModel: USBConnectionViewModel) {
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Saisissez du texte") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.transmitData(inputText) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Transmettre")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("config") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Paramètres")
        }
    }
}


