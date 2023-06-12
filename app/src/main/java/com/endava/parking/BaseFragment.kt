package com.endava.parking

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.endava.parking.ui.navigation.NavigationCallback

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater, container: ViewGroup?, bool: Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null
    private var _navigationCallback: NavigationCallback? = null

    protected val binding: VB
        get() = _binding as VB
    protected val navigationCallback: NavigationCallback
        get() = _navigationCallback as NavigationCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _navigationCallback = context as NavigationCallback?
    }

    override fun onDetach() {
        super.onDetach()
        _navigationCallback = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
