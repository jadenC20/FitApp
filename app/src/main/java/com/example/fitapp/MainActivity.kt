package com.example.fitapp

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import com.example.fitapp.notification.NotificationHelper
import com.example.fitapp.ui.HealthViewModel
import com.example.fitapp.ui.components.CreateEntryDialog
import com.example.fitapp.ui.fragments.DashboardFragment
import com.example.fitapp.ui.fragments.EntriesListFragment
import com.example.fitapp.ui.fragments.TrendsFragment
import com.example.fitapp.ui.theme.FitAppTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Schedule daily reminders
        NotificationHelper.scheduleDailyReminder(this)

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
    val fragmentActivity = context as FragmentActivity

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

    val containerId = remember { View.generateViewId() }

    LaunchedEffect(currentDestination) {
        val targetFragment = when (currentDestination) {
            AppDestinations.DASHBOARD -> DashboardFragment()
            AppDestinations.ENTRIES -> EntriesListFragment()
            AppDestinations.TRENDS -> TrendsFragment()
        }

        fragmentActivity.supportFragmentManager
            .beginTransaction()
            .replace(containerId, targetFragment)
            .commit()
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
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        FrameLayout(ctx).apply {
                            id = containerId
                        }
                    }
                )
            }
        }
    }
}
