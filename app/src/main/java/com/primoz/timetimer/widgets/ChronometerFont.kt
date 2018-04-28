package com.primoz.timetimer.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.Chronometer
import com.primoz.timetimer.extras.FontManager

/**
 * Created by Primož on 21/04/2018.
 */
class ChronometerFont @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : Chronometer(context, attrs, defStyleAttr) {
    init {
        typeface = FontManager.getTypeface(context, FontManager.ALEGREYA_TTF)
    }

}