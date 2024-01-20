package org.cslab.flashcard

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cslab.flashcard.ui.theme.FlashCardTheme
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashCardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DisplayTextFromFile()

                }
            }
        }
    }
}

@Composable
fun DisplayTextFromFile() {
    // Retrieve the context
    val context = LocalContext.current

    // Read the contents of the text file
    val textContent = readTextFile(context, R.raw.gre)

    // Display the contents in a TextField
    TextField(
        value = textContent,
        onValueChange = { /* no-op, read-only */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        )
    )
}

/**
 * Function to read the contents of a text file from the resources.
 */
fun readTextFile(context: Context, resourceId: Int): String {
    val inputStream: InputStream = context.resources.openRawResource(resourceId)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val stringBuilder = StringBuilder()
    var line: String? = reader.readLine()
    while (line != null) {
        stringBuilder.append(line).append("\n")
        line = reader.readLine()
    }
    reader.close()
    return stringBuilder.toString()
}

@Preview(showBackground = true)
@Composable
fun DisplayTextFromFilePreview() {
    // Mock data for preview
    val mockTextContent = "This is a preview text content."

    // Display the contents in a TextField
    TextField(
        value = mockTextContent,
        onValueChange = { /* no-op, read-only */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        )
    )
}
