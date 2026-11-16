package co.magiclines.apk2

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    private val captureRequestCode = 4107

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MagicLinesScreen(
                onEnableFloating = { enableFloatingMenu() },
                onOpenSettings = { openLocalSettings() },
                onStartCapture = { requestScreenCapture() },
                onStopCapture = { stopService(Intent(this, ScreenCaptureService::class.java)) }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
            startService(Intent(this, FloatingMenuService::class.java))
        }
    }

    @Deprecated("Deprecated in Android API; retained for compatibility with minSdk 23")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == captureRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            val serviceIntent = Intent(this, ScreenCaptureService::class.java).apply {
                putExtra(ScreenCaptureService.EXTRA_RESULT_CODE, resultCode)
                putExtra(ScreenCaptureService.EXTRA_RESULT_DATA, data)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(serviceIntent)
            else startService(serviceIntent)
        }
    }

    private fun requestScreenCapture() {
        val manager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(manager.createScreenCaptureIntent(), captureRequestCode)
    }

    private fun enableFloatingMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivity(intent)
        } else {
            startService(Intent(this, FloatingMenuService::class.java))
        }
    }

    private fun openLocalSettings() {
        startActivity(Intent(this, LocalSettingsActivity::class.java))
    }
}

@Composable
fun MagicLinesScreen(
    onEnableFloating: () -> Unit,
    onOpenSettings: () -> Unit,
    onStartCapture: () -> Unit,
    onStopCapture: () -> Unit
) {
    val background = Color(0xFF10131D)
    val panel = Color(0xFF201E25)
    val accent = Color(0xFFE8C99A)
    val teal = Color(0xFF35D6C2)
    var status by remember { mutableStateOf("Capture is off. No screen is being recorded.") }

    MaterialTheme {
        Box(Modifier.fillMaxSize().background(background)) {
            Image(
                painter = painterResource(R.drawable.sbg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.34f
            )
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(12.dp))
                Image(
                    painter = painterResource(R.drawable.layr5),
                    contentDescription = "MagicLines 8 logo",
                    modifier = Modifier.fillMaxWidth(0.82f).height(150.dp),
                    contentScale = ContentScale.Fit
                )
                Text("VERSION 5.0", color = accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(18.dp))
                Image(
                    painter = painterResource(R.drawable.slides_1),
                    contentDescription = "Welcome to MagicLines",
                    modifier = Modifier.fillMaxWidth().height(158.dp).clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(18.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = panel.copy(alpha = 0.94f)),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text("8 BALL POOL", color = accent, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(9.dp).clip(RoundedCornerShape(50)).background(teal))
                            Spacer(Modifier.width(9.dp))
                            Text("VISUAL GUIDE • MANUAL PLAY", color = teal, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(Modifier.height(14.dp))
                        Text("SCREEN CAPTURE", color = accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(status, color = Color.White, fontSize = 13.sp)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        status = "Android will ask you to approve screen capture."
                        onStartCapture()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = teal)
                ) { Text("START SCREEN ANALYSIS", color = Color(0xFF10131D), fontWeight = FontWeight.Bold) }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        onStopCapture()
                        status = "Capture stopped."
                    },
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = accent)
                ) { Text("STOP CAPTURE") }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onEnableFloating,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accent)
                ) { Text("ENABLE FLOATING MENU", color = Color(0xFF17131A), fontWeight = FontWeight.Bold) }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = accent)
                ) { Text("LOCAL SETTINGS", fontWeight = FontWeight.SemiBold) }
                Spacer(Modifier.weight(1f))
                Text("NO AUTO-AIM  •  NO AUTOMATIC SHOTS", color = Color.LightGray.copy(alpha = 0.75f),
                    fontSize = 10.sp, letterSpacing = 1.sp)
            }
        }
    }
}
