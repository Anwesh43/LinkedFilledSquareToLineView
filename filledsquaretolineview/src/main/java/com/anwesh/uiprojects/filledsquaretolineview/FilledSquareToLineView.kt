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
import java.lang.Exception

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

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    cb()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class FSPNode(var i : Int, val state : State = State()) {

        private var next : FSPNode? = null
        private var prev : FSPNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = FSPNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawFSPNode(i, state.scale, paint)
            next?.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : FSPNode {
            var curr : FSPNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class FilledSquare(var i : Int) {

        private val root : FSPNode = FSPNode(0)
        private var curr : FSPNode = root
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            root.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : FilledSquareToLineView) {

        private val animator : Animator = Animator(view)
        private val fsp : FilledSquare = FilledSquare(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(backColor)
            fsp.draw(canvas, paint)
            animator.animate {
                fsp.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            fsp.startUpdating {
                animator.start()
            }
        }
    }
}
