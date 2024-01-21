package org.cslab.flashcard

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

@Composable
fun DisplayQuizScreen(
    navController: NavHostController,
    resourceId: Int
) {
    // Retrieve the context
    val context = LocalContext.current

    // Read the contents of the text file
    val quizList = readTextFile(context, resourceId)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(quizList) { quiz ->
            QuizItem(quiz = quiz)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QuizItem(quiz: Pair<String, String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Display question as the first row
            Text(text = "Question: ${quiz.first}", color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(8.dp))

            // Display answer as the second row
            Text(text = "Answer: ${quiz.second}", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}


/**
 * Function to read the contents of a text file from the resources.
 */
fun readTextFile(context: Context, resourceId: Int): MutableList<Pair<String, String>> {
    val inputStream: InputStream = context.resources.openRawResource(resourceId)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val quizList: MutableList<Pair<String, String>> = mutableListOf()
    var line: String?
    while (true) {
        line = reader.readLine()
        if (line == null) break
        if (!line.contains('\t')) {
            Log.i("myTag", "The line does not contain a tab character.")
            continue
        }
        val parts = line.split('\t')
        if (parts.size > 2) {
            Log.i("myTag", "The line contains multiple tab characters.")
            continue
        }
        // Add the question answer pair to the list of quizzes
        val question = parts[0].trim()
        val answer = parts[1].trim()
        if (question == "" || answer == "") {
            Log.i("myTag", "Empty question or answer.")
            continue
        }
        quizList.add(question to answer)
    }
    reader.close()
    return quizList
}

@Preview(showBackground = true)
@Composable
fun QuizListPreview() {
    val previewQuizList = listOf(
        Pair("Preview Question 1", "Preview Answer 1"),
        Pair("Preview Question 2", "Preview Answer 2"),
        Pair("Preview Question 3", "Preview Answer 3")
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(previewQuizList) { quiz ->
            QuizItem(quiz = quiz)
        }
    }
}

