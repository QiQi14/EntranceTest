package vn.com.nexle.entrancetest.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import vn.com.nexle.entrancetest.data.remote.model.SignInItem

class RepoTest {

    private val testEmail: String = "test1@gmail.com"
    private val testPassword: String = "nopassword"
    @Test
    fun test_SignIn_FlowReceived() = runTest {
        //Given
        val repository = FakeRepository()
        //When
        val item = repository.signIn(testEmail, testPassword, true).first()
        //Then
        Assert.assertEquals(signInItem, item)
    }

    private val testFName: String = "Quy"
    private val testLName: String = "Phung"
    @Test
    fun test_SignUp_FlowReceived() = runTest {
        //Given
        val repository = FakeRepository()
        //When
        val item = repository.signUp(testFName, testLName, testEmail, testPassword).first()
        //Then
        Assert.assertEquals(signInItem, item)
    }

    private val signInItem = Result.success(
        SignInItem (
            _id = "627a67b3dfcfa579ffcc525e",
            email = "test1@gmail.com",
            firstName = "Phung",
            lastName = "Quy",
            displayName = "Phung Quy",
            token = "token",
            refreshToken = "refreshToken",
        )
    )
}