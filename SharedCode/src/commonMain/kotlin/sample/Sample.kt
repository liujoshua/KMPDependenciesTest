package sample
import io.islandtime.Instant
import io.islandtime.clock.now

expect class Sample() {
    fun checkMe(): Int
}

expect object Platform {
    fun name(): String
}

fun hello(): String = "Hello from ${Platform.name()}"

fun sayTime(): String {
    return Instant.now().toString()
}

