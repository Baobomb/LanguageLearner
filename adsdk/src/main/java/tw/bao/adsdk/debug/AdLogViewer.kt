package tw.bao.adsdk.debug

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gogolook.adsdk.R
import tw.bao.adsdk.Definition

/**
 * Created by chenweilun on 2017/10/18.
 */

//Only use in develop mode
@SuppressLint("StaticFieldLeak")
object AdLogViewer {
    @JvmStatic
    var isActive = false

    private val mWindowLayoutParams: WindowManager.LayoutParams by lazy { WindowManager.LayoutParams() }
    private var mWindowManager: WindowManager? = null
    private var mMetrics: DisplayMetrics? = null
    private var mIsExpand = true
    private var mAdUnit = Definition.AdUnit.INFO
    private var mLlLogViewer: LinearLayout? = null
    private var mTvLog: TextView? = null
    private var mHsvAdTag: HorizontalScrollView? = null

    @JvmStatic
    fun showViewer(context: Context) {
        try {
            mMetrics = context.resources.displayMetrics
            if (mWindowManager == null) {
                mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            }

            mWindowLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            mWindowLayoutParams.format = PixelFormat.TRANSPARENT
            mWindowLayoutParams.gravity = Gravity.BOTTOM
            mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
            mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            mWindowLayoutParams.x = 0
            mMetrics?.apply {
                mWindowLayoutParams.y = (heightPixels * 0.1f).toInt()
            }

            if (mLlLogViewer == null) {
                val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                mLlLogViewer = layoutInflater.inflate(R.layout.ad_loger_viewer_layout, null) as LinearLayout
            }

            mHsvAdTag = mLlLogViewer?.findViewById(R.id.hsv_ad_log_viewer_tag)
            mTvLog = mLlLogViewer?.findViewById(R.id.et_ad_log_viewer_log)
            mTvLog?.text = ""
            mTvLog?.movementMethod = ScrollingMovementMethod()

            val controlButton: ImageView? = mLlLogViewer?.findViewById(R.id.iv_ad_log_viewer_control)
            controlButton?.setOnClickListener {
                if (mIsExpand) {
                    collapseLogViewer()
                } else {
                    expandLogViewer()
                }
            }
            mLlLogViewer?.findViewById<TextView>(R.id.tv_ad_log_viewer_block)?.setOnClickListener {
                mAdUnit = Definition.AdUnit.INFO
                refreshDisplay(Definition.AdUnit.INFO)
            }


            addSystemAlertView(context, mWindowManager, mLlLogViewer, mWindowLayoutParams)
            isActive = true
        } catch (e: Exception) {
            Log.d("AdLogViewer", e.toString())
        }

    }

    @JvmStatic
    fun hideViewer() {
        isActive = false
        try {
            mWindowManager?.removeView(mLlLogViewer)
        } catch (e: Exception) {
        }
    }

    private fun expandLogViewer() {
        if (!isActive) {
            return
        }
        mTvLog?.apply {
            visibility = View.VISIBLE
            mIsExpand = true
        }
    }

    private fun collapseLogViewer() {
        if (!isActive) {
            return
        }
        mTvLog?.apply {
            visibility = View.GONE
            mIsExpand = false
        }
    }

    fun refreshDisplay(adUnit: Definition.AdUnit) {
        if (adUnit != mAdUnit) {
            return
        }
        if (!isActive) {
            return
        }
        mTvLog?.apply {
            post {
                mTvLog?.text = ""
                mTvLog?.append(AdLog.getAdLog(mAdUnit))
            }
        }
    }

    private fun addSystemAlertView(context: Context, windowManager: WindowManager?, view: View?, windowLayoutParams: WindowManager.LayoutParams) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
            return
        }
        if (windowManager == null || view == null) {
            return
        }
        try {
            windowManager.addView(view, windowLayoutParams)
        } catch (e: SecurityException) {
            Log.d("AdLogViewer", e.toString())
        }

    }
}
