package dev.mobilehealth.reimaginedlamp.repository
//
//import com.benasher44.uuid.Uuid
//import com.soywiz.krypto.sha256
//import io.islandtime.Instant
//import io.islandtime.ZonedDateTime
//import io.islandtime.ranges.InstantInterval
//import io.islandtime.toInstant
//
//data class DailyHealth(val combinedNonce: String, val ok: Boolean, val timestamp: ZonedDateTime) {
//    fun toDocumentMap(): Any {
//        return hashMapOf(
//            "combinedNonce" to combinedNonce,
//            "ok" to ok,
//            "timestamp" to timestamp.toString()
//        )
//    }
//
//    companion object {
//        @ExperimentalStdlibApi
//        fun create(sharedNonce: Uuid, userNonce: Uuid, ok: Boolean, timestamp: ZonedDateTime): DailyHealth {
//            return DailyHealth(
//                "$sharedNonce|$userNonce".encodeToByteArray().sha256().base64,
//                ok,
//                timestamp
//            )
//        }
//    }
//}
//
//data class Nonce(val nonce: Uuid, val activeInterval: InstantInterval) {
//    companion object {
//        fun create(nonce: Uuid, startAndEndInstant: StartAndEndInstant): Nonce {
//            return Nonce(
//                nonce,
//                InstantInterval(startAndEndInstant.startDate, startAndEndInstant.endDate)
//            )
//        }
//    }
//
//    fun toDocumentMap(): Any {
//        return hashMapOf(
//            "nonce" to nonce,
//            "endDate" to activeInterval.start.toString(),
//            "startDate" to activeInterval.endExclusive.toString()
//        )
//    }
//}
//
//data class StartAndEndInstant(val startDate: Instant, val endDate: Instant) {
//
//    fun toDocumentMap(): Any {
//        return hashMapOf(
//            "endDate" to endDate.toString(),
//            "startDate" to startDate.toString()
//        )
//    }
//
//    companion object {
//        fun fromMap(documentMap: Map<String, Any?>): StartAndEndInstant {
//            documentMap.getValue("startDate")
//            return StartAndEndInstant(
//                (documentMap["startDate"] as String).toInstant(),
//                (documentMap["endDate"] as String).toInstant()
//            )
//        }
//    }
//}
