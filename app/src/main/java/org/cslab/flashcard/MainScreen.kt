package org.cslab.flashcard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {
    // A list of the quiz text files in src/main/res/raw/
    val quizFiles = listOf(
        QuizFile(R.raw.current_affairs, "Current Affairs 2023"),
        QuizFile(R.raw.gre, "GRE Words")
    )
    // Display the Names as clickable Cards/Buttons
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(quizFiles) { quizFile ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        // Directly call the composable function for the details screen
                        DisplayQuizzes(quizFile.id)
                    }
            ) {
                // Get name
                Text(text = quizFile.name, color = Color.White)
            }
        }
    }
}
