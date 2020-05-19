package sample

import com.github.aakira.napier.Napier

expect class Sample() {
    fun checkMe(): Int
}

expect object Platform {
    fun name(): String
}

fun hello(): String = "Hello from ${Platform.name()}"

fun log() {
    Napier.d(hello())
}