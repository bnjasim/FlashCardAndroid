package org.cslab.flashcard

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

@Composable
fun DisplayQuizScreen(
    navController: NavHostController,
    resourceId: Int
) {
    // Retrieve the context
    val context = LocalContext.current

    // Read the contents of the text file
    val quizList = readTextFile(context, resourceId)

    // Display the Quiz as a Flash Card
    QuizFCard(quizList)

}
