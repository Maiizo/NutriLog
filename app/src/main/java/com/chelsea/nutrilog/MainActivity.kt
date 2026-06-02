package com.chelsea.nutrilog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.chelsea.nutrilog.core.theme.NutriLogTheme
import com.chelsea.nutrilog.navigation.NutriLogApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriLogTheme {
                // Use the Hilt-backed composable app entrypoint which obtains ViewModels via hiltViewModel()
                NutriLogApp()
            }
        }
    }
}