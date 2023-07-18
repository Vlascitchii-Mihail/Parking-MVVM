package com.endava.parking.ui.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class SingleEventLiveData<T> : MutableLiveData<T>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer {
            if (it == null) return@Observer
            observer.onChanged(it)
            value = null
        })
    }
}
