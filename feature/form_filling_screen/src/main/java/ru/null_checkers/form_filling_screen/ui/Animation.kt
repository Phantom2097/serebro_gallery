package ru.null_checkers.form_filling_screen.ui

import android.view.View

object Animation {

    private const val SCALE_UP = 1.1f
    private const val SCALE_DEFAULT = 1.0f

    private const val ANIMATION_DURATION_START = 300L
    private const val ANIMATION_DURATION_END = 150L

    fun View.imagePickSimpleAnimation() {
        animate()
            .scaleX(SCALE_UP)  // Увеличиваем по X
            .scaleY(SCALE_UP)  // Увеличиваем по Y
            .setDuration(ANIMATION_DURATION_START)
            .withEndAction {
                // Возвращаем к исходному размеру после увеличения
                animate()
                    .scaleX(SCALE_DEFAULT)
                    .scaleY(SCALE_DEFAULT)
                    .setDuration(ANIMATION_DURATION_END)
                    .start()
            }
            .start()
    }
}