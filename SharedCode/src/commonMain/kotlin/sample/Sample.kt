package sample
import com.benasher44.uuid.uuid4
import io.islandtime.Instant
import io.islandtime.clock.now

expect class Sample() {
    fun checkMe(): Int
}

expect object Platform {
    fun name(): String
}

fun hello(): String = "Hello from ${Platform.name()}"

fun sayUuid(): String {
    return uuid4().toString()
}

fun sayTime(): String {
    return Instant.now().toString()
}