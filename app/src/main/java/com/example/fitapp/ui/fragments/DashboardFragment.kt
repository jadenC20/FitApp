package com.example.fitapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fitapp.ui.HealthViewModel
import com.example.fitapp.ui.screens.DashboardScreen
import com.example.fitapp.ui.theme.FitAppTheme

class DashboardFragment : Fragment() {

    private val viewModel: HealthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FitAppTheme {
                    DashboardScreen(
                        viewModel = viewModel,
                        onAddEntryClick = { viewModel.openCreateDialog() }
                    )
                }
            }
        }
    }
}
