package fr.cesizen.presentation.services

expect open class DialerService() {

    /** Opens the dialer for the given [phoneNumber]. */
    fun openDialer(phoneNumber: String)
}