package tw.bao.languagelearner.info.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import tw.bao.languagelearner.R

/**
 * Created by bao on 2017/12/16.
 */
class CircleView : View {
    companion object {
        private val LOG_TAG = CircleView::class.java.simpleName
    }

    private var mStrokeWidth = 0
    private var mForegroundStrokeColor = 0
    private var mBackgroundStrokeColor = 0
    private var mForegroundStrokePaint: Paint? = null
    private var mBackgroundCirclePaint: Paint? = null
    private var mCenterX = 0
    private var mCenterY = 0
    private var mCircleBounds: RectF? = null
    public var mUserExpPercent: Float = 50f

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, R.attr.circleViewStyle)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context?, attrs: AttributeSet?, defStyle: Int) {
        val attributes = context?.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyle, 0)
        attributes?.apply {
            mStrokeWidth = getDimension(R.styleable.CircleView_stroke_width, 10f).toInt()
            mForegroundStrokeColor = getColor(R.styleable.CircleView_foreground_stroke_color, Color.CYAN)
            mBackgroundStrokeColor = getColor(R.styleable.CircleView_background_stroke_color, Color.CYAN)
            recycle()
        }
    }


    private fun setupCircleParam(w: Int, h: Int) {
        mCenterX = w / 2
        mCenterY = h / 2
        if (mCircleBounds == null) {
            mCircleBounds = RectF()
        }
        mCircleBounds?.set(0f + mStrokeWidth, 0f + mStrokeWidth, w.toFloat() - mStrokeWidth, h.toFloat() - mStrokeWidth)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            val w = width
            val h = height
            if (w <= 0 || h <= 0) {
                return
            }
            setupCircleParam(w, h)
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mBackgroundCirclePaint == null) {
            mBackgroundCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mBackgroundCirclePaint?.style = Paint.Style.STROKE
            mBackgroundCirclePaint?.strokeWidth = mStrokeWidth.toFloat()
            mBackgroundCirclePaint?.color = mBackgroundStrokeColor
        }

        if (mForegroundStrokePaint == null) {
            mForegroundStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mForegroundStrokePaint?.style = Paint.Style.STROKE
            mForegroundStrokePaint?.strokeWidth = mStrokeWidth.toFloat()
            mForegroundStrokePaint?.color = mForegroundStrokeColor
        }
        mBackgroundCirclePaint?.apply {
            canvas?.drawArc(mCircleBounds, 0f, 360f, false, this)
        }
        mForegroundStrokePaint?.apply {
            val sweepAngle = 360f * (mUserExpPercent * 0.01f)
            Log.d(LOG_TAG, "Sweep angle : " + sweepAngle)
            canvas?.drawArc(mCircleBounds, 90f, sweepAngle, false, this)
        }
    }
}