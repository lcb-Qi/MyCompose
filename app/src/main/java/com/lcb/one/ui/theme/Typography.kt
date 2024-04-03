package com.lcb.one.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight

val Typography = Typography().run {
    copy(titleLarge = titleLarge.copy(fontWeight = FontWeight.Medium))
}

