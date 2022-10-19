package vn.com.nexle.entrancetest.presentation.dashboard.view

import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.avatarview.glide.loadImage
import kotlinx.coroutines.launch
import vn.com.nexle.entrancetest.R
import vn.com.nexle.entrancetest.databinding.FragmentDashboardBinding
import vn.com.nexle.entrancetest.presentation.base.BaseFragment
import vn.com.nexle.entrancetest.presentation.signin.view.FragmentSignInDirections
import vn.com.nexle.entrancetest.presentation.signin.viewmodel.ViewModelSignIn

@AndroidEntryPoint
class FragmentDashboard : BaseFragment<FragmentDashboardBinding>(FragmentDashboardBinding::inflate){
    private val viewModel by viewModels<ViewModelSignIn>()
    private val args by navArgs<FragmentDashboardArgs>()

    override fun initialize() {
        binding.apply {
            tvUserName.text = args.displayName.ifEmpty { mMainActivity.localDataSource.getDisplayName() }
            avatarView.loadImage(R.drawable.sample_avatar)

            mMainActivity.setSupportActionBar(toolBar)
            mMainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            mMainActivity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
    }

    override fun setupListeners() {
        binding.apply {
            nvView.setNavigationItemSelectedListener {
                selectDrawerItem(it)
                true
            }
        }
    }

    override fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.navigateToResults.collect {
                        val direction = FragmentDashboardDirections.actionFragmentDashboardToFragmentSignin()
                        navigate(direction)
                    }
                }
            }
        }
    }

    private fun selectDrawerItem(item: MenuItem) {
        when(item.itemId) {
            R.id.nav_logout -> {
                viewModel.logOut()
            }
        }
    }
}