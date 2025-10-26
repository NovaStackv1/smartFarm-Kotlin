// CustomWeatherIcon.kt in weather/presentation/components
package com.example.smartfarm.ui.features.weather.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.smartfarm.ui.features.weather.domain.models.WeatherIcon

@Composable
fun CustomWeatherIcon(
    weatherIcon: WeatherIcon,
    modifier: Modifier = Modifier,
    iconSize: Dp = 48.dp,
    isDayTime: Boolean = true
) {
    when (weatherIcon) {
        WeatherIcon.SUNNY -> {
            Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = "Sunny",
                modifier = modifier.size(iconSize)
            )
        }
        WeatherIcon.MOON -> {
            Icon(
                imageVector = Icons.Default.NightsStay,
                contentDescription = "Clear Night",
                modifier = modifier.size(iconSize)
            )
        }
        WeatherIcon.PARTLY_CLOUDY_DAY -> {
            // Sun with cloud underneath
            Box(
                modifier = modifier.size(iconSize),
                contentAlignment = Alignment.Center
            ) {
                // Cloud (behind)
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = "Partly Cloudy",
                    modifier = Modifier
                        .size(iconSize * 0.9f)
                        .offset(y = 4.dp)
                )
                // Sun (on top)
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize * 0.7f)
                        .offset(y = (-4).dp)
                )
            }
        }
        WeatherIcon.PARTLY_CLOUDY_NIGHT -> {
            // Moon with cloud underneath
            Box(
                modifier = modifier.size(iconSize),
                contentAlignment = Alignment.Center
            ) {
                // Cloud (behind)
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = "Partly Cloudy Night",
                    modifier = Modifier
                        .size(iconSize * 0.9f)
                        .offset(y = 4.dp)
                )
                // Moon (on top)
                Icon(
                    imageVector = Icons.Default.NightsStay,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize * 0.7f)
                        .offset(y = (-4).dp)
                )
            }
        }
        WeatherIcon.CLOUDY -> {
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = "Cloudy",
                modifier = modifier.size(iconSize)
            )
        }
        WeatherIcon.RAINY -> {
            Box(
                modifier = modifier.size(iconSize),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = "Rainy",
                    modifier = Modifier.size(iconSize * 0.9f)
                )
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize * 0.5f)
                        .offset(y = 8.dp)
                )
            }
        }
        WeatherIcon.STORMY -> {
            Box(
                modifier = modifier.size(iconSize),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = "Stormy",
                    modifier = Modifier.size(iconSize * 0.9f)
                )
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize * 0.5f)
                        .offset(y = 4.dp)
                )
            }
        }
        WeatherIcon.SNOWY -> {
            Box(
                modifier = modifier.size(iconSize),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = "Snowy",
                    modifier = Modifier.size(iconSize * 0.9f)
                )
                Icon(
                    imageVector = Icons.Default.AcUnit,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconSize * 0.5f)
                        .offset(y = 8.dp)
                )
            }
        }
        WeatherIcon.FOGGY -> {
            Icon(
                imageVector = Icons.Default.BlurOn,
                contentDescription = "Foggy",
                modifier = modifier.size(iconSize)
            )
        }
        WeatherIcon.WINDY -> {
            Icon(
                imageVector = Icons.Default.Air,
                contentDescription = "Windy",
                modifier = modifier.size(iconSize)
            )
        }
    }
}