package co.magiclines.apk2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class LocalSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("magiclines_local_settings", MODE_PRIVATE)
        setContent {
            val accent = Color(0xFF35D6C2)
            val background = Color(0xFF10131D)
            val panel = Color(0xFF1B2232)
            var showGuide by remember { mutableStateOf(prefs.getBoolean("show_guide", true)) }
            var manualGuideEnabled by remember { mutableStateOf(prefs.getBoolean("manual_guide_enabled", false)) }
            var keepPanelOpen by remember { mutableStateOf(prefs.getBoolean("keep_panel_open", false)) }
            var lineWidth by remember { mutableFloatStateOf(prefs.getFloat("line_width", 2f)) }
            var opacity by remember { mutableFloatStateOf(prefs.getFloat("opacity", 75f)) }
            var guideAngle by remember { mutableFloatStateOf(prefs.getFloat("guide_angle", -90f)) }
            var guideLength by remember { mutableFloatStateOf(prefs.getFloat("guide_length", 55f)) }

            MaterialTheme {
                Column(
                    Modifier.fillMaxSize().background(background).verticalScroll(rememberScrollState()).padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("LOCAL SETTINGS", color = accent, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text("Saved on this device • Offline", color = Color.LightGray, fontSize = 13.sp)
                    Card(colors = CardDefaults.cardColors(containerColor = panel), shape = RoundedCornerShape(16.dp)) {
                        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("INTERFACE", color = accent, fontWeight = FontWeight.Bold)
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Show guide labels", color = Color.White)
                                Switch(checked = showGuide, onCheckedChange = {
                                    showGuide = it; prefs.edit().putBoolean("show_guide", it).apply()
                                })
                            }
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Keep menu open", color = Color.White)
                                Switch(checked = keepPanelOpen, onCheckedChange = {
                                    keepPanelOpen = it; prefs.edit().putBoolean("keep_panel_open", it).apply()
                                })
                            }
                            Divider(color = Color.DarkGray)
                            Text("MANUAL GUIDE LINE", color = accent, fontWeight = FontWeight.Bold)
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Show guide line", color = Color.White)
                                Switch(checked = manualGuideEnabled, onCheckedChange = {
                                    manualGuideEnabled = it
                                    prefs.edit().putBoolean("manual_guide_enabled", it).apply()
                                })
                            }
                            Text("Angle: ${guideAngle.toInt()}°", color = Color.White)
                            Slider(value = guideAngle, onValueChange = {
                                guideAngle = it; prefs.edit().putFloat("guide_angle", it).apply()
                            }, valueRange = -180f..180f)
                            Text("Length: ${guideLength.toInt()}%", color = Color.White)
                            Slider(value = guideLength, onValueChange = {
                                guideLength = it; prefs.edit().putFloat("guide_length", it).apply()
                            }, valueRange = 10f..95f)
                            Divider(color = Color.DarkGray)
                            Text("LINE APPEARANCE", color = accent, fontWeight = FontWeight.Bold)
                            Text("Line width: ${lineWidth.toInt()}", color = Color.White)
                            Slider(value = lineWidth, onValueChange = {
                                lineWidth = it; prefs.edit().putFloat("line_width", it).apply()
                            }, valueRange = 1f..8f)
                            Text("Opacity: ${opacity.toInt()}%", color = Color.White)
                            Slider(value = opacity, onValueChange = {
                                opacity = it; prefs.edit().putFloat("opacity", it).apply()
                            }, valueRange = 10f..100f)
                        }
                    }
                    Text("This is a manually adjusted visual guide. It does not detect balls, calculate shots, or control gameplay.", color = Color.Gray, fontSize = 12.sp)
                    Button(onClick = { finish() }, modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = accent)) {
                        Text("DONE", color = Color.Black)
                    }
                }
            }
        }
    }
}
