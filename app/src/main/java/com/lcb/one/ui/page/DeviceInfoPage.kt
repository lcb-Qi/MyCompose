package com.lcb.one.ui.page

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lcb.one.ui.theme.bodyLarge
import com.lcb.one.ui.theme.labelLarge
import com.lcb.one.bean.BasicInfo
import com.lcb.one.bean.DisplayInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun DeviceInfoPage() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val tabTitles = listOf("Basic", "Display")
    Column(modifier = Modifier.fillMaxSize(), Arrangement.spacedBy(4.dp)) {
        TabRow(
            selectedTabIndex = selectedIndex,
            modifier = Modifier
                .padding(vertical = 4.dp)
        ) {
            tabTitles.forEachIndexed { index, text ->
                val selected = selectedIndex == index
                Tab(
                    modifier = Modifier.zIndex(2f),
                    selected = selected,
                    onClick = { selectedIndex = index },
                    text = { Text(text = text, style = labelLarge()) },
                    interactionSource = NoRippleInteractionSource()
                )
            }
        }

        when (selectedIndex) {
            0 -> BasicInfoList()
            1 -> DisplayInfoList()
        }
    }
}

@Composable
private fun InfoList(items: Map<String, String>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items.forEach { (key, value) ->
            item {
                Row {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        text = key,
                        textAlign = TextAlign.Start,
                        style = bodyLarge()
                    )

                    Spacer(modifier = Modifier.weight(0.5f))

                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        text = value,
                        textAlign = TextAlign.End,
                        style = bodyLarge()
                    )
                }
            }
        }
    }
}

@Composable
private fun DisplayInfoList() {
    val displayInfo by remember { mutableStateOf(DisplayInfo.obtain()) }

    InfoList(items = buildMap {
        put("displaySize", displayInfo.displaySize.toString())
        put("resolution", displayInfo.resolution.toString())
        put("smallWidth", displayInfo.smallWidth.toString())
        put("dpi", displayInfo.dpi.toString())
        put("density", displayInfo.density.toString())
        put("statusBars", displayInfo.statusBarsHeight.toString())
        put("navigationBars", displayInfo.navigationBarsHeight.toString())
    })
}

@Composable
private fun BasicInfoList() {
    val deviceInfo by remember { mutableStateOf(BasicInfo.obtain()) }

    InfoList(items = buildMap {
        put("deviceModel", deviceInfo.deviceModel)
        put("brand", deviceInfo.brand)
        put("osVersion", deviceInfo.osVersion)
        put("sdkVersion", deviceInfo.sdkVersion.toString())
    })
}

class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction) = true
}




