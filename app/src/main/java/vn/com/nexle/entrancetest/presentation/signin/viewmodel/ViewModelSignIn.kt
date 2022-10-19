package vn.com.nexle.entrancetest.presentation.signin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import org.json.JSONObject
import retrofit2.HttpException
import vn.com.nexle.entrancetest.data.repository.EntranceRepository
import javax.inject.Inject

@HiltViewModel
class ViewModelSignIn @Inject constructor(
    private val entranceRepository: EntranceRepository
) : ViewModel() {

    private val _textToDisplay: MutableStateFlow<String> = MutableStateFlow("")
    val textToDisplay = _textToDisplay.asStateFlow()

    private val _navigateToResults = Channel<String>(Channel.BUFFERED)
    val navigateToResults = _navigateToResults.receiveAsFlow()

    fun signIn(email: String, password: String, remember: Boolean) {
        viewModelScope.launch {
            entranceRepository.signIn(email, password, remember).collect { result ->
                result.fold(onSuccess = { item ->
                    _navigateToResults.send(item.displayName)
                }, onFailure = { ex ->
                    val message = try {
                        val json = (ex as? HttpException)?.response()?.errorBody()?.string()
                            ?.let {
                                JSONObject(it)
                            }
                        json?.getJSONObject("errors")?.getString("error")
                    } catch (ex: Exception) {
                        "404 Not found"
                    }

                    _textToDisplay.emit(message.toString())
                })
            }
        }
    }

    fun signUp(firstName: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {
            entranceRepository.signUp(firstName, lastName, email, password).collect { result ->
                result.fold(onSuccess = { item ->
                    _navigateToResults.send(item.displayName)
                }, onFailure = { ex ->
                    val message = try {
                        val json = (ex as? HttpException)?.response()?.errorBody()?.string()
                            ?.let {
                                JSONObject(it)
                            }
                        json?.getJSONObject("errors")?.getString("error")
                    } catch (ex: Exception) {
                        "404 Not found"
                    }

                    _textToDisplay.emit(message.toString())
                })
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            entranceRepository.logOut().collect()
            _navigateToResults.send("logout")
        }
    }

    @TestOnly
    fun testEmitText(string: String) {
        viewModelScope.launch {
            _textToDisplay.emit(string)
        }
    }

    @TestOnly
    fun testEmitNavigationValue(string: String) {
        viewModelScope.launch {
            _navigateToResults.send(string)
        }
    }
}