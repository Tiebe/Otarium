package nl.tiebe.openbaarlyceumzeist.utils.server

import io.ktor.http.*

val RELEASE_SERVER_URL = Url("https://otarium.groosman.nl")
val DEBUG_SERVER_URL = Url("http://192.168.2.37:80")
val SERVER_URL = RELEASE_SERVER_URL

val DEVICE_URL = URLBuilder(SERVER_URL).appendPathSegments("device").build()
val DEVICE_ADD_URL = URLBuilder(DEVICE_URL).appendPathSegments("add").build()

val LOGIN_URL = URLBuilder(SERVER_URL).appendPathSegments("login").build()
val EXCHANGE_URL = URLBuilder(LOGIN_URL).appendPathSegments("exchange").build()
