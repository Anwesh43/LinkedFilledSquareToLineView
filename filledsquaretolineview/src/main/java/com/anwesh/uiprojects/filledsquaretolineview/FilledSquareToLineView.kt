package com.anwesh.uiprojects.filledsquaretolineview

/**
 * Created by anweshmishra on 18/09/19.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.content.Context
import android.app.Activity

val nodes : Int = 5
val parts : Int = 2
val scGap : Float = 0.01f
val color1 : Int = Color.parseColor("#9C27B0")
val color2 : Int = Color.parseColor("#4CAF50")
val backColor : Int = Color.parseColor("#BDBDBD")
val sizeFactor : Float = 2.9f
val delay : Long = 25

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawFilledSquareToLine(i : Int, sc : Float, size : Float, paint : Paint) {
    val sci : Float = sc.divideScale(i, parts)
    save()
    scale(1f - 2 * i, 1f)
    paint.color = color1
    drawRect(RectF(0f, -size, size * sci, size), paint)
    paint.color = color2
    drawRect(RectF(size * (1 - sci), -size, size, size), paint)
    restore()
}

fun Canvas.drawFilledSquareParts(sc : Float, size : Float, paint : Paint) {
    for (j in 0..(parts - 1)) {
        drawFilledSquareToLine(j, sc, size, paint)
    }
}

fun Canvas.drawFSPNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    val size : Float = gap / sizeFactor
    save()
    translate(gap * (i + 1), h / 2)
    drawFilledSquareParts(scale, size, paint)
    restore()
}

class FilledSquareToLineView(ctx : Context)  : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }
}
