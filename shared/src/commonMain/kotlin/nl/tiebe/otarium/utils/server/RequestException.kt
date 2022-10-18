package nl.tiebe.otarium.utils.server

import io.ktor.http.*

class RequestException(val statusCode: HttpStatusCode, val error: String?, message: String?) : RuntimeException(message) {
}