
package com.example.writteraplication.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.writteraplication.data.repository.PlotRepository
import com.example.writteraplication.data.repository.CharacterRepository
import com.example.writteraplication.data.repository.FirebaseCharacterRepository
import com.example.writteraplication.data.repository.NoteRepository
import com.example.writteraplication.data.repository.FirebaseNoteRepository
import com.example.writteraplication.data.repository.FirebasePlotRepository
import com.example.writteraplication.data.repository.FirebaseTimelineRepository
import com.example.writteraplication.data.repository.FirebaseProjectRepository
import com.example.writteraplication.data.repository.ProjectRepository
import com.example.writteraplication.data.repository.TimelineRepository
import com.example.writteraplication.ui.screens.*
import com.example.writteraplication.viewmodel.PlotsViewModel
import com.example.writteraplication.viewmodel.PlotsViewModelFactory
import com.example.writteraplication.viewmodel.CharacterViewModel
import com.example.writteraplication.viewmodel.CharacterViewModelFactory
import com.example.writteraplication.viewmodel.NoteViewModel
import com.example.writteraplication.viewmodel.NoteViewModelFactory
import com.example.writteraplication.viewmodel.TimelineViewModel
import com.example.writteraplication.viewmodel.TimelineViewModelFactory
import com.example.writteraplication.viewmodel.SharedProjectViewModel
import com.example.writteraplication.viewmodel.SettingsViewModel
import com.example.writteraplication.ui.components.MainScaffold
import com.example.writteraplication.viewmodel.ProjectViewModel
import com.example.writteraplication.viewmodel.ProjectViewModelFactory


