package fr.cesizen.presentation.core

interface State<T> {
    class      Loading<T>()            : State<T>
    data class Loaded<T>(val value: T) : State<T>
}