package vn.com.nexle.entrancetest.viewmodel

import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import vn.com.nexle.entrancetest.presentation.signin.viewmodel.ViewModelSignIn
import vn.com.nexle.entrancetest.repository.FakeRepository

class ViewModelTest {
    private lateinit var entranceRepository: FakeRepository
    private lateinit var viewModel: ViewModelSignIn
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        entranceRepository = mockk()
        viewModel = ViewModelSignIn(entranceRepository)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    private val displayName = "Quy Phung"
    private val emitValue = "Quy Phung"
    @Test
    fun test_TextDisplay_FlowReceived() = runTest {
        //Given
        viewModel.testEmitText(emitValue)
        //When
        val nameDisplay = viewModel.textToDisplay.first()
        //Then
        assertEquals(displayName, nameDisplay)
    }

    @Test
    fun test_NavigateValue_FlowReceived() = runTest {
        //Given
        viewModel.testEmitNavigationValue(emitValue)
        //When
        val nameDisplay = viewModel.navigateToResults.first()
        //Then
        assertEquals(displayName, nameDisplay)
    }
}