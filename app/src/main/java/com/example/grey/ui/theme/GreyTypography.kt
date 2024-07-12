package com.example.grey.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.grey.R

object GreyTypography {

    private val provider by lazy {
        GoogleFont.Provider(
            providerAuthority = "com.google.android.gms.fonts",
            providerPackage = "com.google.android.gms",
            certificates = R.array.com_google_android_gms_fonts_certs
        )
    }

    private val fontName by lazy {GoogleFont("Manrope")}

    private val fontFamily by lazy {
        FontFamily(
            Font(fontName, provider, FontWeight.Normal),
            Font(fontName, provider, FontWeight.Medium),
            Font(fontName, provider, FontWeight.Bold)
        )
    }

    val h1 by lazy {
        TextStyle(
            fontSize = 18.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp
        )
    }

    val h2 by lazy {
        TextStyle(
            fontSize = 16.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            lineHeight = 21.sp
        )
    }

    val h3 by lazy {
        TextStyle(
            fontSize = 16.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 21.sp
        )
    }

    val tab by lazy {
        TextStyle(
            fontSize = 10.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 13.sp
        )
    }

    val tabB by lazy {
        TextStyle(
            fontSize = 12.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 13.sp
        )
    }

    val medium by lazy {
        TextStyle(
            fontSize = 12.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            lineHeight = 20.sp
        )
    }

    val mediumS by lazy {
        TextStyle(
            fontSize = 12.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            lineHeight = 16.sp
        )
    }

    val mediumSS by lazy {
        TextStyle(
            fontSize = 10.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            lineHeight = 13.sp
        )
    }

    val mediumXS by lazy {
        TextStyle(
            fontSize = 8.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            lineHeight = 10.sp
        )
    }

    val normalSm by lazy {
        TextStyle(
            fontSize = 12.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            lineHeight = 20.sp
        )
    }

    val normalXSm by lazy {
        TextStyle(
            fontSize = 10.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            lineHeight = 13.sp
        )
    }

    val mediumB by lazy {
        TextStyle(
            fontSize = 14.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            lineHeight = 19.sp
        )
    }

    val normal by lazy {
        TextStyle(
            fontSize = 14.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            lineHeight = 19.sp
        )
    }
}
