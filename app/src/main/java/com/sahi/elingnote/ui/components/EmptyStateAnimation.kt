package com.sahi.elingnote.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sahi.elingnote.R
import kotlinx.coroutines.delay
import kotlin.streams.toList

@Composable
fun EmptyStateAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            R.raw.empty_state
        )
    )
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier.size(300.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        TypeWriterText(
            texts = listOf("It's Empty"),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
fun TypeWriterText(
    texts: List<String>,
    color: Color,
    style: TextStyle,
    fontWeight:FontWeight,
    fontStyle: FontStyle,
) {
    var textIndex by remember {
        mutableIntStateOf(0)
    }
    var textToDisplay by remember {
        mutableStateOf("")
    }
    val textCharsList: List<List<String>> = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            texts.map {
                it.splitToCodePoints()
            }
        } else {
            texts.map { text ->
                text.map {
                    it.toString()
                }
            }
        }
    }

    LaunchedEffect(
        key1 = texts,
    ) {
        while (textIndex < textCharsList.size) {
            delay(200)
            textCharsList[textIndex].forEachIndexed { charIndex, _ ->
                textToDisplay = textCharsList[textIndex]
                    .take(
                        n = charIndex + 1,
                    ).joinToString(
                        separator = "",
                    )
                delay(160)
            }
            textIndex = (textIndex + 1) % texts.size
            delay(1000)
        }
    }

    Text(
        text = textToDisplay,
        color = color,
        style = style,
        fontWeight = fontWeight,
        fontStyle = fontStyle
    )
}

@RequiresApi(Build.VERSION_CODES.N)
fun String.splitToCodePoints(): List<String> {
    return codePoints()
        .toList()
        .map {
            String(Character.toChars(it))
        }
}