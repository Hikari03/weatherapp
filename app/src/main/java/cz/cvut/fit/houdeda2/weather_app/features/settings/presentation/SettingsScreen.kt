package cz.cvut.fit.houdeda2.weather_app.features.settings.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import cz.cvut.fit.houdeda2.weather_app.R
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherLocationGeo
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.weatherLocationGeoSaver
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject


@SuppressLint("CoroutineCreationDuringComposition", "MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    firebaseAnalytics: FirebaseAnalytics = koinInject()
) {

    LaunchedEffect(Unit) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "SettingsScreen")
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var apiKey by rememberSaveable { mutableStateOf(viewModel.getAPIKey()) }
    var selectedLocationGeo by rememberSaveable(stateSaver = weatherLocationGeoSaver) {
        mutableStateOf(
            viewModel.getCurrentLocation()
        )
    }
    var location by rememberSaveable { mutableStateOf(selectedLocationGeo.locationName) }

    val locations by viewModel.locationStateStream.collectAsStateWithLifecycle()

    val error = locations.error

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.inversePrimary,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (error != null) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = error,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            SettingCard(
                title = stringResource(id = R.string.api_key),
            ) {
                TextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    modifier = Modifier.padding(16.dp),
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.enter_api_key),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer

                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    shape = MaterialTheme.shapes.small
                )
            }

            SettingCard(
                title = if (selectedLocationGeo.locationName != "")
                    "${stringResource(id = R.string.current_location)}: ${selectedLocationGeo.locationName}, ${selectedLocationGeo.country}"
                else
                    stringResource(id = R.string.current_location) + ": None",
            ) {
                TextField(
                    value = location,
                    onValueChange = { location = it },
                    modifier = Modifier.padding(16.dp),
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.enter_location_name),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    shape = MaterialTheme.shapes.small
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    TryContentWithLocationPermission(
                        rationale = stringResource(id = R.string.location_permission_rationale),
                        permissionNotAvailableContent = {
                            val message = stringResource(id = R.string.location_permission_required)
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = message,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                },
                                modifier = Modifier.padding(top = 8.dp),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    contentColor = MaterialTheme.colorScheme.onTertiary
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.help_24),
                                    contentDescription = stringResource(id = R.string.location_permission_required)
                                )
                            }
                        }
                    ) {


                        Button(
                            onClick = {

                                viewModel.getUserLocation()
                            },
                            modifier = Modifier.padding(top = 8.dp),
                            shape = MaterialTheme.shapes.small,
                        ) {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.pin_drop_24),
                                    contentDescription = stringResource(id = R.string.get_current_location),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = stringResource(id = R.string.get_current_location),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }

                    }

                    IconButton(
                        onClick = {
                            viewModel.getGeoForLocation(location)
                        },
                        modifier = Modifier.padding(top = 8.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = stringResource(id = R.string.search_location),
                        )
                    }


                }

                if (locations.locations != null) {
                    SelectDesiredLocation(
                        locations = locations.locations!!,
                        onLocationSelected = { selectedLocationGeo = it }
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.no_locations_found),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Button(
                onClick = {
                    viewModel.setAPIKey(apiKey)
                    viewModel.selectLocation(selectedLocationGeo)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_check_24),
                        contentDescription = stringResource(id = R.string.save),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = stringResource(id = R.string.save),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

    }
}

@Composable
fun SettingCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = MaterialTheme.shapes.large
            ),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            Text(
                text = title,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            content()
        }
    }
}

@Composable
fun SelectDesiredLocation(
    locations: List<WeatherLocationGeo>,
    onButtonClick: (() -> Unit) = {},
    onLocationSelected: (WeatherLocationGeo) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.padding(16.dp)
    ) {
        IconButton(onClick = {
            expanded = !expanded
            onButtonClick()
        }) {
            Icon(Icons.AutoMirrored.Rounded.List, contentDescription = "Select Location")
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                locations.forEach { location ->
                    Text(
                        text = "${location.locationName}, ${location.country}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                onLocationSelected(location)
                                expanded = false
                            }
                    )
                }
            }
        }
    }
}