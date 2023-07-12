package com.endava.parking

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.endava.parking.utils.KeyboardManager

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater, container: ViewGroup?, bool: Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null

    protected val binding: VB
        get() = _binding as VB

    private var keyboardManager: KeyboardManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    protected fun setOnKeyboardStateListener(
        openListener: (() -> Unit?)? = null,
        closeListener: (() -> Unit?)? = null
    ) {
        keyboardManager = KeyboardManager(binding.root)
        keyboardManager?.setOnStateChangeActionListener(
            openListener = { openListener?.invoke() },
            closeListener = { closeListener?.invoke() }
        )
    }

    protected fun showKeyboard() {
        val view = activity?.currentFocus
        val methodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        assert(view != null)
        methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    protected fun hideKeyboard() {
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onStop() {
        super.onStop()
        keyboardManager?.unregister()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
