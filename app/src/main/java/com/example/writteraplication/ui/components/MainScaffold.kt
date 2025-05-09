package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavController,
    screenTitle: String,
    projectId: Int? = null, // ← додано
    bottomBar: @Composable (() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Меню проєкту",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                DrawerItem("Головне меню") {
                    scope.launch { drawerState.close() }
                    navController.navigate("main_menu")
                }

                DrawerItem("Сюжети") {
                    scope.launch { drawerState.close() }
                    projectId?.let { id ->
                        navController.navigate("plots/$id")
                    }
                }

                DrawerItem("Персонажі") {
                    scope.launch { drawerState.close() }
                    projectId?.let { id ->
                        navController.navigate("characters/$id")
                    }
                }

                DrawerItem("Нотатки") {
                    scope.launch { drawerState.close() }
                    projectId?.let { id ->
                        navController.navigate("notes/$id")
                    }
                }

                DrawerItem("Хронологія") {
                    scope.launch { drawerState.close() }
                    projectId?.let { id ->
                        navController.navigate("timeline/$id")
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                DrawerItem("Проєкти") {
                    scope.launch { drawerState.close() }
                    navController.navigate("projects")
                }

                DrawerItem("Улюблені") {
                    scope.launch { drawerState.close() }
                    navController.navigate("favorites")
                }

                DrawerItem("Налаштування") {
                    scope.launch { drawerState.close() }
                    navController.navigate("settings")
                }

                DrawerItem("Профіль") {
                    scope.launch { drawerState.close() }
                    navController.navigate("profile")
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                DrawerItem("Вийти") {
                    scope.launch { drawerState.close() }
                    navController.navigate("login") {
                        popUpTo("main_menu") { inclusive = true }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(screenTitle) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Меню"
                            )
                        }
                    }
                )
            },
            bottomBar = { bottomBar?.invoke() },
            floatingActionButton = { floatingActionButton?.invoke() },
            content = content
        )
    }
}

@Composable
fun DrawerItem(label: String, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(label) },
        selected = false,
        onClick = onClick
    )
}
