package com.example.placemark

import com.example.placemark.models.PlacemarkMemStore
import android.content.Context
import java.io.File

object AppData {

    lateinit var placedMarks: PlacemarkMemStore
    private set

    fun initialize(context: Context) {
        val file = File(
            context.filesDir,
            "place_list.json"
        )
        placedMarks = PlacemarkMemStore(file)
    }
    //val placedMarks = PlacemarkMemStore(context(file))
}
