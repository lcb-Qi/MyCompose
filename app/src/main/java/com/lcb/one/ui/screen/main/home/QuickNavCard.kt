package com.lcb.one.ui.screen.main.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lcb.one.R
import com.lcb.one.ui.LocalNav
import com.lcb.one.ui.Screen
import com.lcb.one.ui.defaultScreens
import com.lcb.one.ui.launchSingleTop
import com.lcb.one.ui.supportQuickNavScreens
import com.lcb.one.ui.widget.common.AppTextButton
import com.lcb.one.util.android.UserPref
import com.lcb.one.util.common.JsonUtils
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

private val userQuickNav by lazy {
    val quickNav = UserPref.getString(UserPref.Key.QUICK_NAV, "")
    val serializer = ListSerializer(String.serializer())
    val quickNavs = JsonUtils.fromJsonOrDefault(quickNav, serializer)?.let { list ->
        defaultScreens.filter { list.contains(it.route) }
    }

    quickNavs?.toMutableStateList() ?: mutableStateListOf()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuickNavCard(modifier: Modifier = Modifier) {
    val navController = LocalNav.current!!
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.shortcut), style = MaterialTheme.typography.titleMedium)

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                userQuickNav.forEach {
                    ElevatedAssistChip(
                        onClick = { navController.launchSingleTop(it.route) },
                        label = { Text(text = it.label) }
                    )
                }

                var showAdd by remember { mutableStateOf(false) }
                SuggestionChip(
                    onClick = { showAdd = true },
                    label = { Text(text = stringResource(R.string.add_shortcut)) },
                    icon = { Icon(Icons.Rounded.AddCircle, null) }
                )

                AddQuickNavDialog(
                    show = showAdd,
                    onDismiss = { showAdd = false },
                    onSelectFinish = {
                        userQuickNav.clear()
                        userQuickNav.addAll(it)
                        UserPref.putString(
                            UserPref.Key.QUICK_NAV,
                            JsonUtils.toJson(it.map { it.route })
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun AddQuickNavDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onSelectFinish: (List<Screen>) -> Unit = {}
) {
    if (!show) return

    val selectedScreen = remember { mutableStateListOf<Screen>() }
    LaunchedEffect(Unit) {
        selectedScreen.addAll(userQuickNav)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.add_shortcut)) },
        dismissButton = {
            AppTextButton(
                text = stringResource(R.string.cancel),
                onClick = onDismiss
            )
        },
        confirmButton = {
            AppTextButton(
                text = stringResource(R.string.ok),
                onClick = {
                    onDismiss()
                    onSelectFinish(selectedScreen)
                }
            )
        },
        text = {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(count = supportQuickNavScreens.size, key = { it }) { index ->
                    val screen = supportQuickNavScreens[index]
                    val selected = screen in selectedScreen
                    FilterChip(
                        selected = selected,
                        onClick = {
                            if (selected) {
                                selectedScreen.remove(screen)
                            } else {
                                selectedScreen.add(screen)
                            }
                        },
                        label = { Text(text = screen.label) },
                        trailingIcon = {
                            AnimatedVisibility(selected) {
                                Icon(Icons.Rounded.Check, null)
                            }
                        }
                    )
                }
            }
        }
    )
}
