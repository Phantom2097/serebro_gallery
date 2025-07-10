package ru.null_checkers.common.entity

import ru.null_checkers.common.shared_view_model.PhotoSharedViewModel

interface SharedViewModel {
    fun getSharedPhotoViewModel(): PhotoSharedViewModel
}