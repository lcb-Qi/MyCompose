package com.lcb.one.ui.widget.common

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable

@Composable
fun listItemColorOnCard(): ListItemColors {
    return ListItemDefaults.colors(containerColor = CardDefaults.cardColors().containerColor)
}