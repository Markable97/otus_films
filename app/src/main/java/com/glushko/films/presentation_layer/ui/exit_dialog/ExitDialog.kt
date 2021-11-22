package com.glushko.films.presentation_layer.ui.exit_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.glushko.films.R
import java.lang.ClassCastException

class ExitDialog: DialogFragment() {

    lateinit var listener: OnDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as OnDialogListener
        }catch (ex: ClassCastException){
            throw ClassCastException("must implement OnDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_custom)
            .setNegativeButton(android.R.string.cancel) { dialog, which -> listener.onClickDialog(false)}
            .setPositiveButton(android.R.string.ok) { dialog, which -> listener.onClickDialog(true)}
            .create()
    }

    interface OnDialogListener{
        fun onClickDialog(exit: Boolean)
    }

}