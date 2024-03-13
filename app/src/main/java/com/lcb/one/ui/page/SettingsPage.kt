package com.lcb.one.ui.page

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.lcb.one.ui.widget.settings.storage.disk.rememberBooleanPreferenceState
import com.lcb.one.ui.widget.settings.storage.disk.rememberIntPreferenceState
import com.lcb.one.ui.widget.settings.ui.SettingsListDropdown
import com.lcb.one.ui.widget.settings.ui.SettingsSwitch
import com.lcb.one.R
import com.lcb.one.ui.MyApp
import com.lcb.one.ui.widget.appDynamicColor
import com.lcb.one.util.android.SharedPrefUtils
import com.lcb.one.viewmodel.PoemViewModel


object AppSettings {
    fun getPoemUpdateDurationIndex(context: Context): Int {
        val keyOfIndex = context.resources.getString(R.string.settings_poem_update_duration_key)
        val index = SharedPrefUtils.getInt(keyOfIndex)
        return index.coerceAtLeast(0)
    }

    fun getPoemUpdateDuration(context: Context, index: Int): Int {
        val values = context.resources.getIntArray(R.array.settings_duration_values)
        return values[index]
    }

    fun getAppDynamicColor(): Boolean {
        val key = MyApp.getAppContext().getString(R.string.settings_dynamic_color_key)

        return SharedPrefUtils.getBoolean(key)
    }
}

@Composable
fun SettingsPage(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        // 动态取色
        val appDynamicColorState = rememberBooleanPreferenceState(
            stringResource(R.string.settings_dynamic_color_key),
            appDynamicColor
        )

        SettingsSwitch(
            title = { SettingsTitle(stringResource(R.string.settings_dynamic_color_title)) },
            summary = { SettingsSummary(stringResource(R.string.settings_dynamic_color_summary)) },
            checked = appDynamicColorState.value
        ) {
            appDynamicColor = it
            appDynamicColorState.value = appDynamicColor
        }

        // 标题更新间隔
        val durationIndexState = rememberIntPreferenceState(
            stringResource(R.string.settings_poem_update_duration_key),
            PoemViewModel.durationIndex
        )
        val options = stringArrayResource(R.array.settings_duration_options)
        val values = integerArrayResource(R.array.settings_duration_values)
        SettingsListDropdown(
            title = { SettingsTitle(stringResource(R.string.settings_poem_update_duration_title)) },
            selectIndex = durationIndexState.value,
            items = options.toList(),
            onItemSelected = { index, _ ->
                durationIndexState.value = index
                PoemViewModel.durationIndex = index
                PoemViewModel.duration = values[index]
            }
        )
    }
}

@Composable
fun SettingsTitle(title: String) {
    Text(text = title, style = MaterialTheme.typography.titleMedium)
}

@Composable
fun SettingsSummary(summary: String) {
    Text(text = summary, style = MaterialTheme.typography.bodySmall)
}