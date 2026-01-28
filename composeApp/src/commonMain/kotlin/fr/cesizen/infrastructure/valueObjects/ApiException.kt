package fr.cesizen.infrastructure.valueObjects

sealed class ApiException(message: String) : Exception(message) {
    class Unauthorized : ApiException("Vous n'êtes plus connecté(e) !")
    class Forbidden    : ApiException("Accès refusé lors de la requête !")
    class ServerError  : ApiException("Erreur serveur lors de la requête !")
    class Timeout      : ApiException("Impossible de se connecter au serveur !")
    class Unknown      : ApiException("Erreur inconnue lors de la requête !")
}
