package com.lcb.one.ui.widget.appbar

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.lcb.one.ui.widget.common.AppIconButton

@Composable
fun ToolBar(
    title: String,
    enableBack: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
) {
    ToolBar(title = { Text(text = title) }, enableBack, actions)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ToolBar(
    title: @Composable () -> Unit,
    enableBack: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    TopAppBar(
        navigationIcon = {
            if (enableBack) {
                AppIconButton(
                    icon = Icons.AutoMirrored.Rounded.ArrowBack,
                    onClick = { backPressedDispatcher?.onBackPressed() }
                )
            }
        },
        actions = actions,
        title = title
    )
}