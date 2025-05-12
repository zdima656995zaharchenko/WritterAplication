
package com.example.writteraplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.writteraplication.R
import com.example.writteraplication.viewmodel.SettingsViewModel

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
                    text = stringResource(R.string.menu_title),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                DrawerItem(stringResource(R.string.menu_home)) {
                    scope.launch { drawerState.close() }
                    navController.navigate("main_menu")
                }

                DrawerItem(stringResource(R.string.menu_plots)) {
                    scope.launch { drawerState.close() }
                    projectId?.let { id ->
                        navController.navigate("plots/$id")
                    }
                }

                DrawerItem(stringResource(R.string.menu_characters)) {
                    scope.launch { drawerState.close() }
                    projectId?.let { id ->
                        navController.navigate("characters/$id")
                    }
                }

                DrawerItem(stringResource(R.string.menu_notes)) {
                    scope.launch { drawerState.close() }
                    projectId?.let { id ->
                        navController.navigate("notes/$id")
                    }
                }

                DrawerItem(stringResource(R.string.menu_timeline)) {
                    scope.launch { drawerState.close() }
                    projectId?.let { id ->
                        navController.navigate("timeline/$id")
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                DrawerItem(stringResource(R.string.menu_projects)) {
                    scope.launch { drawerState.close() }
                    navController.navigate("projects")
                }

                DrawerItem(stringResource(R.string.menu_favorites)) {
                    scope.launch { drawerState.close() }
                    navController.navigate("favorites")
                }

                DrawerItem(stringResource(R.string.menu_settings)) {
                    scope.launch { drawerState.close() }
                    navController.navigate("settings")
                }

                DrawerItem(stringResource(R.string.menu_profile)) {
                    scope.launch { drawerState.close() }
                    navController.navigate("profile")
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                DrawerItem(stringResource(R.string.menu_logout)) {
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
                                contentDescription = stringResource(R.string.menu_title)
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
