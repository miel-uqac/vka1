package com.example.keyboard.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyboard.R

@Composable
fun MainScreen(
    sendData: (String) -> Unit
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Page Clavier")

    Column {
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                "Keyboard",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        TabRow(
            selectedTabIndex = tabIndex
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        Column(modifier = Modifier.fillMaxSize(1f)) {
            when (tabIndex) {
                0 -> {
                    MacrosTab(
                        sendData = sendData

                    )
                }
            }

        }
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            KeyboardInput(sendData)
        }
    }
}

@Composable
fun MacrosTab(
    sendData: (String) -> Unit
) {
    val context = LocalContext.current
    Column {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            TextButton(
                onClick = {
                    sendData(context.getString(R.string.id_copy))
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Copier")
            }
            TextButton(
                onClick = {
                    sendData("up")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("↑")
            }
            TextButton(
                onClick = {
                    sendData(context.getString(R.string.id_paste))
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Coller")
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            TextButton(
                onClick = {
                    sendData("left")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("←")
            }
            TextButton(
                onClick = {
                    sendData("down")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("↓")
            }
            TextButton(
                onClick = {
                    sendData("right")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("→")
            }
        }
        Row {
            Text(
                text = "Les différentes flèches servent à naviguer sur le texte. Si le clavier disparaît, appuyez sur la touche retour arrière pour qu'il réapparaisse.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}


@Composable
fun KeyboardInput(sendData: (String) -> Unit) {
    var input by remember { mutableStateOf("") }
    val backspaceKeyCode = Key.Backspace.nativeKeyCode
    val backspace = """\b"""
    val focusRequester = remember { FocusRequester() }
    TextField(
        value = input,
        onValueChange = { newText ->

            val addedText =
                if (newText.isNotEmpty()) {
                    findFirstDifferenceIndex(newText, input).let {
                        if (it >= 0) {
                            for (i in it..<input.length) {
                                sendData(backspace)
                            }
                            newText.substring(it)
                        } else {
                            if (newText.length > input.length) {
                                newText.substring(input.length)
                            } else {
                                for (i in 1..input.length - newText.length) {
                                    sendData(backspace)
                                }
                                ""
                            }
                        }
                    }
                } else {
                    sendData(backspace)
                    ""
                }

            input = if (addedText != ".") newText else ""

            if (addedText.isNotEmpty()) {
                sendData(addedText)
            }

        },
        label = { Text("Label") },

        modifier = Modifier
            .onKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.keyCode == backspaceKeyCode && input.isEmpty()) {
                    sendData(backspace)
                    true
                } else {
                    false
                }
            }
            .fillMaxWidth()
            .alpha(0f)
            .focusRequester(focusRequester),

        singleLine = true,
        textStyle = TextStyle(color = Color.Transparent), // Le texte est également invisible,


    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

}


fun findFirstDifferenceIndex(str1: String, str2: String): Int {
    return str1.zip(str2).indexOfFirst { (char1, char2) -> char1 != char2 }
}

