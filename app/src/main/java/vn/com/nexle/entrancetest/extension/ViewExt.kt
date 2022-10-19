package vn.com.nexle.entrancetest.extension

import android.view.View

fun View.setOnSingleClickListener(debounceTime: Long = 200L, action: (() -> Unit)) {
    setOnClickListener { view ->
        if (view?.isClickable == true) {
            view?.isClickable = false
            action()
        }
        view?.postDelayed({
            view?.isClickable = true
        }, debounceTime)
    }
}