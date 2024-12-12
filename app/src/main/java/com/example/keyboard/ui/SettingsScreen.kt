package com.example.keyboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.keyboard.data.Layout


@Composable
fun SettingsScreen(
    changeTheme: (Boolean) -> Unit,
    setLayout: (Layout) -> Unit,
    onBackButtonClicked: () -> Unit = {},
    isDarkTheme: Boolean,
    actualLayout: Layout
) {
    listOf("Claviers", "Thèmes")


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    )
    {
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            IconButton(onClick = onBackButtonClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back"
                )
            }
            Text(
                "Keyboard",
                modifier = Modifier.align(Alignment.CenterVertically)
            )

        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp), // Espace autour du Divider
            thickness = 1.dp, // Épaisseur de la ligne
            color = Color.Gray // Couleur de la ligne
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ClaviersTab(setLayout, actualLayout)

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ThemesTab(changeTheme, isDarkTheme)
        }
    }
}

@Composable
fun ThemesTab(changeTheme: (Boolean) -> Unit, isDarkTheme: Boolean) {


    Box {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                text = if (isDarkTheme) "Thème sombre activé" else "Thème clair activé",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Switch(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically),
                checked = isDarkTheme,
                onCheckedChange = {
                    changeTheme(it)
                }
            )
        }
    }
}

@Composable
fun ClaviersTab(setLayout: (Layout) -> Unit, layout: Layout) {
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val layoutOptions = Layout.entries.toTypedArray()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Choose your preferred keyboard layout:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
        // Main dropdown button with centered text and arrow
        Box(
            Modifier
                .fillMaxWidth()
                .clickable { isDropDownExpanded.value = true }
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Layout: $layout",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(8.dp)) // Space between text and arrow
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown arrow",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Dropdown menu positioned directly below the dropdown button
        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = { isDropDownExpanded.value = false },
            modifier = Modifier
                .fillMaxWidth() // Full width for centered items
        ) {
            layoutOptions.forEach { option ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(), // Full width for alignment
                    text = {
                        Text(
                            text = option.toString(),
                            modifier = Modifier.fillMaxWidth(), // Full width to ensure centering
                            textAlign = TextAlign.Center // Center the text within the item
                        )
                    },
                    onClick = {
                        isDropDownExpanded.value = false
                        setLayout(option)
                    }
                )
            }
        }
    }
}

