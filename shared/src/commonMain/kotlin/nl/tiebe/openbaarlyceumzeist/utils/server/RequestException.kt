package nl.tiebe.openbaarlyceumzeist.utils.server

import io.ktor.http.*

class RequestException(val statusCode: HttpStatusCode, val error: String?, message: String?) : RuntimeException(message) {
}