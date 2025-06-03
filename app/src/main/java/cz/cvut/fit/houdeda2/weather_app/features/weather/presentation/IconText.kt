package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconText(
    painter: Painter,
    contentDescription: String,
    text: String,
    textStyle: TextStyle,
    iconSize: Dp = 24.dp
) {
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .padding(end = 4.dp)
            .size(iconSize),
        tint = MaterialTheme.colorScheme.onPrimaryContainer
    )
    Text(
        text = text,
        style = textStyle,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

@Composable
fun RowIconText(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    painter: Painter,
    contentDescription: String,
    text: String,
    textStyle: TextStyle,
    iconSize: Dp = 24.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        IconText(
            painter = painter,
            contentDescription = contentDescription,
            text = text,
            textStyle = textStyle,
            iconSize = iconSize
        )
    }
}