package vn.com.nexle.entrancetest.presentation.signin.view

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import vn.com.nexle.entrancetest.R
import vn.com.nexle.entrancetest.databinding.FragmentSigninBinding
import vn.com.nexle.entrancetest.extension.isValidEmail
import vn.com.nexle.entrancetest.extension.isValidPassword
import vn.com.nexle.entrancetest.extension.setOnSingleClickListener
import vn.com.nexle.entrancetest.presentation.base.BaseFragment
import vn.com.nexle.entrancetest.presentation.signin.viewmodel.ViewModelSignIn
import vn.com.nexle.entrancetest.presentation.widget.CustomBulletTransformationMethod

@AndroidEntryPoint
class FragmentSignIn : BaseFragment<FragmentSigninBinding>(FragmentSigninBinding::inflate) {
    private val viewModel by activityViewModels<ViewModelSignIn>()

    override fun initialize() {
        binding.edtPassword.transformationMethod = CustomBulletTransformationMethod()
    }

    override fun setupListeners() {
        addTextWatcher()
        binding.apply {
            btnSignIn.setOnSingleClickListener {
                if (mMainActivity.connectivity)

                if (!edtEmail.text.toString().isValidEmail()) {
                    if (edtEmail.text.isEmpty()) {
                        tvEmailErrorMessage.text = getText(R.string.str_signin_error_email_empty)
                    } else {
                        tvEmailErrorMessage.text = getText(R.string.str_signin_error_email_not_valid)
                    }
                }

                if (!edtPassword.text.toString().isValidPassword()) {
                    if (edtPassword.text.isEmpty()) {
                        tvPasswordErrorMessage.text =
                            getText(R.string.str_signin_error_password_empty)
                    } else {
                        tvPasswordErrorMessage.text =
                            getText(R.string.str_signin_error_password_not_valid)
                    }
                }

                if (tvPasswordErrorMessage.text.isEmpty() && tvEmailErrorMessage.text.isEmpty())
                    viewModel.signIn(edtEmail.text.toString(), edtPassword.text.toString(), checkBox.isChecked)
            }

            tvSignInRegister.setOnSingleClickListener {
                binding.apply {
                    if (edtEmail.text.isNotEmpty()) {
                        edtEmail.text.clear()
                    }

                    if (edtPassword.text.isNotEmpty()) {
                        edtPassword.text.clear()
                    }

                    if (tvEmailErrorMessage.text.isNotEmpty()) {
                        tvEmailErrorMessage.text = ""
                    }

                    if (tvPasswordErrorMessage.text.isNotEmpty()) {
                        tvPasswordErrorMessage.text = ""
                    }
                }

                val directions = FragmentSignInDirections.actionFragmentSigninToFragmentSignup()
                navigate(directions)
            }
        }
    }

    override fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.textToDisplay.collect {
                        if (it.contains("password"))
                            binding.tvPasswordErrorMessage.text = it
                        else
                            binding.tvEmailErrorMessage.text = it
                    }
                }

                launch {
                    viewModel.navigateToResults.collect {
                        if (it == "logout") {
                            return@collect
                        }
                        binding.apply {
                            if (edtEmail.text.isNotEmpty()) {
                                edtEmail.text.clear()
                            }

                            if (edtPassword.text.isNotEmpty()) {
                                edtPassword.text.clear()
                            }

                            if (tvEmailErrorMessage.text.isNotEmpty()) {
                                tvEmailErrorMessage.text = ""
                            }

                            if (tvPasswordErrorMessage.text.isNotEmpty()) {
                                tvPasswordErrorMessage.text = ""
                            }
                        }

                        val direction = FragmentSignInDirections.actionFragmentSigninToFragmentDashboard(
                            displayName = it
                        )
                        navigate(direction)
                    }
                }

                launch {
                    viewModel.connectivity.collect {
                        buttonClickControl(it)
                    }
                }
            }
        }
    }

    private fun buttonClickControl(enable: Boolean) {
        binding.apply {
            btnSignIn.isEnabled = enable
            btnSignIn.isClickable = enable
        }
    }

    private var watcher: TextWatcher? = null
    private fun addTextWatcher() {
        binding.apply {
            watcher?.let {
                edtEmail.removeTextChangedListener(it)
                edtPassword.removeTextChangedListener(it)
                watcher = null
            }

            watcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (tvEmailErrorMessage.text.isNotEmpty()) {
                        tvEmailErrorMessage.text = ""
                    }

                    if (tvPasswordErrorMessage.text.isNotEmpty()) {
                        tvPasswordErrorMessage.text = ""
                    }
                }
            }

            edtEmail.addTextChangedListener(watcher)
            edtPassword.addTextChangedListener(watcher)
        }
    }
}