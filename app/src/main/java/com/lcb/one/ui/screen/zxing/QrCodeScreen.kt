package com.lcb.one.ui.screen.zxing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lcb.one.ui.AppNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@Destination<AppNavGraph>
@Composable
fun QrCodeScreen() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
        }
    }
}