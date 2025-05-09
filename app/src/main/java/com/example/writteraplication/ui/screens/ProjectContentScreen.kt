package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectContentScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    projectId: Int
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Вміст проєкту") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate("plots/$projectId") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сюжети")
            }

            Button(
                onClick = { navController.navigate("characters/$projectId") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Персонажі")
            }

            Button(
                onClick = { navController.navigate("notes/$projectId") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Нотатки")
            }

            Button(
                onClick = { navController.navigate("timeline/$projectId") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Хронологія")
            }
        }
    }
}
