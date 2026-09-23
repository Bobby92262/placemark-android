package com.example.placemark.models

/**
 * Data class representing a single Placemark item.
 * Kotlin automatically generates toString(), equals(), hashCode(), and copy().
 */
data class PlacemarkModel(
    var id: Long = 0L,
    var title: String = "",
    var description: String = "",
    var x: Double = 0.0,
    var y: Double = 0.0
    // TODO add more here
)
