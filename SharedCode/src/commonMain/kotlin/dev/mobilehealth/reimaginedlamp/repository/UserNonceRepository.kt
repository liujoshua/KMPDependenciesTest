package dev.mobilehealth.reimaginedlamp.repository
//
//import co.touchlab.firebase.firestore.*
//import com.benasher44.uuid.uuidFrom
//import com.github.aakira.napier.Napier
//import dev.mobilehealth.reimaginedlamp.domain.Response
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//
////expect suspend fun getNewNonceImpl(doc: DocumentReference, firestore: FirebaseFirestore): Response<Nonce>
//open class UserNonceRepository(val userId: String, val firestore: FirebaseFirestore) {
//    val user_collection_key = "user"
//    val user_collection_nonce_key = "nonce"
//
//
//// TODO: get new nonce from server
//
//    @ExperimentalCoroutinesApi
//    fun getNonces(limit: Int): Flow<List<Nonce>> = callbackFlow {
//        var subscription = firestore.collection(user_collection_key)
//            .document(userId!!)
//            .collection(user_collection_nonce_key)
//            .orderBy("startDate", QueryDirection.DESCENDING)
//            .addSnapshotListener_(null) { value, e ->
//                if (e != null) {
//                    Napier.w("Failed to retrieve daily nonces", e)
//                    return@addSnapshotListener_
//                }
//                val dailyNonceList = mutableListOf<Nonce>()
//                for (nonceDocument in value!!.documents_) {
//
//                    Napier.d("NonceDocument: ${nonceDocument.id}, hasPendingWrites: ${nonceDocument.metadata.pendingWrites}")
//                    val nonceInterval = StartAndEndInstant.fromMap(nonceDocument.data_()!!)
//                    dailyNonceList.add(
//                        Nonce.create(
//                            uuidFrom(
//                                nonceDocument.id
//                            ), nonceInterval
//                        )
//                    )
//                }
//                offer(dailyNonceList)
//            }
//        awaitClose { subscription.remove() }
//    }
//}