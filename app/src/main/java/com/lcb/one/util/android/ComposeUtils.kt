package com.lcb.one.util.android

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun rememberLauncherForGetContent(onResult: (Uri?) -> Unit) =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        onResult.invoke(it)
    }

@Composable
fun rememberLauncherForGetContents(onResult: (List<Uri>) -> Unit) =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) {
        onResult.invoke(it)
    }

@Composable
fun rememberLauncherForStartActivity(onResult: (ActivityResult) -> Unit) =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        onResult.invoke(it)
    }