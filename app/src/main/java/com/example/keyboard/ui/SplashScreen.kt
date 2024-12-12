package com.example.keyboard.ui

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyboard.R
import com.example.keyboard.ui.theme.DarkerBlue
import kotlin.reflect.KFunction1

@Composable
fun SplashScreen(
    connected: Boolean,
    hasPermission: Boolean,
    onGranted: () -> Unit,
    askPermission: KFunction1<Context, Unit>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Box(modifier) {
        Image(
            painter = painterResource(R.drawable.splash),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
        )
        val value: String = if (!connected) {
            "Branchez le périphérique HID"
        } else if (!hasPermission) {
            "Veuillez autoriser l'accès USB"
        } else {
            "Chargement..."
        }

        Column(modifier = Modifier.align(Alignment.Center)) {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(320.dp)
                    .background(Color(0xFF98C3D9))
            ) {
                Text(
                    text = value,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    color = DarkerBlue,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            AnimatedVisibility(
                visible = !hasPermission && connected,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = { askPermission(context) },
                    content = { Text("Autoriser") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            onGranted()
        }
    }
}