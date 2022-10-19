package vn.com.nexle.entrancetest.data.remote.model

data class SignInItem(
    val _id: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val displayName: String,
    val token: String = "",
    val refreshToken: String = ""
)
