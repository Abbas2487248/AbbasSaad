package co.magiclines.apk2

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView

class FloatingMenuService : Service() {
    private lateinit var windowManager: WindowManager
    private var bubble: TextView? = null
    private var menu: LinearLayout? = null
    private lateinit var bubbleParams: WindowManager.LayoutParams
    private var menuParams: WindowManager.LayoutParams? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !android.provider.Settings.canDrawOverlays(this)) {
            stopSelf()
            return
        }
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        showBubble()
    }

    private fun overlayParams(width: Int, height: Int): WindowManager.LayoutParams {
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else WindowManager.LayoutParams.TYPE_PHONE
        return WindowManager.LayoutParams(
            width, height, type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START }
    }

    private fun showBubble() {
        val view = TextView(this).apply {
            text = "ML"
            textSize = 16f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
            setBackgroundColor(Color.rgb(53, 214, 194))
            elevation = 12f
        }
        bubbleParams = overlayParams(58, 58).apply { x = 24; y = 180 }
        view.setOnTouchListener(object : View.OnTouchListener {
            private var downX = 0
            private var downY = 0
            private var startX = 0
            private var startY = 0
            private var moved = false
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downX = event.rawX.toInt(); downY = event.rawY.toInt()
                        startX = bubbleParams.x; startY = bubbleParams.y; moved = false
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = event.rawX.toInt() - downX
                        val dy = event.rawY.toInt() - downY
                        if (kotlin.math.abs(dx) > 6 || kotlin.math.abs(dy) > 6) moved = true
                        bubbleParams.x = startX + dx; bubbleParams.y = startY + dy
                        windowManager.updateViewLayout(view, bubbleParams)
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (!moved) toggleMenu()
                        return true
                    }
                }
                return false
            }
        })
        bubble = view
        windowManager.addView(view, bubbleParams)
    }

    private fun toggleMenu() {
        if (menu != null) {
            try { windowManager.removeView(menu) } catch (_: Exception) {}
            menu = null
            return
        }
        val panel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 18, 24, 18)
            setBackgroundColor(Color.rgb(27, 34, 50))
            elevation = 16f
        }
        fun addLine(label: String, color: Int = Color.WHITE, action: (() -> Unit)? = null) {
            val item = TextView(this).apply {
                text = label; textSize = 15f; setTextColor(color)
                setPadding(8, 12, 8, 12)
                setOnClickListener { action?.invoke() }
            }
            panel.addView(item)
        }
        addLine("MAGICLINES  •  OFFLINE", Color.rgb(53, 214, 194))
        addLine("Local settings — coming next", Color.LTGRAY)
        addLine("Close menu", Color.WHITE) { toggleMenu() }
        menuParams = overlayParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT).apply {
            x = bubbleParams.x
            y = bubbleParams.y + 66
        }
        menu = panel
        windowManager.addView(panel, menuParams)
    }

    override fun onDestroy() {
        try { bubble?.let { windowManager.removeView(it) } } catch (_: Exception) {}
        try { menu?.let { windowManager.removeView(it) } } catch (_: Exception) {}
        bubble = null; menu = null
        super.onDestroy()
    }
}