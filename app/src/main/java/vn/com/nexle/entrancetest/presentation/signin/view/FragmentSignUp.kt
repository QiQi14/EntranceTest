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
import vn.com.nexle.entrancetest.databinding.FragmentSignupBinding
import vn.com.nexle.entrancetest.extension.isValidEmail
import vn.com.nexle.entrancetest.extension.isValidName
import vn.com.nexle.entrancetest.extension.isValidPassword
import vn.com.nexle.entrancetest.extension.setOnSingleClickListener
import vn.com.nexle.entrancetest.presentation.base.BaseFragment
import vn.com.nexle.entrancetest.presentation.signin.viewmodel.ViewModelSignIn
import vn.com.nexle.entrancetest.presentation.widget.CustomBulletTransformationMethod

@AndroidEntryPoint
class FragmentSignUp : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {
    private val viewModel by activityViewModels<ViewModelSignIn>()

    override fun initialize() {
        binding.edtPassword.transformationMethod = CustomBulletTransformationMethod()
    }

    override fun setupListeners() {
        addTextWatcher()
        binding.apply {
            btnSignUp.setOnSingleClickListener {
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

                if (!edtFirstName.text.toString().isValidName()) {
                    if (edtFirstName.text.isEmpty()) {
                        tvFirstNameErrorMessage.text =
                            getText(R.string.str_signup_error_first_name_empty)
                    } else {
                        tvFirstNameErrorMessage.text =
                            getText(R.string.str_signup_error_first_name_not_valid)
                    }
                }

                if (!edtLastName.text.toString().isValidName()) {
                    if (edtLastName.text.isEmpty()) {
                        tvLastNameErrorMessage.text =
                            getText(R.string.str_signup_error_last_name_empty)
                    } else {
                        tvLastNameErrorMessage.text =
                            getText(R.string.str_signup_error_last_name_not_valid)
                    }
                }

                if (tvPasswordErrorMessage.text.isEmpty() && tvEmailErrorMessage.text.isEmpty()
                    && tvFirstNameErrorMessage.text.isEmpty() && tvLastNameErrorMessage.text.isEmpty())
                    viewModel.signUp(edtFirstName.text.toString(), edtLastName.text.toString(), edtEmail.text.toString(), edtPassword.text.toString())
            }

            tvSignIn.setOnSingleClickListener {
                navigateUp()
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
                        val direction = FragmentSignUpDirections.actionFragmentSignupToFragmentDashboard(
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
            btnSignUp.isEnabled = enable
            btnSignUp.isClickable = enable
        }
    }

    private var watcher: TextWatcher? = null
    private fun addTextWatcher() {
        binding.apply {
            watcher?.let {
                edtEmail.removeTextChangedListener(it)
                edtPassword.removeTextChangedListener(it)
                edtFirstName.removeTextChangedListener(it)
                edtLastName.removeTextChangedListener(it)
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

                    if (tvFirstNameErrorMessage.text.isNotEmpty()) {
                        tvFirstNameErrorMessage.text = ""
                    }

                    if (tvLastNameErrorMessage.text.isNotEmpty()) {
                        tvLastNameErrorMessage.text = ""
                    }
                }
            }

            edtEmail.addTextChangedListener(watcher)
            edtPassword.addTextChangedListener(watcher)
            edtFirstName.addTextChangedListener(watcher)
            edtLastName.addTextChangedListener(watcher)
        }
    }
}