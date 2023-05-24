package com.endava.parking.ui.navigation

import androidx.fragment.app.Fragment

interface NavigationCallback {

    fun navigate(fragment: Fragment, stackName: String? = null)

    fun popBackStack()
}
