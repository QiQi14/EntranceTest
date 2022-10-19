package vn.com.nexle.entrancetest.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vn.com.nexle.entrancetest.presentation.MainActivity
import java.lang.Exception

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T
abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!
    val mMainActivity: MainActivity by lazy { activity as MainActivity }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBinding(inflater, container!!)
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    private fun initBinding(layoutInflater: LayoutInflater, container: ViewGroup) {
        _binding = inflate.invoke(layoutInflater, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.actionBar?.hide()
        initialize()
        setupListeners()
        observe()
    }

    abstract fun initialize()

    abstract fun setupListeners()

    abstract fun observe()

    open fun navigate(action: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            view?.let { _view ->
                try {
                    Navigation.findNavController(_view).navigate(action)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    open fun navigate(action: NavDirections) {
        lifecycleScope.launch(Dispatchers.Main) {
            view?.let { _view ->
                try {
                    Navigation.findNavController(_view).navigate(action)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    open fun navigateUp() {
        lifecycleScope.launch(Dispatchers.Main) {
            findNavController().navigateUp()
        }
    }

    open fun navigateUp(@IdRes destination: Int, inclusive: Boolean) {
        lifecycleScope.launch(Dispatchers.Main) {
            findNavController().popBackStack(destination, inclusive)
        }
    }
}