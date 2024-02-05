package org.cslab.flashcard

data class QuizState(
    val currentQuiz: Pair<String, String>,
    val answerStatus: AnswerStatus,
    val numAttempted: Int,
    val successRate: Float,
    val numQuizDone: Int
)
