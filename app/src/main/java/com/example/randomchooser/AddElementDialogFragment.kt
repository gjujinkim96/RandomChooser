package com.example.randomchooser

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.add_element_dialog.*
import java.lang.ClassCastException
import java.lang.IllegalStateException
import java.text.FieldPosition

class AddElementDialogFragment(private val defaultElement: Element?, private val defaultPosition: Int?) : DialogFragment() {
    constructor() : this(null, null)

    private lateinit var listener: AddElementDialogListener

    interface AddElementDialogListener {
        fun onDialogPositive(element: Element)
        fun onDialogNegative()
        fun onDialogModify(element: Element, position: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as AddElementDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException(context.toString() + "must implement AddElementDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.add_element_dialog, null)

            var positiveButtonTextId = R.string.add_button

            val nameData = view.findViewById<TextView>(R.id.add_element_name_data)

            if (defaultElement != null && defaultPosition != null) {
                view.findViewById<TextView>(R.id.add_element_title).text = requireContext().getText(R.string.modify_element_title)
                positiveButtonTextId = R.string.modify_button
                nameData.text = defaultElement.name
            }

            builder.setView(view)
                .setPositiveButton(positiveButtonTextId) { _, _ ->
                    val newName = nameData.text.toString()
                    val newElement = Element(newName)

                    if (defaultElement != null && defaultPosition != null) {
                        listener.onDialogModify(newElement, defaultPosition)
                    } else {
                        listener.onDialogPositive(newElement)
                    }
                }
                .setNegativeButton(R.string.cancel_button) { _, _ ->
                    listener.onDialogNegative()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}