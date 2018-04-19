package com.test.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*

class CustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var paint: Paint = Paint()
    private var path: Path = Path()
    private val defaultBarWidth = resources.getDimensionPixelSize(R.dimen.default_width)
    private val defaultBarHeight = resources.getDimensionPixelSize(R.dimen.default_height)

    init {
        paint.strokeWidth = 5f
        paint.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            View.MeasureSpec.EXACTLY -> widthSize
            View.MeasureSpec.AT_MOST -> defaultBarWidth
            View.MeasureSpec.UNSPECIFIED -> defaultBarWidth
            else -> defaultBarWidth
        }

        val height = when (heightMode) {
            View.MeasureSpec.EXACTLY -> heightSize
            View.MeasureSpec.AT_MOST -> defaultBarHeight
            View.MeasureSpec.UNSPECIFIED -> defaultBarHeight
            else -> defaultBarHeight
        }

        path.moveTo(0f,0f)
        loadLines()

        setMeasuredDimension(width, height)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paint.shader = LinearGradient(0f, 0f, 0f, h.toFloat(), Color.BLACK, Color.WHITE, Shader.TileMode.CLAMP)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(path, paint)
    }

    fun changeGradient(startColor: Int, endColor: Int) {
        path.rewind()
        paint.shader = LinearGradient(0f, 0f, 0f, height.toFloat(), startColor, endColor, Shader.TileMode.CLAMP)
        loadLines()
        invalidate()
    }

    private fun loadLines() {
        for (index in 0..width) {
            if (index == 0 || index == width) {
                path.lineTo(index.toFloat(), 0f)
                continue
            }

            path.lineTo(index.toFloat(), (0..height).random().toFloat())
        }
    }

    private fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) +  start
}