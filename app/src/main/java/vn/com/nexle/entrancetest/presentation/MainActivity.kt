package vn.com.nexle.entrancetest.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.auth0.android.jwt.JWT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import vn.com.nexle.entrancetest.R
import vn.com.nexle.entrancetest.data.local.source.PreferenceSource
import vn.com.nexle.entrancetest.databinding.ActivityMainBinding
import vn.com.nexle.entrancetest.extension.SlideDirection
import vn.com.nexle.entrancetest.extension.SlideType
import vn.com.nexle.entrancetest.extension.slideAnimation
import vn.com.nexle.entrancetest.presentation.base.BaseActivity
import vn.com.nexle.entrancetest.presentation.dashboard.view.FragmentDashboard
import vn.com.nexle.entrancetest.presentation.signin.viewmodel.ViewModelSignIn
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    private val viewModel by viewModels<ViewModelSignIn>()

    @Inject
    lateinit var localDataSource: PreferenceSource

    private var navHostFragment: NavHostFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment?.navController?.navInflater
        val navGraph = inflater?.inflate(R.navigation.navigation_main)

        val accessToken = localDataSource.getAccessToken()
        if (accessToken.isNotEmpty() && (JWT(accessToken).expiresAt?.time ?: 0) - System.currentTimeMillis() > 1000) {
            navGraph?.setStartDestination(R.id.fragmentDashboard)
        } else {
            viewModel.logOut()
            navGraph?.setStartDestination(R.id.fragmentSignin)
        }

        navHostFragment?.navController?.graph = navGraph!!
        observeConnection()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val fragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
                if (fragment is FragmentDashboard) {
                    fragment.binding.drawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    var connectivity : Boolean = true
    private fun observeConnection() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startListeningToNetwork()
                viewModel.connectivity.collect {
                    if (it) {
                        hideToast()
                    } else {
                        showToast()
                    }
                    connectivity = it
                }
            }
        }
    }

    private fun showToast() {
        binding.toastMessage.slideAnimation(SlideDirection.UP, SlideType.SHOW)
    }

    private fun hideToast() {
        binding.toastMessage.slideAnimation(SlideDirection.DOWN, SlideType.HIDE)
    }
}