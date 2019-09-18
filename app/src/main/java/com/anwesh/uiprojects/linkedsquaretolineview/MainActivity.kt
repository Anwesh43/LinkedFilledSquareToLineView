package com.anwesh.uiprojects.linkedsquaretolineview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.filledsquaretolineview.FilledSquareToLineView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FilledSquareToLineView.create(this)
    }
}
