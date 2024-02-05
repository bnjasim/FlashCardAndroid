package org.cslab.flashcard

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.random.Random

class QuizModel(
    private val context: Context,
    private val resourceId: Int
) {
    // Read the contents of the text file
    private val quizList: MutableList<Pair<String, String>> = mutableListOf()
    private var quizIndex: Int
    private val quizWeights: MutableList<Int>

    // Quiz State encapsulates all state variables
    val state = MutableStateFlow(
        QuizState(
            currentQuiz = Pair("", ""),
            answerStatus = AnswerStatus.NA,
            numAttempted = 0,
            successRate = 1F,
            numQuizDone = 0
        )
    )

    init {
        readTextFile()
        // Randomly pick a quiz as the first one to display
        // startIndex = Random.nextInt(quizList.size)
        // A mutable flow variable whose value will be collected in the UI
        quizIndex = Random.nextInt(quizList.size)
        // Set the initial quiz state
        state.update { it.copy(
            currentQuiz = quizList[quizIndex]
        ) }
        // Log.i("MyTag", state.value.numAttempted.toString())
        // A weight of 2 is assigned to each quiz initially.
        // The weight is reduced by 1 when the quiz is answered correctly.
        // The weight is reduced by 2 if the quiz is skipped.
        quizWeights = MutableList(quizList.size) { 2 }
    }

    fun isEmpty(): Boolean {
        return quizList.isEmpty()
    }

    fun getSize(): Int {
        return quizList.size
    }

    fun getAllQuiz(): MutableList<Pair<String, String>> {
        return quizList
    }

    fun checkAnswer(userAnswer: String) {
        val actualAnswer = state.value.currentQuiz.second.lowercase()
        val uAnswer = userAnswer.trim().lowercase()
        val correct = if (userAnswer.length >= 4) {
            // compare against the actual answer
            // userAnswer should be a subString of actual answer
            // case insensitive!
            actualAnswer.contains(uAnswer)
        } else {
            (actualAnswer == uAnswer)
        }
        // if correct answer
        if (correct) {
            markAsCorrect()
        }
        else {
            markAsWrong()
        }
    }

    fun markAsCorrect() {
        // should execute only if the question is Not Answered yet!
        if (state.value.answerStatus != AnswerStatus.NA) return
        // Otherwise
        state.update { it.copy(
            answerStatus = AnswerStatus.CORRECT
        )}
        // update the success rate
        val numCorrectAnswers = state.value.successRate * state.value.numAttempted
        state.update { it.copy(
            numAttempted = it.numAttempted + 1
        )}
        state.update { it.copy(
            successRate = (numCorrectAnswers + 1)/it.numAttempted
        )}
        // Subtract weight of this quiz
        quizWeights[quizIndex]--
        if(quizWeights[quizIndex] < 0) {
            // This should never happen!
            Log.e("myTag", "weight went below 0. Fishy!!")
            quizWeights[quizIndex] = 0
        }
        // update the number of Done questions
        if (quizWeights[quizIndex] == 0) {
            state.update { it.copy(
                numQuizDone = it.numQuizDone
            )}
        }
    }

    fun markAsWrong() {
        // should execute only if the question is Not Answered yet!
        if (state.value.answerStatus != AnswerStatus.NA) return
        // Otherwise
        state.update { it.copy(
            answerStatus = AnswerStatus.WRONG
        )}
        // update the success rate
        val numCorrectAnswers = state.value.successRate * state.value.numAttempted
        state.update { it.copy(
            numAttempted = it.numAttempted + 1
        )}
        state.update { it.copy(
            successRate = numCorrectAnswers/it.numAttempted
        )}
    }

    fun nextQuiz() {
        // Stop when all the weights are zero
        if (state.value.numQuizDone == getSize()) {
            state.update { it.copy(
                answerStatus = AnswerStatus.ALLDONE
            )}
        }
        else {
            // Reset the question by random sampling
            quizIndex = sampleIndex()
            state.update { it.copy(
                currentQuiz = quizList[quizIndex]
            )}
            // hide the correct answer!
            state.update { it.copy(
                answerStatus = AnswerStatus.NA
            )}
        }
    }

    // Function to perform weighted random sampling
    // The weights are 0, 1 or 2 (strictly non-negative)
    private fun sampleIndex(): Int {
        val totalWeight = quizWeights.sum()
        val randomValue = Random.nextFloat() * totalWeight

        var cumulativeWeight = 0
        for (index in quizWeights.indices) {
            cumulativeWeight += quizWeights[index]
            if (randomValue < cumulativeWeight) {
                return index
            }
        }

        // In case of issues, return the first index as a fallback
        return 0
    }

    private fun readTextFile() {
        val inputStream: InputStream = context.resources.openRawResource(resourceId)
        val reader = BufferedReader(InputStreamReader(inputStream))
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
    }
}