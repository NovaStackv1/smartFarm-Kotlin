package com.example.smartfarm.ui.features.home.presentation.components


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WelcomeHeader(
    userName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = getGreeting(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = " 🌾",
                    fontSize = 24.sp
                )
            }
        }

//        IconButton(onClick = onMenuClick) {
//            Icon(
//                imageVector = Icons.Default.Menu,
//                contentDescription = "Menu",
//                tint = MaterialTheme.colorScheme.onBackground
//            )
//        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun getGreeting(): String {
    return when (LocalTime.now().hour) {
        in 5..11 -> "Happy Rising" // 5:00 AM to 11:59 AM
        in 12..16 -> "Hello There" // 12:00 PM to 4:59 PM
        in 17..21 -> "Good Evening" // 5:00 PM to 9:59 PM
        else -> "Hey!" // 10:00 PM to 4:59 AM
    }
}