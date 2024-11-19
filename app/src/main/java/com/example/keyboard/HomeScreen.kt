package com.example.keyboard.ui

import androidx.compose.foundation.layout.*

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.example.keyboard.R


@Composable
fun HomeScreen(
    sendData: (String) -> Unit,
    connected: Boolean,
    hasPermission: Boolean,
    onUSBDisconnected: () -> Unit
) {
    LaunchedEffect(connected, hasPermission) {
        if (!connected || !hasPermission) {
            onUSBDisconnected()
        }
    }

    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Macros") // AJOUT DE PAGE PAR LA SUITE

    Column{
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text(
                "Keyboard",
                modifier = Modifier.align(Alignment.CenterVertically)
            )

        }
        TabRow(
            selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        Row (modifier  = Modifier.fillMaxSize(0.5f)) {
            when (tabIndex) {
                0 -> MacrosTab(sendData = sendData)
                //AJOUT DE PAGE PAR LA SUITE
            }

        }
        Row (modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
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
                    sendData(context.getString(R.string.copier))
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
                    sendData(context.getString(R.string.coller))
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
    }
}

@Composable
fun KeyboardInput(sendData: (String) -> Unit, modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    val backspaceKeyCode = Key.Backspace.nativeKeyCode
    val backspace: String = """\b"""
    TextField(
        value = input,
        onValueChange = { newText ->

            val addedText =
                if (newText.isNotEmpty()) {
                    findFirstDifferenceIndex(newText, input).let {
                        if (it >= 0) {
                            for (i in it..input.length - 1) {
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
            .fillMaxWidth(),
        singleLine = true,
        textStyle = TextStyle(color = Color.Transparent)
    )

}


fun findFirstDifferenceIndex(str1: String, str2: String): Int {
    return str1.zip(str2).indexOfFirst { (char1, char2) -> char1 != char2 }
}
