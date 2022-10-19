package vn.com.nexle.entrancetest.presentation.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding>: AppCompatActivity() {

    lateinit var binding: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestOrientation()
        binding = getViewBinding()
        setContentView(binding.root)
    }

    abstract fun getViewBinding(): VB

    fun requestOrientation(orientation: Int = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        requestedOrientation = orientation
    }
}