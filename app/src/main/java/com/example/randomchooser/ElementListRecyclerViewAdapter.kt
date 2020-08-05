package com.example.randomchooser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class ElementListRecyclerViewAdapter(private val context: Context, private var data: MutableList<Element>) : RecyclerView.Adapter<ElementListRecyclerViewAdapter.ViewHolder>() {
    var isEditMode = false

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.element_name)
    }

    class EditModeViewHolder(val view: View) : ViewHolder(view) {
        val xButton: Button = view.findViewById(R.id.element_item_x_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        return if (viewType == 0) {
            ViewHolder(layoutInflater.inflate(R.layout.element_item, parent, false))
        }else {
            EditModeViewHolder(layoutInflater.inflate(R.layout.element_item_edit_mode, parent, false))
        }
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].name

        if (holder.itemViewType == 1){
            val editHolder = holder as EditModeViewHolder
            editHolder.view.setOnClickListener {
                val dialog = AddElementDialogFragment(data[holder.adapterPosition], holder.adapterPosition)
                dialog.show((context as AppCompatActivity).supportFragmentManager, "ElementDialog")
            }

            editHolder.xButton.setOnClickListener {
                data.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
            }
        }
    }

    override fun getItemViewType(position: Int) = if (isEditMode) 1 else 0

    fun toggleEditMode() {
        isEditMode = !isEditMode
        notifyDataSetChanged()
    }

    fun addElement(element: Element) {
        data.add(element)
        notifyItemInserted(data.size)
    }

    fun modifyElement(element: Element, position: Int) {
        data[position] = element
        notifyItemChanged(position)
    }
}