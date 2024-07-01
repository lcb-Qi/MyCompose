package com.lcb.one.ui.widget.appbar

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.lcb.one.ui.widget.common.AppIconButton

@Composable
private fun DefaultNavIcon() {
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    AppIconButton(
        icon = Icons.AutoMirrored.Rounded.ArrowBack,
        onClick = { backPressedDispatcher?.onBackPressed() }
    )
}

@Composable
fun ToolBar(
    title: String,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    navigationIcon: (@Composable () -> Unit)? = { DefaultNavIcon() },
    actions: (@Composable RowScope.() -> Unit)? = null,
) = ToolBar(
    title = { Text(text = title, style = titleStyle) },
    navigationIcon = navigationIcon,
    actions = actions
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBar(
    title: @Composable () -> Unit,
    navigationIcon: (@Composable () -> Unit)? = { DefaultNavIcon() },
    actions: (@Composable RowScope.() -> Unit)? = null,
) = TopAppBar(
    title = title,
    navigationIcon = navigationIcon ?: {},
    actions = actions ?: {},
)