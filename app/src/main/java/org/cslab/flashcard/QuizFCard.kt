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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cslab.flashcard.ui.theme.Crimson
import org.cslab.flashcard.ui.theme.DarkGreen
import org.cslab.flashcard.ui.theme.FlashCardTheme

@Composable
fun QuizFCard(
    model: QuizModel
) {
    // Total number of quizzes
    val totalNumQuiz = model.getSize()
    // Get the quiz state as a compose state variable
    val quizState by model.state.collectAsState()
    // User typed answer in the text field
    var userAnswer by rememberSaveable { mutableStateOf("") }

    // The selected quiz
    val question = quizState.currentQuiz.first.trim()
    val answer = quizState.currentQuiz.second.trim()

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
                    append("${(quizState.successRate*100).toInt()}%")
                }
            }

            // Text on the left
            Text(
                text = "Quiz: ${quizState.numAttempted}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(start = 6.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Text at the middle
            Text(
                text = successString,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier
                    .padding(2.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Text on the right
            Text(
                text = "Learned: ${quizState.numQuizDone/totalNumQuiz}", // Replace with your actual success rate
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(end = 6.dp)
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
                text = question,
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
            Column {
                Text(
                    text = when (quizState.answerStatus) {
                        AnswerStatus.CORRECT -> "Correct!"
                        AnswerStatus.WRONG -> "Wrong"
                        AnswerStatus.NA -> ""
                        AnswerStatus.ALLDONE -> "Congratulations!\nAll Done!"
                    },
                    color = when (quizState.answerStatus) {
                        AnswerStatus.CORRECT, AnswerStatus.ALLDONE -> DarkGreen
                        AnswerStatus.WRONG -> Crimson
                        else -> MaterialTheme.colorScheme.primary
                    },
                    modifier = Modifier
                        .padding(12.dp)
                )

                Text(
                    text = if (
                        quizState.answerStatus == AnswerStatus.NA  ||
                        quizState.answerStatus == AnswerStatus.ALLDONE
                        ) "" else answer,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(12.dp)
                )
            }
        }

        // Spacer to add vertical space
        Spacer(modifier = Modifier.height(16.dp))

        // if answer is too long, only require any 4 consecutive characters
        if (quizState.currentQuiz.second.length > 4) {
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
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )

            // Go Button
            Button(
                modifier = Modifier
                    .padding(start = 8.dp),
                shape = MaterialTheme.shapes.small,
                onClick = {
                    // if actual answer is longer than 4 characters
                    // this can be done inside the checkAnswer function as well
                    if (answer.length >= 4 && userAnswer.trim().length < 4)
                        return@Button
                    // update the state variables in the model
                    model.checkAnswer(userAnswer)
                }
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
                onClick = {
                    // Button shouldn't work if the question is answered already
                    // if (answerStatus != AnswerStatus.NA) return@Button
                    // update the model parameters
                    model.markAsWrong()
                },
                border = BorderStroke(width = 3.dp, color = Crimson),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "No Idea")
            }

            // Center-aligned button
            Button(
                onClick = {
                    // Button shouldn't work if the question is answered already
                    // if (answerStatus != AnswerStatus.NA) return@Button
                    // update model parameters
                    // the function takes care of (answerStatus == AnswerStatus.NA)
                    model.markAsCorrect()
                },
                border = BorderStroke(width = 3.dp, color = DarkGreen)

            ) {
                Text(text = "I Know")
            }

            // Right-aligned button with arrow icon
            Button(
                modifier = Modifier.width(80.dp),
                onClick = {
                    // Change states to the next quiz
                    // Takes care of the quiz state: (answerState == AnswerState.ALLDONE)
                    model.nextQuiz()
                    // clear the text field
                    userAnswer = ""
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

@Composable
@Preview(showBackground = true)
fun DisplayScreenPreviewLight() {
    // Set the light theme
    FlashCardTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val previewModel = QuizModel(LocalContext.current, R.raw.current_affairs)
            QuizFCard(previewModel)
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
            val previewModel = QuizModel(LocalContext.current, R.raw.current_affairs)
            QuizFCard(previewModel)
        }
    }
}

