package ru.null_checkers.form_filling_screen.domain.use_cases_impls

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.null_checkers.form_filling_screen.domain.use_cases.PickImageFromLocalStorage

@Deprecated("")
class PickImageFromLocalStorageUseCase : PickImageFromLocalStorage {
    override suspend operator fun invoke() : String {
        return withContext(Dispatchers.IO) {
            ""
        }
    }
}