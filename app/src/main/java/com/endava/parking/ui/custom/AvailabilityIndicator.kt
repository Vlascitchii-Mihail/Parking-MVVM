package com.endava.parking.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.endava.parking.R
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private const val MIN_VALUE = 0
private const val MAX_VALUE = 100
private const val GREEN_YELLOW_THRESHOLD = 33
private const val YELLOW_RED_THRESHOLD = 66

class AvailabilityIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val center = PointF()
    private val circleRect = RectF()
    private val segment = Path()
    private val fillPaint = Paint()
    private val strokePaint = Paint()
    private val notAvailableStrokePaint = Paint()

    private var radius = 0
    private var fillColor = 0
    private var strokeColor = 0
    private var strokeWidth = 0f
    private var value = 0
    private var closedMode = false

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.AvailabilityIndicator, 0, 0
        )
        try {
            fillColor = typedArray.getColor(R.styleable.AvailabilityIndicator_circleFillColor, Color.WHITE)
            strokeColor = typedArray.getColor(R.styleable.AvailabilityIndicator_circleStrokeColor, Color.BLACK)
            strokeWidth = typedArray.getFloat(R.styleable.AvailabilityIndicator_circleStrokeWidth, 1f)
            value = typedArray.getInteger(R.styleable.AvailabilityIndicator_value, 0)
            notAvailableStrokePaint.style = Paint.Style.STROKE
            adjustValue(value)
            adjustColor(value)
            setPaths()
        } finally {
            typedArray.recycle()
        }
        fillPaint.color = fillColor
        strokePaint.color = strokeColor
        strokePaint.strokeWidth = strokeWidth
        strokePaint.style = Paint.Style.STROKE
    }

    fun setTemporaryClosedMode(temporaryClosed: Boolean) {
        closedMode = temporaryClosed
    }

    fun changeLevel(value: Int) {
        adjustValue(value)
        adjustColor(value)
        setPaths()
        invalidate()
    }

    private fun adjustValue(value: Int) {
        this.value = MAX_VALUE.coerceAtMost(MIN_VALUE.coerceAtLeast(value))
    }

    private fun adjustColor(value: Int) {
        when (value) {
            in MIN_VALUE..GREEN_YELLOW_THRESHOLD -> fillPaint.color = context.getColor(R.color.japanese_laurel)
            in GREEN_YELLOW_THRESHOLD..YELLOW_RED_THRESHOLD -> fillPaint.color = context.getColor(R.color.yellow)
            in YELLOW_RED_THRESHOLD..MAX_VALUE -> fillPaint.color = context.getColor(R.color.persian_red)
        }
    }

    private fun setPaths() {
        val y: Float = center.y + radius - (2 * radius * value / 100 - 1)
        val x: Float = center.x - sqrt(radius.toDouble().pow(2.0) - (y - center.y).toDouble().pow(2.0)).toFloat()
        val angle = Math.toDegrees(atan(((center.y - y) / (x - center.x)).toDouble())).toFloat()
        val startAngle = 180 - angle
        val sweepAngle = 2 * angle - 180
        segment.rewind()
        segment.addArc(circleRect, startAngle, sweepAngle)
        segment.close()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!closedMode) {
            canvas?.drawPath(segment, fillPaint)
            canvas?.drawCircle(center.x, center.y, radius.toFloat(), strokePaint)
            return
        } else {
            notAvailableStrokePaint.strokeWidth = radius * 0.2F
            val x1 = center.x + radius * cos(45F)
            val y1 = center.y + radius * sin(45F)
            val x2 = center.x + radius * cos(180F)
            val y2 = center.y + radius * sin(180F)
            canvas?.drawLine(x1, y1, x2, y2, notAvailableStrokePaint)
            canvas?.drawCircle(center.x, center.y, (radius - notAvailableStrokePaint.strokeWidth / 2), notAvailableStrokePaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        center.x = (width / 2).toFloat()
        center.y = (height / 2).toFloat()
        radius = width.coerceAtMost(height) / 2 - strokeWidth.toInt()
        circleRect.set(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
        setPaths()
    }
}
