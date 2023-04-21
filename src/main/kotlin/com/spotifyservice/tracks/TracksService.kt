package com.spotifyservice.tracks

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.spotifyservice.authorize.AuthorizeService
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TracksService {
    private val client = OkHttpClient()
    private val mapper = jacksonObjectMapper()
    private val authorizeService = AuthorizeService()
    private val BASE_URL = "https://api.spotify.com/v1/tracks/"

    fun getTrack(trackId: String): TracksResponse? {
        val authorizeResponse = authorizeService.authorize()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization failed. Could not get track")

        val request = Request.Builder()
            .url(BASE_URL + trackId)
            .addHeader(
                "Authorization",
                "Bearer ${authorizeResponse.accessToken}"
            )
            .addHeader(
                "Content-type",
                "application/json"
            )
            .get()
            .build()

        return try {
            client.newCall(request).execute().let {
                if (it.isSuccessful) {
                    val tracksResponse = it.body?.string()?.let { body ->
                        mapper.readValue<TracksResponse>(body).also {
                            logger.info("Tracks response is $it")
                        }
                    }
                    tracksResponse
                } else {
                    val body = it.body?.string()
                    logger.error("Track call failed. Error body: $body")
                    throw ResponseStatusException(HttpStatus.valueOf(it.code), body)
                }
            }
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
