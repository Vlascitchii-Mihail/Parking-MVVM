package com.endava.parking.ui.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.endava.parking.R
import com.endava.parking.databinding.PopUpConfirmationBinding
import com.endava.parking.ui.createparkinglot.FragmentCreateParkingLot

fun createAlertDialog(context: Context, message: String, navigateAction: () -> Unit): AlertDialog? {
    val customDialogView = PopUpConfirmationBinding.inflate(LayoutInflater.from(context))
    val dialogBuilder = AlertDialog.Builder(context)
    dialogBuilder.setView(customDialogView.root)
    val customDialog = dialogBuilder.create()
    customDialog.window?.setBackgroundDrawableResource(R.color.transparent)

    customDialogView.tvResponseText.text =
        if (message == FragmentCreateParkingLot.POSITIVE_CODE) {
            customDialogView.btnConfirmPopup.setOnClickListener {
                navigateAction.invoke()
                customDialog.dismiss()
            }
            context.getText(R.string.lot_admin_parking_positive_message).toString()
        } else {
            customDialogView.btnConfirmPopup.setOnClickListener {
                customDialog.dismiss()
            }
            message
        }

    return customDialog
}
