package com.endava.parking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.endava.parking.utils.KeyboardManager

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater, container: ViewGroup?, bool: Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null
    private var _navController: NavController? = null

    protected val binding: VB
        get() = _binding as VB
    protected val navController: NavController
        get() =_navController as NavController

    private var keyboardManager: KeyboardManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        _navController = findNavController()
        return binding.root
    }

    protected fun setOnKeyboardCloseListener(listener: () -> Unit) {
        keyboardManager = KeyboardManager(binding.root)
        keyboardManager?.setOnCloseActionListener(listener)
    }

    override fun onStop() {
        super.onStop()
        keyboardManager?.unregister()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _navController = null
    }
}
