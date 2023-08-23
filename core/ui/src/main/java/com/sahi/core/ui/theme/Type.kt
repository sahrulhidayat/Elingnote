package com.sahi.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sahi.elingnote.R

val nunitoSansFamily = FontFamily(
    Font(R.font.nunitosans_condensed_regular, FontWeight.Normal),
    Font(R.font.nunitosans_condensed_medium, FontWeight.Medium),
    Font(R.font.nunitosans_condensed_semibold, FontWeight.SemiBold),
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = nunitoSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = nunitoSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.2.sp
    ),
    titleLarge = TextStyle(
        fontFamily = nunitoSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = nunitoSansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
)