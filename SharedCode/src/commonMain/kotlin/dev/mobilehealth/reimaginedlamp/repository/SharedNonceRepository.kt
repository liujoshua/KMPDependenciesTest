package dev.mobilehealth.reimaginedlamp.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SharedNonceRepository {
    private val client = HttpClient()


    fun about(callback: (String) -> Unit) {
        GlobalScope.apply {
            launch(ApplicationDispatcher) {
                val result: String = client.get {
                    url(block = {
                        this.host = Service.host.toString()
                    })
                }

                callback(result)
            }
        }
    }
}