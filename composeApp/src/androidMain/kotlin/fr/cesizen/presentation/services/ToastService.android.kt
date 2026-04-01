package fr.cesizen.presentation.services

import android.widget.Toast

actual open class ToastService actual constructor() {
    actual fun showToast(message: String) {
        val context = activityProvider.invoke()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}