@Composable
fun AppNavigation(
    plotRepository: PlotRepository,
    firebasePlotRepository: FirebasePlotRepository,
    characterRepository: CharacterRepository,
    firebaseCharacterRepository: FirebaseCharacterRepository,
    noteRepository: NoteRepository,
    projectRepository: ProjectRepository,
    firebaseProjectRepository: FirebaseProjectRepository,
    firebaseRepository: FirebaseNoteRepository,
    timelineRepository: TimelineRepository,
    firebaseTimelineRepository: FirebaseTimelineRepository,
    settingsViewModel: SettingsViewModel,
    navController: NavHostController = rememberNavController()
) {
    val plotsViewModel: PlotsViewModel = viewModel(factory = PlotsViewModelFactory(plotRepository,firebasePlotRepository))
    val characterViewModel: CharacterViewModel = viewModel(factory = CharacterViewModelFactory(characterRepository, firebaseCharacterRepository))
    val noteViewModel: NoteViewModel = viewModel(factory = NoteViewModelFactory(noteRepository, firebaseRepository ))
    val timelineViewModel: TimelineViewModel = viewModel(factory = TimelineViewModelFactory(timelineRepository, firebaseTimelineRepository))
    val projectViewModel: ProjectViewModel = viewModel(factory = ProjectViewModelFactory(projectRepository, firebaseProjectRepository))
    val sharedProjectViewModel: SharedProjectViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onRegisterClick = { navController.navigate("register") },
                onForgotPasswordClick = { navController.navigate("forgot_password") },
                onChangePasswordClick = { navController.navigate("change_password") },
                onLoginSuccess = { navController.navigate("main_menu") }
            )
        }

        composable("register") {
            RegisterScreen(
                onLoginClick = { navController.navigate("login") },
                onBackToMainMenu = { navController.navigateUp() }
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(onBackToLogin = { navController.navigateUp() })
        }

        composable("change_password") {
            ChangePasswordScreen(onBackToProfile = { navController.navigateUp() })
        }

        composable("main_menu") {
            MainMenuScreen(navController = navController)
        }

        composable("project_editor") {
            MainScaffold(navController = navController, screenTitle = "Редактор проєкту") { padding ->
                ProjectEditorScreen(navController = navController, padding = padding)
            }
        }

        composable("project_content/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull()
            if (projectId != null) {
                sharedProjectViewModel.currentProjectId = projectId
                MainScaffold(
                    navController = navController,
                    screenTitle = "Вміст проєкту",
                    projectId = projectId
                ) { padding ->
                    ProjectContentScreen(
                        navController = navController,
                        paddingValues = padding,
                        projectId = projectId
                    )
                }
            }
        }

        composable("plots/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull()
            if (projectId != null) {
                sharedProjectViewModel.currentProjectId = projectId
                MainScaffold(
                    navController = navController,
                    screenTitle = "Сюжети",
                    projectId = projectId
                ) { padding ->
                    PlotScreen(
                        modifier = Modifier.padding(padding),
                        plotsViewModel = plotsViewModel,
                        projectId = projectId,
                        onPlotClick = { plotId ->
                            navController.navigate("plot_details/$plotId")
                        }
                    )
                }
            }
        }

        composable("plot_details/{plotId}") { backStackEntry ->
            val plotId = backStackEntry.arguments?.getString("plotId")?.toIntOrNull()
            if (plotId != null) {
                MainScaffold(navController = navController, screenTitle = "Деталі сюжету") { padding ->
                    PlotDetailsScreen(
                        plotId = plotId,
                        plotsViewModel = plotsViewModel,
                        onBack = { navController.popBackStack() },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }

        composable("characters/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull()
            if (projectId != null) {
                sharedProjectViewModel.currentProjectId = projectId
                MainScaffold(
                    navController = navController,
                    screenTitle = "Персонажі",
                    projectId = projectId
                ) { padding ->
                    CharacterScreen(
                        characterViewModel = characterViewModel,
                        projectId = projectId,
                        onCharacterClick = { id ->
                            navController.navigate("character_details/$id/$projectId")
                        }
                    )
                }
            }
        }

        composable("character_details/{id}/{projectId}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull()
            if (characterId != null && projectId != null) {
                MainScaffold(
                    navController = navController,
                    screenTitle = if (characterId == -1) "Новий персонаж" else "Деталі персонажа",
                    projectId = projectId
                ) { padding ->
                    CharacterDetailsScreen(
                        characterId = characterId,
                        projectId = projectId,
                        characterViewModel = characterViewModel,
                        onBack = { navController.popBackStack() },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }

        composable("notes/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull()
            if (projectId != null) {
                sharedProjectViewModel.currentProjectId = projectId
                MainScaffold(
                    navController = navController,
                    screenTitle = "Нотатки",
                    projectId = projectId
                ) { padding ->
                    NoteScreen(
                        modifier = Modifier.padding(padding),
                        noteViewModel = noteViewModel,
                        projectId = projectId,
                        onNoteClick = { noteId ->
                            navController.navigate("note_details/$noteId/$projectId")
                        }
                    )
                }
            }
        }

        composable("note_details/{noteId}/{projectId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull()
            if (noteId != null && projectId != null) {
                MainScaffold(
                    navController = navController,
                    screenTitle = if (noteId == -1) "Нова нотатка" else "Деталі нотатки",
                    projectId = projectId
                ) { padding ->
                    NoteDetailsScreen(
                        noteId = noteId,
                        projectId = projectId,
                        noteViewModel = noteViewModel,
                        onBack = { navController.popBackStack() },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }

        composable("timeline/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull()
            if (projectId != null) {
                sharedProjectViewModel.currentProjectId = projectId
                MainScaffold(
                    navController = navController,
                    screenTitle = "Хронологія",
                    projectId = projectId
                ) { padding ->
                    TimelineScreen(
                        timelineViewModel = timelineViewModel,
                        projectId = projectId,
                        onTimelineClick = { id ->
                            navController.navigate("timeline_details/$id/$projectId")
                        },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }

        composable("timeline_details/{id}/{projectId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull()
            if (id != null && projectId != null) {
                MainScaffold(
                    navController = navController,
                    screenTitle = if (id == -1) "Нова подія" else "Деталі події",
                    projectId = projectId
                ) { padding ->
                    TimelineDetailsScreen(
                        context = LocalContext.current,
                        id = id,
                        projectId = projectId,
                        timelineViewModel = timelineViewModel,
                        onBack = { navController.popBackStack() },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }

        composable("projects") {
            MainScaffold(navController = navController, screenTitle = "Проєкти", projectId = sharedProjectViewModel.currentProjectId) { padding ->
                ProjectsScreen(navController = navController, padding = padding, firebaseProjectRepository = firebaseProjectRepository)
            }
        }

        composable("favorites") {
            MainScaffold(navController = navController, screenTitle = "Улюблені", projectId = sharedProjectViewModel.currentProjectId) { padding ->
                FavoritesScreen(navController = navController, padding = padding, firebaseProjectRepository = firebaseProjectRepository )
            }
        }

        composable("settings") {
            MainScaffold(navController = navController, screenTitle = "Налаштування", projectId = sharedProjectViewModel.currentProjectId) { padding ->
                SettingsScreen(viewModel = settingsViewModel, padding = padding)
            }
        }

        composable("profile") {
            MainScaffold(navController = navController, screenTitle = "Профіль", projectId = sharedProjectViewModel.currentProjectId) { padding ->
                ProfileScreen(navController = navController, padding = padding)
            }
        }
    }
}