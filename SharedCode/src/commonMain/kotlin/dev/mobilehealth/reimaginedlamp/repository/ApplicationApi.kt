package dev.mobilehealth.reimaginedlamp.repository

import io.ktor.http.Url
import kotlinx.coroutines.CoroutineDispatcher


object Service {

    val host = Url("http://EC2Co-EcsEl-1SPLTEMRBRTIS-77938816.us-west-2.elb.amazonaws.com")
}

expect val ApplicationDispatcher: CoroutineDispatcher