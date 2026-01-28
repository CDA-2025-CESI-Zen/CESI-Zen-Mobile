package fr.cesizen.presentation.services

expect open class ToastService() {

    /** Show the given [message] on the screen for a few seconds. */
    fun showToast(message: String)
}