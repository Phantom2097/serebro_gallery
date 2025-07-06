package ru.null_checkers.ui.toolbar

/**
 * Обеспечивает функционал для инициализации и изменения заголовка toolbar
 */
interface ToolbarController {
    /**
     * Инициализация toolbar
     */
    fun createToolbar()

    /**
     * Установка нового заголовка для экрана
     *
     * @param title Новый заголовок экрана
     */
    fun setTitle(title: String)
}