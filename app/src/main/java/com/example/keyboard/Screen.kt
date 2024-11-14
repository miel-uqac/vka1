package com.example.keyboard.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.keyboard.R
import kotlinx.coroutines.delay

@Composable
fun AnimatedSplashScreen(onComplete: () -> Unit) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        // Animation slpash
        alpha.animateTo(1f, animationSpec = tween(durationMillis = 1000))
        scale.animateTo(1f, animationSpec = tween(durationMillis = 1000))
        delay(2000) // Garde l'écran un peu
        onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = alpha.value, scaleX = scale.value, scaleY = scale.value)
                .align(Alignment.Center)
        )
    }
}
