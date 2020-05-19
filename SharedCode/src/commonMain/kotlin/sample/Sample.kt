package sample
import com.benasher44.uuid.uuid4
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime

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

fun sayDate(): String {
    return DateTime.now().toString(DateFormat.FORMAT1)
}