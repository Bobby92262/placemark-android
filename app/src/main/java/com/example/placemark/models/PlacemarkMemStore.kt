package com.example.placemark.models
import java.util.concurrent.atomic.AtomicLong

class PlacemarkMemStore {

    private var placemarks = ArrayList<PlacemarkModel>()
    private var lastId = AtomicLong(0L)

    fun findAll(): List<PlacemarkModel> {
        return placemarks
    }

    fun create(placemark: PlacemarkModel) {
        placemark.id = lastId.incrementAndGet() // Assign a unique ID to the placemark by increment by 1 from the lastId
        placemarks.add(placemark)
    }

    fun update(placemark: PlacemarkModel): Boolean {
        val foundPlacemark = findOne(placemark.id)
        return if (foundPlacemark != null) {
            foundPlacemark.id = placemark.id
            foundPlacemark.title = placemark.title
            foundPlacemark.description = placemark.description
            foundPlacemark.x = placemark.x
            foundPlacemark.y = placemark.y
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

    fun findOne(id: Long): PlacemarkModel? {
        return placemarks.find { p -> p.id == id }
    }
}

