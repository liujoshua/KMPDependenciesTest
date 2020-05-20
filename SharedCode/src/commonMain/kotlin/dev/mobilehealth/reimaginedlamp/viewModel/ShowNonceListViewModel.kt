package dev.mobilehealth.reimaginedlamp.viewModel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.liveData
//import com.benasher44.uuid.Uuid
//import com.github.aakira.napier.Napier
//import com.google.android.gms.tasks.OnCanceledListener
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.EventListener
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.Query
//import com.google.firebase.firestore.QuerySnapshot
//import dev.icerock.moko.mvvm.livedata.LiveData
//import dev.icerock.moko.mvvm.livedata.MutableLiveData
//import dev.icerock.moko.mvvm.livedata.map
//import dev.icerock.moko.mvvm.viewmodel.ViewModel
//import dev.mobilehealth.reimaginedlamp.repository.Nonce
//import dev.mobilehealth.reimaginedlamp.repository.StartAndEndInstant
//import dev.mobilehealth.reimaginedlamp.repository.UserNonceRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//
//class ShowNonceListViewModel : ViewModel() {
//
//    val dailyNonces: MutableLiveData<List<Nonce>> = MutableLiveData(listOf())
//    val activeNonces =
//        dailyNonces.map { nonces -> nonces.filter { nonce -> nonce.activeInterval.contains(Instant.now()) } }
////        dailyNonces.map { nonces -> nonces.find { nonce -> nonce.activeInterval.contains(Instant.now()) } }
//
//    // TODO: remove
//    val user_collection_key = "user"
//    val user_collection_nonce_key = "nonce"
//
//    val auth = FirebaseAuth.getInstance();
//    val firestore = FirebaseFirestore.getInstance()
//    var userId: String? = null
//
//    init {
//        // auth
//        auth.currentUser?.let {
//            userId = it.uid
//        } ?: auth.signInAnonymously()
//            .addOnSuccessListener { result ->
//                userId = result.user?.uid
//            }
//            .addOnFailureListener() { Napier.w("Sign in failed", it) }
//            .addOnCanceledListener(OnCanceledListener { Napier.w("Sign in Cancelled") })
//    }
//
//
//    @ExperimentalCoroutinesApi
//    val dailyNoncesLiveData = LiveData(liveData<List<Nonce>>(Dispatchers.IO) {
//        emit(listOf())
//
//        var sharedNonceRepo = UserNonceRepository(userId!!, firestore)
//
//        sharedNonceRepo.getNonces(5).collect {
//            emit(it)
//        }
//    })
//
//    fun fetchAllDailyNonces() {
//        if (userId == null) {
//            Napier.w("No userId found")
//            return
//        }
//
//
//        // TODO: refactor with CreateNonceViewModel version
//        firestore.collection(user_collection_key)
//            .document(userId!!)
//            .collection(user_collection_nonce_key).orderBy("startDate", Query.Direction.DESCENDING)
//            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
//                if (e != null) {
//                    Napier.w("Failed to retrieve daily nonces", e)
//                    dailyNonces.ld().value = listOf()
//                    return@EventListener
//                }
//                val dailyNonceList = mutableListOf<Nonce>()
//                for (nonceDocument in value!!) {
//
//                    Napier.d("NonceDocument: ${nonceDocument.id}, hasPendingWrites: ${nonceDocument.metadata.hasPendingWrites()}")
//                    val nonceInterval = StartAndEndInstant.fromMap(nonceDocument.data)
//                    dailyNonceList.add(
//                        Nonce.create(
//                            Uuid.fromString(
//                                nonceDocument.id
//                            ), nonceInterval
//                        )
//                    )
//                }
//                dailyNonces.value = dailyNonceList
//            })
//
//    }
//
//
//}
