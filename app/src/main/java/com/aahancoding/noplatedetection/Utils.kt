package com.aahancoding.noplatedetection

import android.view.View
import java.util.regex.Matcher
import java.util.regex.Pattern

private val CAR_NUMBER_PLATE_PATTERN_OLD_PATTERN =
    Pattern.compile("^[A-Z,a-z]{2}[0-9,a-zA-Z]{1,3}[A-Z]{1,2}[0-9]{4}\$")
private val CAR_NUMBER_PLATE_PATTERN_NEW_PATTERN =
    Pattern.compile("^[0-9]{2}[a-zA-Z]{1,2}[0-9]{4}[a-zA-Z]{1,2}\$")


fun String.isCarNumber(): Boolean {
    val password = toString().trim().replace(" ", "").uppercase()
    val oldPattern: Matcher = CAR_NUMBER_PLATE_PATTERN_OLD_PATTERN.matcher(password)
    val newPattern: Matcher = CAR_NUMBER_PLATE_PATTERN_NEW_PATTERN.matcher(password)
    return oldPattern.matches() || newPattern.matches()
}

fun View.animateAlpha(visibility: Int, delay: Long = 200) {
    if (visibility == View.VISIBLE) {
        setVisibility(View.VISIBLE)
    }

    val alpha = when (visibility) {
        View.GONE, View.INVISIBLE -> 0f
        View.VISIBLE -> 1f
        else -> 1f
    }

    animate().apply {
        duration = delay
        alpha(alpha)
        withEndAction {
            setVisibility(visibility)
        }
    }
}