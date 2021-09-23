package com.spotifyservice.authorize

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Base64

@Service
class AuthorizeService {
    private val client = OkHttpClient()
    private val BASE_URL = "https://accounts.spotify.com/api/token"

    // todo make these secrets
    private val CLIENT_ID = ""
    private val CLIENT_SECRET = ""
    private val base64 = Base64.getUrlEncoder()
    private val mapper = jacksonObjectMapper()

    fun authorize(): AuthorizeResponse? {
        val requestBody = FormBody
            .Builder()
            .add("grant_type", "client_credentials")
            .build()

        val request = Request.Builder()
            .url(BASE_URL)
            .addHeader(
                "Authorization", "Basic ${base64Encode()}"
            )
            .addHeader(
                "Content-type",
                "application/x-www-form-urlencoded"
            )
            .post(requestBody)
            .build()

        return try {
            client.newCall(request).execute().let {

                if (it.isSuccessful) {
                    val authorizeResponse = it.body?.string()?.let { body ->
                        mapper.readValue<AuthorizeResponse>(body).also {
                            println("authorize response is $it") // todo make these actual log messages
                        }
                    }
                    authorizeResponse
                } else {
                    val body = it.body?.string()
                    println("Authorization flow failed. Error body: $body")
                    throw ResponseStatusException(HttpStatus.valueOf(it.code), body)
                }
            }
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    private fun base64Encode(): String {
        return base64.encode("$CLIENT_ID:$CLIENT_SECRET".toByteArray()).decodeToString()
    }
}
