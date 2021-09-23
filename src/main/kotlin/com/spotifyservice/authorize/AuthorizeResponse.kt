package com.spotifyservice.authorize

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AuthorizeResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long
)
