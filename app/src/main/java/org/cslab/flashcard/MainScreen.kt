package org.cslab.flashcard

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.cslab.flashcard.ui.theme.FlashCardTheme

@Composable
fun MainScreen(navController: NavHostController) {
    // A list of the quiz text files in src/main/res/raw/
    val quizFiles = listOf(
        QuizFile(R.raw.current_affairs, "Current Affairs 2023"),
        QuizFile(R.raw.gre, "GRE Words")
    )
    Column {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 24.dp),
        ) {
            Text(
                text = "UniQuiz",
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Center
            )
        }

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
                            navController.navigate("show_quiz/${quizFile.id}")
                        }
                ) {
                    // Get name
                    Text(
                        text = quizFile.name,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreviewLight() {
    // Set the light theme
    FlashCardTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen(navController = rememberNavController())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreviewDark() {
    // Set the dark theme
    FlashCardTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen(navController = rememberNavController())
        }
    }
}
