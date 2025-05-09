package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.writteraplication.local.model.PlotEntity
import com.example.writteraplication.viewmodel.PlotsViewModel

@Composable
fun PlotDetailsScreen(
    plotId: Int,
    plotsViewModel: PlotsViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var plot by remember { mutableStateOf<PlotEntity?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var cause by remember { mutableStateOf("") }
    var consequence by remember { mutableStateOf("") }
    var relatedCharacters by remember { mutableStateOf("") }

    LaunchedEffect(plotId) {
        plotsViewModel.getPlotById(plotId) { loadedPlot ->
            plot = loadedPlot
            loadedPlot?.let {
                title = it.title
                description = it.description
                eventDate = it.eventDate.orEmpty()
                cause = it.cause.orEmpty()
                consequence = it.consequence.orEmpty()
                relatedCharacters = it.relatedCharacters.orEmpty()
            }
        }
    }

    plot?.let { currentPlot ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Деталі сюжету",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Назва") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Опис") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = eventDate,
                onValueChange = { eventDate = it },
                label = { Text("Дата подій (опціонально)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cause,
                onValueChange = { cause = it },
                label = { Text("Що призвело до подій") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = consequence,
                onValueChange = { consequence = it },
                label = { Text("Наслідки подій") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = relatedCharacters,
                onValueChange = { relatedCharacters = it },
                label = { Text("Пов’язані персонажі") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(
                    onClick = {
                        val updatedPlot = currentPlot.copy(
                            title = title,
                            description = description,
                            eventDate = eventDate.ifBlank { null },
                            cause = cause.ifBlank { null },
                            consequence = consequence.ifBlank { null },
                            relatedCharacters = relatedCharacters.ifBlank { null }
                        )
                        plotsViewModel.updatePlot(updatedPlot)
                        onBack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Зберегти")
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Назад")
                }
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

