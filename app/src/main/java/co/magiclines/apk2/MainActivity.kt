package co.magiclines.apk2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MagicLinesScreen()
        }
    }
}

@Composable
fun MagicLinesScreen() {
    val background = Color(0xFF10131D)
    val panel = Color(0xFF1B2232)
    val accent = Color(0xFF35D6C2)

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(30.dp))

            Text(
                "MAGICLINES",
                color = accent,
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Version 5.0",
                color = Color.LightGray,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(30.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = panel),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("8 Ball Pool", color = Color.White, fontSize = 20.sp)
                    Spacer(Modifier.height(12.dp))
                    Text("Status: Not Installed", color = Color.LightGray)
                    Spacer(Modifier.height(8.dp))
                    Text("ACCOUNT STATUS", color = accent)
                    Text("Current State: Checking...", color = Color.White)
                }
            }

            Spacer(Modifier.height(22.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = accent)
            ) {
                Text("INITIALIZE", color = Color.Black)
            }

            Spacer(Modifier.height(15.dp))

            Text(
                "MagicLines Running...",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}
