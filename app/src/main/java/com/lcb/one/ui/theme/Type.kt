package com.lcb.one.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 22.0.sp,
        lineHeight = 28.0.sp,
        letterSpacing = 0.0.sp,
    )
)

@Composable
fun titleLarge() = MaterialTheme.typography.titleLarge

@Composable
fun titleMedium() = MaterialTheme.typography.titleMedium

@Composable
fun titleSmall() = MaterialTheme.typography.titleSmall

@Composable
fun labelLarge() = MaterialTheme.typography.labelLarge

@Composable
fun labelMedium() = MaterialTheme.typography.labelMedium

@Composable
fun labelSmall() = MaterialTheme.typography.labelSmall

@Composable
fun bodyLarge() = MaterialTheme.typography.bodyLarge

@Composable
fun bodyMedium() = MaterialTheme.typography.bodyMedium

@Composable
fun bodySmall() = MaterialTheme.typography.bodySmall

