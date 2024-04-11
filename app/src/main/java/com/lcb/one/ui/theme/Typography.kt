package com.lcb.one.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography().run {
    copy(
        titleLarge = titleLarge.copy(fontWeight = FontWeight.Medium, fontSize = 20.sp)
    )
}

