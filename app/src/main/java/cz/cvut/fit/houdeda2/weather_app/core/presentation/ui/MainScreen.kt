package cz.cvut.fit.houdeda2.weather_app.core.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    Column {
        val navController = rememberNavController()

        Navigation(navController = navController, modifier = Modifier.weight(1f))
        val currentEntry by navController.currentBackStackEntryAsState()
        val currentDestination = currentEntry?.destination

        BottomAppBar(containerColor = MaterialTheme.colorScheme.tertiaryContainer) {
            Screens.TopLevel.all.forEach { screen ->
                NavigationBarItem(
                    painter = painterResource(id = screen.icon),
                    name = stringResource(screen.title),
                    selected = currentDestination?.hierarchy?.any { it.hasRoute(screen::class) } == true,
                    onClick = {
                        navController.navigate(screen) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

    }
}

@Composable
private fun RowScope.NavigationBarItem(
    painter: Painter,
    name: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onTertiaryContainer
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.onTertiary,
        ),
        icon = {
            Icon(painter = painter, contentDescription = null, tint = contentColor)
        },
        label = {
            Text(text = name, style = MaterialTheme.typography.labelMedium, color = contentColor)
        }
    )
}