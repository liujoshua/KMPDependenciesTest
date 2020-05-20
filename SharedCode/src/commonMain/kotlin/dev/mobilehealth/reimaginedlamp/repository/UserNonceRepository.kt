package dev.mobilehealth.reimaginedlamp.repository

import co.touchlab.firebase.firestore.FirebaseFirestore
import co.touchlab.firebase.firestore.QueryDirection
import co.touchlab.firebase.firestore.addSnapshotListener_
import co.touchlab.firebase.firestore.collection
import co.touchlab.firebase.firestore.data_
import co.touchlab.firebase.firestore.document
import co.touchlab.firebase.firestore.documents_
import co.touchlab.firebase.firestore.id
import co.touchlab.firebase.firestore.metadata
import co.touchlab.firebase.firestore.orderBy
import co.touchlab.firebase.firestore.pendingWrites
import com.benasher44.uuid.uuidFrom
import com.github.aakira.napier.Napier
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData

//expect suspend fun getNewNonceImpl(doc: DocumentReference, firestore: FirebaseFirestore): Response<Nonce>
open class UserNonceRepository(val userId: String, val firestore: FirebaseFirestore) {
    val user_collection_key = "user"
    val user_collection_nonce_key = "nonce"

    fun getNoncesLiveData(limit: Int): LiveData<List<Nonce>> {
        val noncesLD = MutableLiveData(listOf<Nonce>())

        var subscription = firestore.collection(user_collection_key)
            .document(userId!!)
            .collection(user_collection_nonce_key)
            .orderBy("startDate", QueryDirection.DESCENDING)
            .addSnapshotListener_(null) { value, e ->
                if (e != null) {
                    Napier.w("Failed to retrieve daily nonces", e)
                    return@addSnapshotListener_
                }
                val dailyNonceList = mutableListOf<Nonce>()
                for (nonceDocument in value!!.documents_) {

                    Napier.d("NonceDocument: ${nonceDocument.id}, hasPendingWrites: ${nonceDocument.metadata.pendingWrites}")
                    val nonceInterval = StartAndEndInstant.fromMap(nonceDocument.data_()!!)
                    dailyNonceList.add(
                        Nonce.create(
                            uuidFrom(
                                nonceDocument.id
                            ), nonceInterval
                        )
                    )
                }
                noncesLD.postValue(dailyNonceList)
            }
        return noncesLD
    }
}