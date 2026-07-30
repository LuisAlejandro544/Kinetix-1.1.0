package com.example.executor

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.R
import com.example.data.Shortcut
import com.example.data.ShortcutDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AssistiveTouchService : Service() {

    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private var popupView: View? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private var isPopupShowing = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        setupFloatingButton()
    }

    private fun setupFloatingButton() {
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        floatingView = layoutInflater.inflate(R.layout.layout_assistive_touch_button, null)

        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 300
        }

        try {
            windowManager?.addView(floatingView, params)
        } catch (e: Exception) {
            FileLogManager.logWarning(this, "AssistiveTouchError", "Error agregando vista flotante: ${e.localizedMessage}")
            stopSelf()
            return
        }

        floatingView?.setOnTouchListener(object : View.OnTouchListener {
            private var isClick = false

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event == null) return false
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        isClick = true
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = (event.rawX - initialTouchX).toInt()
                        val dy = (event.rawY - initialTouchY).toInt()
                        if (Math.abs(dx) > 10 || Math.abs(dy) > 10) {
                            isClick = false
                        }
                        params.x = initialX + dx
                        params.y = initialY + dy
                        try {
                            windowManager?.updateViewLayout(floatingView, params)
                        } catch (_: Exception) {}
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (isClick) {
                            toggleQuickPopup(params.x, params.y)
                        }
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun toggleQuickPopup(touchX: Int, touchY: Int) {
        if (isPopupShowing) {
            dismissPopup()
            return
        }

        serviceScope.launch(Dispatchers.IO) {
            val db = ShortcutDatabase.getDatabase(applicationContext)
            val shortcuts = db.shortcutDao().getAllShortcutsList()

            withContext(Dispatchers.Main) {
                showPopup(shortcuts, touchX, touchY)
            }
        }
    }

    private fun showPopup(shortcuts: List<Shortcut>, xPos: Int, yPos: Int) {
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        popupView = layoutInflater.inflate(R.layout.layout_assistive_touch_menu, null)

        val container = popupView?.findViewById<LinearLayout>(R.id.assistive_menu_container)

        if (shortcuts.isEmpty()) {
            val emptyTv = TextView(this).apply {
                text = "No hay atajos creados"
                setTextColor(0xFFFFFFFF.toInt())
                setPadding(24, 24, 24, 24)
            }
            container?.addView(emptyTv)
        } else {
            shortcuts.take(6).forEach { shortcut ->
                val btn = TextView(this).apply {
                    text = "⚡ ${shortcut.name}"
                    setTextColor(0xFF34D399.toInt())
                    setPadding(32, 20, 32, 20)
                    textSize = 14f
                    setOnClickListener {
                        BackgroundExecutor.executeShortcutInBackground(applicationContext, shortcut)
                        dismissPopup()
                    }
                }
                container?.addView(btn)
            }
        }

        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val popupParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = xPos
            y = yPos + 120
        }

        try {
            windowManager?.addView(popupView, popupParams)
            isPopupShowing = true
        } catch (e: Exception) {
            FileLogManager.logWarning(this, "AssistiveTouchPopupError", e.localizedMessage ?: "")
        }
    }

    private fun dismissPopup() {
        if (isPopupShowing && popupView != null) {
            try {
                windowManager?.removeView(popupView)
            } catch (_: Exception) {}
            popupView = null
            isPopupShowing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissPopup()
        if (floatingView != null) {
            try {
                windowManager?.removeView(floatingView)
            } catch (_: Exception) {}
            floatingView = null
        }
        serviceScope.cancel()
    }

    companion object {
        fun start(context: Context) {
            try {
                val intent = Intent(context, AssistiveTouchService::class.java)
                context.startService(intent)
            } catch (e: Exception) {
                FileLogManager.logWarning(context, "AssistiveTouchStartError", e.localizedMessage ?: "")
            }
        }

        fun stop(context: Context) {
            try {
                val intent = Intent(context, AssistiveTouchService::class.java)
                context.stopService(intent)
            } catch (_: Exception) {}
        }
    }
}
