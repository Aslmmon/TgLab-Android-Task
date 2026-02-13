package com.aslmmovic.tglabtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aslmmovic.tglabtask.presentation.navigation.AppRoot
import com.aslmmovic.tglabtask.presentation.theme.TgLabTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TgLabTaskTheme {
                AppRoot()
            }
        }
    }
}
