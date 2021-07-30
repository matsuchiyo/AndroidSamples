package com.matsuchiyo.customviews.circleprogressview

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.min

class CircleProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val totalPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
    }

    private val progressPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
    }

    private var totalColor: Int = 0
    private var progressColor: Int = 0

    private var progress: Float = 0f

    fun setUp(totalColor: Int, progressColor: Int) {
        this.totalColor = totalColor
        this.progressColor = progressColor
    }

    fun update(progress: Float) {
        this.progress = progress
        invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val strokeWidth: Float = min(width, height) * (4 / 60f)

        totalPaint.apply {
            this.color = totalColor
            this.strokeWidth = strokeWidth
        }

        progressPaint.apply {
            this.color = progressColor
            this.strokeWidth = strokeWidth
        }

        val halfStrokeWidth = strokeWidth / 2
        val left: Float = halfStrokeWidth
        val top: Float = halfStrokeWidth
        val right: Float = width - halfStrokeWidth
        val bottom: Float = height - halfStrokeWidth

        canvas.drawArc(left, top, right, bottom, -90f, 360f, false, totalPaint)
        canvas.drawArc(left, top, right, bottom, -90f, 360f * progress, false, progressPaint)
    }
}