package org.cslab.flashcard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlin.random.Random

@Composable
fun DisplayQuizScreen(
    navController: NavHostController,
    resourceId: Int
) {
    // Retrieve the context
    val context = LocalContext.current

    // Read the contents of the text file
    val quizList = readTextFile(context, resourceId)

    // check if the quiz list is empty
    if (quizList.size == 0) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(150.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(top=40.dp)
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Icon(Icons.Default.Warning, contentDescription = "")
                    Text(
                        text = "The file is empty/badly formatted",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }
            }
        }
    }
    else {
        // Randomly pick a quiz as the first one to display
        val quizIndex = Random.nextInt(quizList.size)
        // Display the Quiz as a Flash Card
        QuizFCard(quizList, quizIndex)
    }
}
