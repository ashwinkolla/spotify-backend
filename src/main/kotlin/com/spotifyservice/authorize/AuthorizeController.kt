package com.spotifyservice.authorize

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/authorize")
class AuthorizeController {
    private val authorizeService = AuthorizeService()

    @GetMapping
    fun authorize(): AuthorizeResponse? =
        authorizeService.authorize()
}
