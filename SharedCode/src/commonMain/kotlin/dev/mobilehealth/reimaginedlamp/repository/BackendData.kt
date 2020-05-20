package dev.mobilehealth.reimaginedlamp.repository

import com.benasher44.uuid.Uuid
import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeRange
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.ISO8601.DATETIME_COMPLETE
import com.soywiz.klock.parseUtc
import com.soywiz.krypto.sha256


data class DailyHealth(val combinedNonce: String, val ok: Boolean, val timestamp: DateTimeTz) {
    fun toDocumentMap(): Any {
        return hashMapOf(
            "combinedNonce" to combinedNonce,
            "ok" to ok,
            "timestamp" to timestamp.toString()
        )
    }

    companion object {
        @ExperimentalStdlibApi
        fun create(sharedNonce: Uuid, userNonce: Uuid, ok: Boolean, timestamp: DateTimeTz): DailyHealth {
            return DailyHealth(
                "$sharedNonce|$userNonce".encodeToByteArray().sha256().base64,
                ok,
                timestamp
            )
        }
    }
}

data class Nonce(val nonce: Uuid, val activeInterval: DateTimeRange) {
    companion object {
        fun create(nonce: Uuid, startAndEndInstant: StartAndEndInstant): Nonce {
            return Nonce(
                nonce,
                DateTimeRange(startAndEndInstant.startDate, startAndEndInstant.endDate)
            )
        }
    }

    fun toDocumentMap(): Any {
        return hashMapOf(
            "nonce" to nonce,
            "endDate" to activeInterval.from.toString(),
            "startDate" to activeInterval.to.toString()
        )
    }
}

data class StartAndEndInstant(val startDate: DateTime, val endDate: DateTime) {

    fun toDocumentMap(): Any {
        return hashMapOf(
            "endDate" to endDate.toString(),
            "startDate" to startDate.toString()
        )
    }

    companion object {
        fun fromMap(documentMap: Map<String, Any?>): StartAndEndInstant {

            documentMap.getValue("startDate")
            return StartAndEndInstant(
                DATETIME_COMPLETE.parseUtc(documentMap["startDate"] as String),
                DATETIME_COMPLETE.parseUtc(documentMap["endDate"] as String)
            )
        }
    }
}