package sample
import com.benasher44.uuid.uuid4

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