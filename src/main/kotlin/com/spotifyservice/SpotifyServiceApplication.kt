package com.spotifyservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpotifyServiceApplication

fun main(args: Array<String>) {
    runApplication<SpotifyServiceApplication>(*args)
}
