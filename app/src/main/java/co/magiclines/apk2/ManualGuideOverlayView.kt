package co.magiclines.apk2

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ManualGuideOverlayView(context: Context) : View(context) {
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var angleDegrees = -90f
    private var lengthPercent = 55f
    private var widthDp = 2f
    private var opacityPercent = 75f

    fun updateFromPreferences(preferences: SharedPreferences) {
        angleDegrees = preferences.getFloat("guide_angle", -90f).coerceIn(-180f, 180f)
        lengthPercent = preferences.getFloat("guide_length", 55f).coerceIn(10f, 95f)
        widthDp = preferences.getFloat("line_width", 2f).coerceIn(1f, 8f)
        opacityPercent = preferences.getFloat("opacity", 75f).coerceIn(10f, 100f)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (width <= 0 || height <= 0) return

        val density = resources.displayMetrics.density
        val startX = width * 0.5f
        val startY = height * 0.55f
        val maxLength = min(width, height) * 0.48f
        val length = maxLength * (lengthPercent / 100f)
        val radians = Math.toRadians(angleDegrees.toDouble())
        val endX = startX + (cos(radians) * length).toFloat()
        val endY = startY + (sin(radians) * length).toFloat()
        val alpha = (opacityPercent * 2.55f).toInt().coerceIn(0, 255)

        linePaint.color = Color.rgb(53, 214, 194)
        linePaint.alpha = alpha
        linePaint.strokeWidth = widthDp * density
        linePaint.strokeCap = Paint.Cap.ROUND
        linePaint.style = Paint.Style.STROKE
        canvas.drawLine(startX, startY, endX, endY, linePaint)

        linePaint.style = Paint.Style.FILL
        canvas.drawCircle(startX, startY, 5f * density, linePaint)
        canvas.drawCircle(endX, endY, 3f * density, linePaint)
    }
}
