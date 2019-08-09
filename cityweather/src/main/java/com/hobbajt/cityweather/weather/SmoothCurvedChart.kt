package com.hobbajt.cityweather.weather

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.hobbajt.cityweather.R
import com.hobbajt.cityweather.weather.model.ChartValue
import com.hobbajt.core.util.dpToPx
import com.hobbajt.core.util.spToPx
import kotlin.math.max
import kotlin.math.roundToInt


class SmoothCurvedChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        private const val SMOOTHNESS = 0.3f
        private const val FONT_NAME = "sans-serif-light"
    }

    private val valuesPathSize = 2.dpToPx()
    private val oneSectionMinWidth = 70.dpToPx().roundToInt()
    private val subtitlesBottomPadding = 8.dpToPx()
    private val graphTopPadding = 12.dpToPx()
    private val subtitlesAreaHeight = 36.dpToPx()
    private val valuesBottomPadding = 12.dpToPx()
    private val graphHorizontalEdgesPadding = 24.dpToPx()

    private val subtitleTextSize = 16.spToPx()
    private val valueTextSize = 18.spToPx()
    private val font = Typeface.create(FONT_NAME, Typeface.NORMAL)

    private var textColor: Int = 0
    private var valueAreaColor: Int = 0
    private var valueLineColor: Int = 0

    private val path: Path = Path()
    private var fullPath: Path = Path()
    private var onePointWidth = 0
    private var onePointHeight: Float = 0F

    var values: List<ChartValue> = emptyList()
        set(value) {
            field = value
            val values = value.map { it.value }.toFloatArray()
            scaleMinValue = (values.minBy { it } ?: 0F).roundToInt() - 1
            scaleMaxValue = (values.maxBy { it } ?: 0F).roundToInt() + 1
            invalidate()
        }

    private var backgroundVerticalLines: List<Float> = emptyList()
    private var points: List<PointF> = emptyList()

    private var scaleMinValue: Int = 0
    private var scaleMaxValue: Int = 0

    private val paint: Paint = Paint()

    override fun onFinishInflate() {
        super.onFinishInflate()
        initColors()
    }

    private fun initColors() {
        textColor = ContextCompat.getColor(context, R.color.white)
        valueAreaColor = ContextCompat.getColor(context, R.color.transparentGrey)
        valueLineColor = ContextCompat.getColor(context, R.color.lightOrange)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        if (points.isNotEmpty()) {
            onePointHeight = (height - subtitlesAreaHeight - graphTopPadding - valueTextSize.toInt()) / (scaleMaxValue - scaleMinValue)
            onePointWidth = max(width / (points.count()), oneSectionMinWidth)
        }

        val measuredWidth = MeasureSpec.makeMeasureSpec(
            onePointWidth * (points.count() - 1) + 2 * graphHorizontalEdgesPadding.toInt(),
            MeasureSpec.EXACTLY
        )
        setMeasuredDimension(measuredWidth, heightMeasureSpec)

        if (height != 0) {
            points = calculatePoints(height)
            backgroundVerticalLines = calculateBackgroundVerticalLines(height)
            calculateSmoothPath()
            calculateFilledAreaPath(height)
        }
    }

    private fun calculateSmoothPath() {
        path.reset()
        var slopeX = 0f
        var slopeY = 0f
        path.moveTo(points[0].x, points[0].y)
        for (i in 1 until points.count()) {
            val currentPoint = points[i]
            val previousPoint = points[i - 1]
            val nextPoint = points[if (i + 1 < points.count()) i + 1 else i]

            val leftControlPoint = PointF(previousPoint.x + slopeX, previousPoint.y + slopeY)

            slopeX = (nextPoint.x - previousPoint.x) / 2 * SMOOTHNESS
            slopeY = (nextPoint.y - previousPoint.y) / 2 * SMOOTHNESS

            val rightControlPoint = PointF(currentPoint.x - slopeX, currentPoint.y - slopeY)

            path.cubicTo(
                leftControlPoint.x, leftControlPoint.y,
                rightControlPoint.x, rightControlPoint.y,
                currentPoint.x, currentPoint.y
            )
        }
    }

    private fun calculateFilledAreaPath(height: Int) {
        fullPath.reset()
        fullPath = Path(path)
        fullPath.lineTo(points.last().x, height - subtitlesAreaHeight)
        fullPath.lineTo(points.first().x, height - subtitlesAreaHeight)
    }

    private fun calculateBackgroundVerticalLines(height: Int): List<Float> {
        return points
            .map { arrayOf(it.x, height.toFloat() - subtitlesAreaHeight.toInt(), it.x, it.y) }
            .toTypedArray()
            .flatten()

    }

    private fun calculatePoints(height: Int): List<PointF> {
        return values.mapIndexed { index, value ->
            PointF(
                index * onePointWidth.toFloat() + graphHorizontalEdgesPadding,
                height - ((value.value - scaleMinValue) * onePointHeight) - subtitlesAreaHeight
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawValueArea(canvas)
        drawValuePath(canvas)
        drawValues(canvas)
        drawTitles(canvas)
    }

    private fun drawValueArea(canvas: Canvas) {
        paint.color = valueAreaColor
        paint.strokeWidth = valuesPathSize
        paint.style = Paint.Style.FILL
        canvas.drawPath(fullPath, paint)
    }


    private fun drawValuePath(canvas: Canvas) {
        paint.color = valueLineColor
        paint.strokeWidth = valuesPathSize
        paint.style = Paint.Style.STROKE
        canvas.drawPath(path, paint)
    }

    private fun drawValues(canvas: Canvas) {
        configureTextPaint()
        paint.textSize = valueTextSize
        values.zip(points).forEach { valueAndPoint ->
            canvas.drawText(
                valueAndPoint.first.valueTitle,
                valueAndPoint.second.x,
                valueAndPoint.second.y - valuesBottomPadding,
                paint
            )
        }
    }

    private fun drawTitles(canvas: Canvas) {
        configureTextPaint()
        paint.textSize = subtitleTextSize
        values.zip(points).forEach { value ->
            canvas.drawText(
                value.first.title,
                value.second.x,
                height - subtitlesBottomPadding,
                paint
            )
        }
    }

    private fun configureTextPaint() {
        paint.color = textColor
        paint.textAlign = Paint.Align.CENTER
        paint.style = Paint.Style.FILL
        paint.typeface = font
        paint.flags = TextPaint.ANTI_ALIAS_FLAG
    }
}
