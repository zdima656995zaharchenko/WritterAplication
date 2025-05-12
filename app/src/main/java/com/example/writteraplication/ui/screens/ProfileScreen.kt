
package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.writteraplication.viewmodel.UserProfileViewModel
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person

@Composable
fun ProfileScreen(navController: NavController, padding: PaddingValues) {
    val userProfileViewModel: UserProfileViewModel = viewModel()
    val userName by userProfileViewModel.userName.collectAsState()
    val email by userProfileViewModel.email.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier.size(96.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = userName ?: "Unknown",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = email ?: "Unknown",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
