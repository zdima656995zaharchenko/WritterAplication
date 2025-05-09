package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController, padding: PaddingValues) {
    Text(
        text = "Налаштування",
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    )
}
