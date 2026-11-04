package com.example.placemark


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.placemark.models.PlacedMarkersData

class PlacedMarkAdapter(
    private var marks: List<PlacedMarkersData>,
    private val onEdit: (PlacedMarkersData) -> Unit,
    private val onDelete: (PlacedMarkersData) -> Unit
) : RecyclerView.Adapter<PlacedMarkAdapter.MarkViewHolder>() {

    class MarkViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val titleText: TextView =
            itemView.findViewById(R.id.titleText)

        val descriptionText: TextView =
            itemView.findViewById(R.id.descriptionText)

        val markImage : ImageView =
            itemView.findViewById(R.id.markImage)

        val coordinatesText: TextView =
            itemView.findViewById(R.id.coordinatesText)

        val editButton: Button =
            itemView.findViewById(R.id.editButton)

        val deleteButton: Button =
            itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarkViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_placed_mark,
                parent,
                false
            )

        return MarkViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MarkViewHolder,
        position: Int
    ) {

        val mark = marks[position]

        holder.titleText.text =
            "${mark.id}: ${mark.title}"

        holder.descriptionText.text =
            mark.desc

        val imageUri = mark.imageUri

        if (imageUri != null) {
            holder.markImage.setImageURI(Uri.parse(imageUri))
        } else {
            holder.markImage.setImageResource(
                android.R.drawable.ic_menu_gallery
            )
        }

        holder.coordinatesText.text =
            "X: ${mark.x}, Y: ${mark.y}"

        holder.editButton.setOnClickListener {
            onEdit(mark)
        }

        holder.deleteButton.setOnClickListener {
            onDelete(mark)
        }
    }

    override fun getItemCount(): Int {
        return marks.size
    }

    fun updateMarks(newMarks: List<PlacedMarkersData>) {
        marks = newMarks
        notifyDataSetChanged()
    }
}
