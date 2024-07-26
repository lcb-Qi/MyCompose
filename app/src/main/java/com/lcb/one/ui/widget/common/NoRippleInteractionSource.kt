package com.lcb.one.ui.widget.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction) = true
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onClick: () -> Unit
) = then(
    Modifier.combinedClickable(
        enabled = enabled,
        role = role,
        indication = null,
        interactionSource = NoRippleInteractionSource(),
        onDoubleClick = onDoubleClick,
        onLongClick = onLongClick,
        onLongClickLabel = onLongClickLabel,
        onClickLabel = onClickLabel,
        onClick = onClick,
    )
)
