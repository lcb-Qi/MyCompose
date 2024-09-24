package com.lcb.one.util.platform

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
inline fun rememberGetContent(crossinline onResult: (Uri?) -> Unit) =
    rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        onResult.invoke(it)
    }

@Composable
inline fun rememberGetContents(crossinline onResult: (List<Uri>) -> Unit) =
    rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        onResult.invoke(it)
    }

@Composable
inline fun rememberStartActivityForResult(crossinline onResult: (ActivityResult) -> Unit = {}) =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        onResult.invoke(it)
    }