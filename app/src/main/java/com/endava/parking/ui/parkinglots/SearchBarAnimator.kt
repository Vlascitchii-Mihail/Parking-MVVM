package com.endava.parking.ui.parkinglots

import android.graphics.Path
import com.endava.parking.databinding.FragmentParkingLotsBinding

private const val DURATION: Long = 400L

class SearchBarAnimator {

    fun showSearchToolbar(binding: FragmentParkingLotsBinding) {
        with (binding) {
            toolbarSearch.visibility = android.view.View.VISIBLE
            fab.hide()
            inputSearchParking.requestFocus()
            val searchPath = Path().apply {
                moveTo(1500f, 0f)
                lineTo(0f, 0f)
            }
            val toolbarPath = Path().apply {
                moveTo(0f, 0f)
                lineTo(-1500f, 0f)
            }
            val searchAnimator = android.animation.ObjectAnimator.ofFloat(toolbarSearch, android.view.View.X, android.view.View.Y, searchPath).apply {
                duration = 400
                start()
            }
            val toolbarAnimator = android.animation.ObjectAnimator.ofFloat(toolbar, android.view.View.X, android.view.View.Y, toolbarPath).apply {
                duration = 400
                start()
            }
        }
    }

    fun hideSearchToolbar(binding: FragmentParkingLotsBinding) {
        with (binding) {
            fab.show()
            inputSearchParking.setText("")
            val searchPath = Path().apply {
                moveTo(0f, 0f)
                lineTo(-1500f, 0f)
            }
            val toolbarPath = Path().apply {
                moveTo(1500f, 0f)
                lineTo(0f, 0f)
            }
            val searchAnimator = android.animation.ObjectAnimator.ofFloat(toolbarSearch, android.view.View.X, android.view.View.Y, searchPath).apply {
                duration = DURATION
                start()
            }
            val toolbarAnimator = android.animation.ObjectAnimator.ofFloat(toolbar, android.view.View.X, android.view.View.Y, toolbarPath).apply {
                duration = DURATION
                start()
            }
        }
    }
}
