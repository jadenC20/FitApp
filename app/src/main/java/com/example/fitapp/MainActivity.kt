package com.example.fitapp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.fitapp.ui.HealthViewModel
import com.example.fitapp.ui.components.CreateEntryDialog
import com.example.fitapp.ui.screens.DashboardScreen
import com.example.fitapp.ui.screens.EntriesListScreen
import com.example.fitapp.ui.screens.TrendsScreen
import com.example.fitapp.ui.theme.FitAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitAppTheme {
                FitAppMainScreen()
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int
) {
    DASHBOARD("Dashboard", R.drawable.ic_home),
    ENTRIES("Entries Log", R.drawable.ic_favorite),
    TRENDS("Analytics", R.drawable.ic_account_box),
}

@Composable
fun FitAppMainScreen() {
    val context = LocalContext.current
    val viewModel: HealthViewModel = remember {
        HealthViewModel(context.applicationContext as Application)
    }

    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.DASHBOARD) }
    val showCreateDialog by viewModel.showCreateDialog.collectAsState()

    if (showCreateDialog) {
        CreateEntryDialog(
            onDismiss = { viewModel.closeCreateDialog() },
            onSave = { newEntry -> viewModel.addEntry(newEntry) }
        )
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(destination.icon),
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (currentDestination) {
                    AppDestinations.DASHBOARD -> DashboardScreen(
                        viewModel = viewModel,
                        onAddEntryClick = { viewModel.openCreateDialog() }
                    )
                    AppDestinations.ENTRIES -> EntriesListScreen(
                        viewModel = viewModel,
                        onAddEntryClick = { viewModel.openCreateDialog() }
                    )
                    AppDestinations.TRENDS -> TrendsScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
