package com.example.placemark.models
import java.util.concurrent.atomic.AtomicLong

class PlacemarkMemStore {

    private var placemarks = ArrayList<PlacedMarkersData>()
    private var lastId = AtomicLong(0L)

    fun findAll(): List<PlacedMarkersData> {
        return placemarks
    }

    fun create(placemark: PlacedMarkersData) {
        placemark.validate()
        placemark.id = lastId.incrementAndGet()
        placemarks.add(placemark)
    }


    fun update(placemark: PlacedMarkersData): Boolean {
        placemark.validate()

        val foundPlacemark = findOne(placemark.id)
        return if (foundPlacemark != null) {
            foundPlacemark.title = placemark.title
            foundPlacemark.desc = placemark.desc
            foundPlacemark.x = placemark.x
            foundPlacemark.y = placemark.y
            foundPlacemark.imageUri = placemark.imageUri
            true
        } else {
            false
        }
    }


    fun delete(id: Long): Boolean {
        val foundPlacemark = findOne(id)
        return if (foundPlacemark != null) {
            placemarks.remove(foundPlacemark)
            true
        } else {
            false
        }
    }

    fun findOne(id: Long): PlacedMarkersData? {
        return placemarks.find { p -> p.id == id }
    }
}

