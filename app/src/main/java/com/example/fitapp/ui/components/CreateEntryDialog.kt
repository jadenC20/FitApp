package com.example.fitapp.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fitapp.R
import com.example.fitapp.data.HealthEntry
import com.example.fitapp.data.HealthMetricType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEntryDialog(
    onDismiss: () -> Unit,
    onSave: (HealthEntry) -> Unit
) {
    val context = LocalContext.current
    var selectedType by remember { mutableStateOf(HealthMetricType.CALORIES) }
    var valueText by remember { mutableStateOf("") }
    var notesText by remember { mutableStateOf("") }
    var photoUriString by remember { mutableStateOf<String?>(null) }
    var expandedDropdown by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            photoUriString = it.toString()
        }
    }

    val selectedBitmap = rememberBitmapFromUri(context, photoUriString)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Track Health Entry",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Metric Type Selector Dropdown
                Text(
                    text = "Select Health Metric:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown }
                ) {
                    OutlinedTextField(
                        value = "${selectedType.displayName} (${selectedType.defaultUnit})",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        HealthMetricType.entries.forEach { metric ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(metric.color)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("${metric.displayName} (${metric.defaultUnit})")
                                    }
                                },
                                onClick = {
                                    selectedType = metric
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }

                // Numeric Value Input
                OutlinedTextField(
                    value = valueText,
                    onValueChange = {
                        valueText = it
                        errorMessage = null
                    },
                    label = { Text("Amount / Value") },
                    suffix = { Text(selectedType.defaultUnit) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = errorMessage != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Notes Input
                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text("Notes (Optional)") },
                    placeholder = { Text("e.g. Healthy breakfast, 30 min run") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )

                // Optional Daily Photo
                Text(
                    text = "Attach Daily Photo (Optional):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (selectedBitmap != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            bitmap = selectedBitmap,
                            contentDescription = "Selected Photo",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { photoUriString = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                .size(28.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_favorite),
                                contentDescription = "Remove Photo",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = { photoPickerLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_account_box),
                            contentDescription = "Add Photo"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pick Photo from Gallery")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedValue = valueText.toDoubleOrNull()
                    if (parsedValue == null || parsedValue <= 0.0) {
                        errorMessage = "Please enter a valid positive number"
                        return@Button
                    }

                    val entry = HealthEntry(
                        metricType = selectedType,
                        value = parsedValue,
                        unit = selectedType.defaultUnit,
                        notes = notesText.trim(),
                        dateMillis = System.currentTimeMillis(),
                        photoUri = photoUriString
                    )
                    onSave(entry)
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Save Entry")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
