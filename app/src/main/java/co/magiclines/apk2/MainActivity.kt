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
    var status by remember { mutableStateOf("Offline mode — account status not checked") }

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
                    Text("Connection: Offline", color = accent)
                    Spacer(Modifier.height(8.dp))
                    Text("ACCOUNT STATUS", color = accent)
                    Text(status, color = Color.White)
                }
            }

            Spacer(Modifier.height(22.dp))

            Button(
                onClick = { status = "Offline interface is ready. Online account verification is unavailable." },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = accent)
            ) {
                Text("OPEN OFFLINE MODE", color = Color.Black)
            }

            Spacer(Modifier.height(15.dp))

            Text(
                "Running locally on this device",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}
