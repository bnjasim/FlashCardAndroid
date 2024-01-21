package org.cslab.flashcard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cslab.flashcard.ui.theme.FlashCardTheme
import kotlin.random.Random

@Composable
fun QuizFCard(quizList: List<Pair<String, String>>) {
    // Total number of quizzes
    val totalNumQuiz = quizList.size
    // A weight of 2 is assigned to each quiz initially.
    // The weight is reduced by 1 when the quiz is answered correctly.
    // The weight is reduced by 2 if the quiz is skipped.
    var quizWeights = MutableList(totalNumQuiz) { 2 }

    // Define compose state variables
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    // keep track of percentage/accuracy 0 to 1 (not percentage)
    var successRate by remember { mutableFloatStateOf(1F) }
    // keep track of the number of questions finished/learned
    var numQuizDone by remember { mutableIntStateOf(0) }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // An info area at the top to display the flash cards count & success rate
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // success rate on the left as percentage
            val successPercent = (successRate * 100).toInt()

            // Create an annotated string with different styles
            val successString = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    fontWeight = FontWeight.Bold
                )) {
                    append("Success: ")
                }
                withStyle(style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )) {
                    append("$successPercent%")
                }
            }

            Text(
                text = successString,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier
                    .padding(end = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Text on the right
            Text(
                text = "Learned: $numQuizDone/$totalNumQuiz", // Replace with your actual success rate
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
    }
}

// Function to perform weighted random sampling
// The weights are 0, 1 or 2 (strictly non-negative)
fun sampleIndex(weights: List<Int>): Int {
    val totalWeight = weights.sum()
    val randomValue = Random.nextFloat() * totalWeight

    var cumulativeWeight = 0
    for (index in weights.indices) {
        cumulativeWeight += weights[index]
        if (randomValue < cumulativeWeight) {
            return index
        }
    }

    // In case of issues, return the first index as a fallback
    return 0
}


val previewQuizList = listOf(
    Pair("Preview Question 1", "Preview Answer 1"),
    Pair("Preview Question 2", "Preview Answer 2"),
    Pair("Preview Question 3", "Preview Answer 3")
)

@Composable
@Preview(showBackground = true)
fun DisplayScreenPreviewLight() {

    // Set the light theme
    FlashCardTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            QuizFCard(previewQuizList)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayScreenPreviewDark() {
    // Set the dark theme
    FlashCardTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            QuizFCard(previewQuizList)
        }
    }
}
