package com.lcb.one.ui.widget.appbar

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lcb.one.ui.widget.common.AppIconButton
import com.lcb.one.ui.widget.common.NoRippleInteractionSource

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun ToolBar(
    title: String,
    enableBack: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
    onTitleClick: (() -> Unit)? = null,
    onTitleLongClick: (() -> Unit)? = null
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
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .wrapContentSize()
                    .combinedClickable(
                        onLongClick = onTitleLongClick,
                        indication = LocalIndication.current,
                        interactionSource = NoRippleInteractionSource(),
                    ) {
                        onTitleClick?.invoke()
                    }
            )
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ToolBar(
    text: @Composable () -> Unit,
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
        title = text
    )
}