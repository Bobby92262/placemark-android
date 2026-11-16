package com.example.placemark.models
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.concurrent.atomic.AtomicLong

class PlacemarkMemStore (private val file: File) {

    private var placemarks = ArrayList<PlacedMarkersData>()
    private var lastId = AtomicLong(0L)

    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    init {
        load()
    }

    private fun load() {
        if(!file.exists()) return

        val type = object: TypeToken<ArrayList<PlacedMarkersData>>() {}.type
        // Read in all text from file and convert to array list if null
        val loaded: ArrayList<PlacedMarkersData> =
            gson.fromJson(file.readText(), type)?: arrayListOf()

        // Already populated, clear it. Safeguard, not needed in this app
        placemarks.clear()
        //Repopulate with new data
        placemarks.addAll(loaded)

        // Find the max placemarks value or null, use lamba to convert to Long
        //Could be issue related to the id incrementation. Duplicate id reuse
        lastId.set(placemarks.maxByOrNull { it.id }?.id ?: 0L)
    }

    private fun save() {
        file.parentFile?.mkdirs()
        file.writeText(gson.toJson(placemarks))
    }


    fun findAll(): List<PlacedMarkersData> {
        return placemarks
    }

    fun findOne(id: Long): PlacedMarkersData? {
        return placemarks.find { p -> p.id == id }
    }
    fun create(placemark: PlacedMarkersData) {
        placemark.validate()
        placemark.id = lastId.incrementAndGet()
        placemarks.add(placemark)
        save()
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
            save()
            true

        } else {
            false
        }
    }


    fun delete(id: Long): Boolean {
        val foundPlacemark = findOne(id)
        return if (foundPlacemark != null) {
            placemarks.remove(foundPlacemark)
            save()
            true
        } else {
            false
        }
    }


}

