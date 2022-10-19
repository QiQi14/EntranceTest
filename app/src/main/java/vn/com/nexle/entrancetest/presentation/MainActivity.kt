package vn.com.nexle.entrancetest.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import vn.com.nexle.entrancetest.R
import vn.com.nexle.entrancetest.data.local.source.PreferenceSource
import vn.com.nexle.entrancetest.databinding.ActivityMainBinding
import vn.com.nexle.entrancetest.presentation.base.BaseActivity
import vn.com.nexle.entrancetest.presentation.dashboard.view.FragmentDashboard
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    @Inject
    lateinit var localDataSource: PreferenceSource

    private var navHostFragment: NavHostFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment?.navController?.navInflater
        val navGraph = inflater?.inflate(R.navigation.navigation_main)

        if (localDataSource.getAccessToken().isNotEmpty()) {
            navGraph?.setStartDestination(R.id.fragmentDashboard)
        } else {
            navGraph?.setStartDestination(R.id.fragmentSignin)
        }

        navHostFragment?.navController?.graph = navGraph!!
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
}