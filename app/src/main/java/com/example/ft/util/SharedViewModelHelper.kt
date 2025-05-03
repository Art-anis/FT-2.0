package com.example.ft.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import org.koin.androidx.compose.navigation.koinNavViewModel

//вспомогательная функция для инициализации общей viewmodel
@Composable
inline fun<reified T: ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return koinNavViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinNavViewModel(viewModelStoreOwner = parentEntry)
}