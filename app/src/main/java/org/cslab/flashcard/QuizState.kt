package org.cslab.flashcard

data class QuizState(
    var currentQuiz: Pair<String, String>,
    var answerStatus: AnswerStatus,
    var numAttempted: Int,
    var successRate: Float,
    var numQuizDone: Int
)
