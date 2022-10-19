package vn.com.nexle.entrancetest.view

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import vn.com.nexle.entrancetest.R
import vn.com.nexle.entrancetest.presentation.signin.view.FragmentSignIn
import vn.com.nexle.entrancetest.presentation.signin.view.FragmentSignUp

@RunWith(AndroidJUnit4::class)
@LargeTest
class FragmentTest : TestCase() {
    private val signInScenario = launchFragmentInContainer<FragmentSignIn>(
        initialState = Lifecycle.State.INITIALIZED
    )
    private val signUpScenario = launchFragmentInContainer<FragmentSignUp>(
        initialState = Lifecycle.State.INITIALIZED
    )

    @Before
    fun init() {
        signInScenario.moveToState(Lifecycle.State.STARTED)
        signUpScenario.moveToState(Lifecycle.State.STARTED)
    }

    private val falseEmail = "agad"
    private val falsePassword = "a"
    private val falseFName = "a"
    private val falseLName = "b"
    @Test
    fun testInputFalse() {
        onView(withId(R.id.edtEmail)).perform(typeText(falseEmail))
        onView(withId(R.id.edtPassword)).perform(typeText(falsePassword))
        onView(withId(R.id.edtFirstName)).perform(typeText(falseFName))
        onView(withId(R.id.edtLastName)).perform(typeText(falseLName))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.btnSignIn)).perform(click())

        onView(withId(R.id.tvEmailErrorMessage)).check(matches(withText(R.string.str_signin_error_email_not_valid)))
        onView(withId(R.id.tvPasswordErrorMessage)).check(matches(withText(R.string.str_signin_error_password_not_valid)))
        onView(withId(R.id.tvFirstNameErrorMessage)).check(matches(withText(R.string.str_signup_error_first_name_not_valid)))
        onView(withId(R.id.tvLastNameErrorMessage)).check(matches(withText(R.string.str_signup_error_last_name_not_valid)))
    }

    @Test
    fun testInputEmpty() {
        onView(withId(R.id.btnSignIn)).perform(click())

        onView(withId(R.id.tvEmailErrorMessage)).check(matches(withText(R.string.str_signin_error_email_empty)))
        onView(withId(R.id.tvPasswordErrorMessage)).check(matches(withText(R.string.str_signin_error_password_empty)))
        onView(withId(R.id.tvFirstNameErrorMessage)).check(matches(withText(R.string.str_signup_error_first_name_empty)))
        onView(withId(R.id.tvLastNameErrorMessage)).check(matches(withText(R.string.str_signup_error_last_name_empty)))
    }
}