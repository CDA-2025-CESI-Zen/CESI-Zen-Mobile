package fr.cesizen.presentation.services

import android.content.Intent
import androidx.core.net.toUri

actual open class DialerService actual constructor() {
    actual fun openDialer(phoneNumber: String) {
        val context = activityProvider.invoke()
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phoneNumber".toUri()
        }
        context?.startActivity(intent)
    }
}