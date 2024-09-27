package com.lcb.one.ui.screen.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.util.platform.PhoneUtil

@Composable
fun HomePage(navController: NavHostController) {
    val horizontalPadding = 16.dp
    LazyColumn(
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            val maxSize = with(LocalDensity.current) {
                val width = PhoneUtil.getScreenWidth().toDp() - horizontalPadding * 2
                val height = width / 16 * 9

                DpSize(width, height)
            }
            HeaderImage(modifier = Modifier.requiredSize(maxSize))
        }
        item { LoveCard() }
        item { ShortcutsCard(onShortcutClick = { navController.launchSingleTop(it) }) }
    }
}