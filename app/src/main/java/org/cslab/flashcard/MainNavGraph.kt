package org.cslab.flashcard

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main_screen") {
        // Main Screen
        composable("main_screen") {
            MainScreen(navController = navController)
        }
        // Second Screen to display the Quiz
        composable(
            route = "show_quiz/{resourceId}",
            arguments = listOf(
                navArgument("resourceId") {
                    type = NavType.IntType
                })
        ) {
            var resourceId = it.arguments?.getInt("resourceId")
            Log.i("myTag", resourceId.toString())
            // if no argument is passed, set a default resource
            if (resourceId == null) {
                resourceId = R.raw.current_affairs
            }
            DisplayQuizScreen(navController = navController, resourceId)
        }
    }
}