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
val delay : Long = 25
