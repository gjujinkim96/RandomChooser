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

class AddElementDialogFragment : DialogFragment() {
    private lateinit var listener: AddElementDialogListener

    interface AddElementDialogListener {
        fun onDialogPositive(element: Element)
        fun onDialogNegative()
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

            builder.setView(view)
                .setPositiveButton(R.string.add_button) { _, _ ->
                    val newName = view.findViewById<TextView>(R.id.add_element_name_data).text.toString()
                    val newElement = Element(newName)
                    listener.onDialogPositive(newElement)
                }
                .setNegativeButton(R.string.cancel_button) { _, _ ->
                    listener.onDialogNegative()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}