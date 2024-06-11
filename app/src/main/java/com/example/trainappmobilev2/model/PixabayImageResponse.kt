package com.example.trainappmobilev2.model

data class PixabayImageResponse(
    val hits: List<ImageResult>
)

data class ImageResult(
    val webformatURL: String
)