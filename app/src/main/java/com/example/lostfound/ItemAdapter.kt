package com.example.lostfound

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    val itemList: ArrayList<Item>
) : RecyclerView.Adapter<ItemAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgItem =
            itemView.findViewById<ImageView>(R.id.imgItem)

        val txtItemName =
            itemView.findViewById<TextView>(R.id.txtItemName)

        val txtDescription =
            itemView.findViewById<TextView>(R.id.txtDescription)

        val txtLocation =
            itemView.findViewById<TextView>(R.id.txtLocation)

        val txtContact =
            itemView.findViewById<TextView>(R.id.txtContact)

        val txtType =
            itemView.findViewById<TextView>(R.id.txtType)

        val btnDelete =
            itemView.findViewById<Button>(R.id.btnDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_layout,
                    parent,
                    false
                )

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        val item = itemList[position]

        holder.txtItemName.text = item.itemName
        holder.txtDescription.text = item.description
        holder.txtLocation.text = item.location
        holder.txtContact.text = item.contact
        holder.txtType.text = item.type

        if (item.photo != null) {

            holder.imgItem.setImageURI(
                Uri.parse(item.photo)
            )

        } else {

            holder.imgItem.setImageResource(
                android.R.drawable.ic_menu_camera
            )
        }

        holder.btnDelete.setOnClickListener {

            AlertDialog.Builder(
                holder.itemView.context
            )
                .setTitle("Delete Item")
                .setMessage(
                    "Are you sure you want to delete this item?"
                )
                .setNegativeButton(
                    "CANCEL",
                    null
                )
                .setPositiveButton(
                    "DELETE"
                ) { _, _ ->

                    val databaseHelper =
                        DatabaseHelper(
                            holder.itemView.context
                        )

                    val result =
                        databaseHelper.deleteItem(
                            item.id
                        )

                    if (result) {

                        itemList.removeAt(position)

                        notifyItemRemoved(position)
                    }
                }
                .show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}