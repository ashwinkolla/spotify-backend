package com.spotifyservice.tracks

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/tracks")
class TracksController {
    private val tracksService: TracksService = TracksService()


    @GetMapping("/{id}")
    fun getSong(@PathVariable id: String) : TracksResponse? {
        return tracksService.getTrack(id)
    }
}