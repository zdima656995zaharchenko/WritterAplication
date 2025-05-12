
package com.example.writteraplication.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.writteraplication.R
import com.example.writteraplication.viewmodel.SettingsViewModel
import androidx.compose.ui.platform.LocalContext


@Composable
fun SettingsScreen(viewModel: SettingsViewModel, padding: PaddingValues) {
    val isDarkTheme = viewModel.isDarkTheme.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(id = R.string.settings), style = MaterialTheme.typography.headlineSmall)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.dark_theme))
            Switch(
                checked = isDarkTheme.value,
                onCheckedChange = { viewModel.toggleTheme() }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.language_ukrainian))
            Switch(
                checked = viewModel.isUkrainian.collectAsState().value,
                onCheckedChange = { isChecked ->
                    val newLang = if (isChecked) "uk" else "en"
                    viewModel.setLanguage(newLang)
                    (context as? Activity)?.recreate() // Перезапуск для застосування мови
                }
            )
        }
    }
}
