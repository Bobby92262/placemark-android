package com.example.placemark


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.placemark.models.PlacedMarkersData
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts


class AddEditActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var xInput: EditText
    private lateinit var yInput: EditText


    private var editingId: Long? = null


    lateinit var imagePreview: ImageView
    var selectedImageUri: Uri? = null

    val imagePicker =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                // Keep access to the document URI for later use, when supported.
                try {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: SecurityException) {
                    // Some providers may not grant persistable access.
                }

                selectedImageUri = uri
                imagePreview.setImageURI(uri)
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_edit)

        titleInput =
            findViewById(R.id.titleInput)

        descriptionInput =
            findViewById(R.id.descriptionInput)

        imagePreview = findViewById(R.id.imagePicker)

        xInput =
            findViewById(R.id.xInput)

        yInput =
            findViewById(R.id.yInput)

        val saveButton =
            findViewById<Button>(R.id.saveButton)

        val cancelButton =
            findViewById<Button>(R.id.cancelButton)

        findViewById<Button>(R.id.imageButton).setOnClickListener {
            imagePicker.launch(arrayOf("image/*"))
        }

        editingId =
            intent.getLongExtra("id", -1L)
                .takeIf { it != -1L }

        if (editingId != null) {

            findViewById<TextView>(R.id.formTitle)
                .text = "Edit Mark"

            loadExistingMark(editingId!!)
        }

        saveButton.setOnClickListener {
            saveMark()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun loadExistingMark(id: Long) {

        val mark =
            AppData.placedMarks.findOne(id)

        selectedImageUri = mark?.imageUri?.let { Uri.parse(it) }

        if (selectedImageUri != null) {
            imagePreview.setImageURI(selectedImageUri)
        } else {
            imagePreview.setImageResource(android.R.drawable.ic_menu_gallery)
        }


        if (mark == null) {

            Toast.makeText(
                this,
                "Mark not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        titleInput.setText(mark.title)
        descriptionInput.setText(mark.desc)
        xInput.setText(mark.x.toString())
        yInput.setText(mark.y.toString())
    }

    private fun saveMark() {

        val title =
            titleInput.text.toString().trim()
        if (title.isEmpty()) {
            titleInput.error = "Title is required"
            return
        }


        val description =
            descriptionInput.text.toString().trim()


        val x =
            xInput.text.toString().toDoubleOrNull()

        if (x == null) {
            xInput.error = "Enter a valid number"
            return
        }

        if (x !in -90.0..90.0) {
            xInput.error = "Latitude must be between -90 and 90"
            return
        }

        val y =
            yInput.text.toString().toDoubleOrNull()

        if (y == null) {
            yInput.error = "Enter a valid number"
            return
        }

        if (y !in -180.0..180.0) {
            yInput.error = "Longitude must be between -180 and 180"
            return
        }

        try {
            if (editingId == null) {

                val mark = PlacedMarkersData(
                    title = title,
                    desc = description,
                    imageUri = selectedImageUri?.toString(),
                    x = x,
                    y = y
                )

                mark.validate()

                AppData.placedMarks.create(mark)

                Toast.makeText(
                    this,
                    "Mark created",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                val mark = PlacedMarkersData(
                    id = editingId!!,
                    title = title,
                    desc = description,
                    imageUri = selectedImageUri?.toString(),
                    x = x,
                    y = y
                )

                mark.validate()

                AppData.placedMarks.update(mark)

                Toast.makeText(
                    this,
                    "Mark updated",
                    Toast.LENGTH_SHORT
                ).show()
            }

            finish()
        } catch (e: IllegalArgumentException) {
            Toast.makeText(
                this,
                e.message?: "Invalid values entered",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
