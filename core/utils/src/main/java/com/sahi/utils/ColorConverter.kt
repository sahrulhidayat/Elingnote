package com.sahi.utils

import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

@ColorInt
fun darkenColor(@ColorInt color: Int, value: Float = 0.5f): Int {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(color, hsl)
    hsl[2] -= value
    hsl[2].coerceAtLeast(0f).coerceAtMost(1f)
    return ColorUtils.HSLToColor(hsl)
}