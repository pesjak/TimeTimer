package com.primoz.timetimer.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.primoz.timetimer.extras.FontManager

/**
 * Created by Primož on 21/04/2018.
 */
class FontTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TextView(context, attrs, defStyleAttr) {
    init {
        typeface = FontManager.getTypeface(context, FontManager.ALEGREYA_TTF)
    }

}