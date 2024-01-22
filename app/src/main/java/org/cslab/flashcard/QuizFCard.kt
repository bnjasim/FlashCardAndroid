package org.cslab.flashcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cslab.flashcard.ui.theme.Crimson
import org.cslab.flashcard.ui.theme.DarkGreen
import org.cslab.flashcard.ui.theme.FlashCardTheme
import kotlin.random.Random

@Composable
fun QuizFCard(
    quizList: List<Pair<String, String>>,
    qIndex: Int
) {
    // Total number of quizzes
    val totalNumQuiz = quizList.size
    // A weight of 2 is assigned to each quiz initially.
    // The weight is reduced by 1 when the quiz is answered correctly.
    // The weight is reduced by 2 if the quiz is skipped.
    var quizWeights = MutableList(totalNumQuiz) { 2 }

    // Define compose state variables
    var quizIndex by remember { mutableIntStateOf(qIndex) }
    var answer by remember { mutableStateOf("") }
    // keep track of percentage/accuracy 0 to 1 (not percentage)
    var successRate by remember { mutableFloatStateOf(1F) }
    // keep track of the number of questions finished/learned
    var numQuizDone by remember { mutableIntStateOf(0) }
    // User answer
    var userAnswer by remember { mutableStateOf("") }

    // The selected quiz
    var quiz = quizList[quizIndex]

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

        // Display the Question Area
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(150.dp)
        ) {
            Text(
                text = quiz.first,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(12.dp)
            )
        }
        // Answer Area
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(150.dp)
        ) {
            Text(
                text = answer,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(12.dp)
            )
        }

        // Spacer to add vertical space
        Spacer(modifier = Modifier.height(16.dp))

        // if answer is too long, only require any 4 consecutive characters
        if (quiz.second.length > 4) {
            // Display a small info text on top of the text field
            Text(
                text = "Any consecutive 4 characters",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 12.dp, top = 12.dp)
            )
        }

        // An area to enter answer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User TextField
            TextField(
                value = userAnswer,
                onValueChange = { userAnswer = it },
                label = { Text("Answer") },
                modifier = Modifier.weight(1f)
            )

            // Go Button
            Button(
                onClick = { /* onGoClick(userInput) */ },
                modifier = Modifier
                    .padding(start = 8.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Go",
                    modifier = Modifier
                        .padding(5.dp)
                )
            }
        }

        // Spacer to add vertical space
        Spacer(modifier = Modifier.height(32.dp))

        // A bottom row for 3 Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left-aligned button
            Button(
                onClick = { /* Handle "No Idea" button click */ },
                border = BorderStroke(width = 3.dp, color = Crimson),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "No Idea")
            }

            // Center-aligned button
            Button(
                onClick = { /* Handle "I Know" button click */ },
                border = BorderStroke(width = 3.dp, color = DarkGreen)

            ) {
                Text(text = "I Know")
            }

            // Right-aligned button with arrow icon
            Button(
                modifier = Modifier.width(80.dp),
                onClick = {
                          // Reset the question by random sampling
                          quizIndex = sampleIndex(quizWeights)
                          },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray)
            ) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary
                )
            }
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
            QuizFCard(previewQuizList, 0)
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
            QuizFCard(previewQuizList, 1)
        }
    }
}

