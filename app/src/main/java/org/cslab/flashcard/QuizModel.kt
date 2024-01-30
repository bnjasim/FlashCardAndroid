package org.cslab.flashcard

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class QuizModel(val context: Context, val resourceId: Int) {
    // Read the contents of the text file
    val quizList: MutableList<Pair<String, String>> = mutableListOf()
    init {
        readTextFile()
    }

    fun isEmpty(): Boolean {
        return quizList.isEmpty()
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