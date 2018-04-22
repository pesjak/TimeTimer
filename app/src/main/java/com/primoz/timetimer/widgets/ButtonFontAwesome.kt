package com.primoz.timetimer.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import com.primoz.timetimer.extras.FontManager

/**
 * Created by Primož on 21/04/2018.
 */

class ButtonFontAwesome @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : Button(context, attrs, defStyleAttr) {
    init {
        FontManager.markAsIconContainer(this, FontManager.getTypeface(context, FontManager.FONTAWESOME))
    }
}