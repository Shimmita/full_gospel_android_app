package com.shimmita.full_gospel.viewmodel

import RetrofitClient
import User
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shimmita.full_gospel.model.PostModel
import com.shimmita.full_gospel.auth.DataStoreManager
import com.shimmita.full_gospel.model.PostResponse
import com.shimmita.full_gospel.model.UserLogin
import com.shimmita.full_gospel.model.UserRegister
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    //user logged in state
    val isLoggedIn = dataStoreManager.isLoggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )


    //contains user data
    val globalUserState: StateFlow<User?> =
        dataStoreManager.userProfile
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                null
            )


    //temp key state
    private val globalTempKey: StateFlow<String?> =
        dataStoreManager.tempKeyValue
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                null
            )


    val authReady: StateFlow<Boolean> =
        combine(globalUserState, globalTempKey) { user, key ->
            !user?.username.isNullOrBlank() && !key.isNullOrBlank()
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            false
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()


    //AUTH

    //login user
    fun login(
        username: String,
        password: String,
        onResult: (success: Boolean, message: String) -> Unit
    ) {
        viewModelScope.launch {
            // ✅ Check empty fields
            if (username.isBlank() || password.isBlank()) {
                onResult(false, "Please fill in all fields")
                return@launch
            }

            try {
                val userReq = UserLogin(username = username, password = password)
                val resp = RetrofitClient.instance.loginUser(userReq)

                if (resp.isSuccessful && resp.body() != null) {
                    val body = resp.body()!!
                    Log.i(AUTHTAG, "Login: SUCCESS ---> $body")

                    // Save temp user key
                    dataStoreManager.setLoggedIn(loginValue = true, tempKey = password)

                    val user = User(
                        userId = body.userId,
                        username = body.username,
                        phone = body.phone,
                        first_name = body.first_name,
                        last_name = body.last_name,
                        role = body.role,
                        imagePath = body.imagePath,
                        enabled = body.enabled,
                        createdAt = body.createdAt,
                        updatedAt = body.updatedAt,
                    )

                    // Save user data
                    dataStoreManager.saveUser(user)

                    onResult(true, "Login Successful")
                } else {
                    val msg = "Login Failed"
                    Log.i(AUTHTAG, "Login: Failed ---> $msg")

                    dataStoreManager.setLoggedIn(loginValue = false, tempKey = "")
                    dataStoreManager.logout()

                    onResult(false, msg)
                }
            } catch (e: Exception) {
                Log.i(AUTHTAG, "Login: Exception ---> ${e.message}")
                dataStoreManager.setLoggedIn(loginValue = false, tempKey = "")
                dataStoreManager.logout()

                onResult(false, "Login Failed")
            }
        }
    }





    fun register(
        username: String,
        password: String,
        phone: String,
        firstName: String,
        lastName: String,
        position: String,
        imagePath: String = "",
        onResult: (success: Boolean, message: String) -> Unit
    ) {
        viewModelScope.launch {
            // ✅ Check if any field is blank
            if (username.isBlank() || password.isBlank() || phone.isBlank() ||
                firstName.isBlank() || lastName.isBlank() || position.isBlank()
            ) {
                onResult(false, "Please fill in all fields")
                return@launch
            }

            try {
                val userReq = UserRegister(
                    username = username,
                    phone = phone,
                    first_name = firstName,
                    last_name = lastName,
                    password = password,
                    role = position,
                    imagePath = imagePath
                )

                val resp = RetrofitClient.instance.registerUser(userReq)
                if (resp.isSuccessful && resp.body() != null) {
                    // backend returns User on success
                    Log.i(AUTHTAG, "Register: SUCCESS ---> ${resp.body()}")
                    onResult(true, "Account Created Successfully")
                } else {
                    // parse error message if available
                    val msg = "Registration failed"
                    Log.i(AUTHTAG, "Register: Failed ---> $msg")
                    onResult(false, msg)
                }

            } catch (e: Exception) {
                Log.i(AUTHTAG, "Register: Exception ---> ${e.message}")
                onResult(false, "Registration Failed")
            }
        }
    }





    //features

    private suspend fun getAllUsers(): List<User>? {
        return try {
            val credentials = "${globalUserState.value?.username}:${globalTempKey.value}"
            val basicAuth = "Basic " + Base64.encodeToString(
                credentials.toByteArray(),
                Base64.NO_WRAP
            )

            RetrofitClient.instance
                .getUsers(basicAuth)
                .body()
        } catch (e: Exception) {
            Log.e("API", "getAllUsers failed", e)
            null
        }
    }

    fun getUserByEmail(username: String, password: String) {
        viewModelScope.launch {
            if (username.isNotBlank() && password.isNotBlank()) {
                val credentials = "$username:$password"
                val basicAuth =
                    "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
                try {
                    val result =
                        RetrofitClient.instance.getUserByEmail(
                            email = username,
                            authHeader = basicAuth
                        );
                    if (result.isSuccessful) {
                        Log.i(AUTHTAG, "getUserByEmail: ${result.body()}")
                    } else {
                        Log.i(AUTHTAG, "Error:getUserByEmail: $result")
                    }
                } catch (e: Exception) {
                    Log.i(AUTHTAG, "Error:--> getUserByEmail: ${e.message}")
                }

            }
        }
    }


    private fun createDailyPrayer(
        username: String,
        phone: String,
        author: String,
        role: String,
        details: String,
        verse: String

    ) {
        viewModelScope.launch {
            val credentials = "$username:${globalTempKey.value}"
            val basicAuth =
                "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
            try {
                val postModel = PostModel(
                    username,
                    phone,
                    author,
                    role,
                    details,
                    verse
                )
                val result =
                    RetrofitClient.instance.createDailyPrayer(
                        postModel,
                        authHeader = basicAuth
                    );
                if (result.isSuccessful) {
                    Log.i(AUTHTAG, "daily prayer: ${result.body()}")
                } else {
                    Log.i(AUTHTAG, "Error:daily prayer: $result")
                }
            } catch (e: Exception) {
                Log.i(AUTHTAG, "Error:--> daily prayer: $e")
            }
        }
    }

    private fun createWeeklyPrayer(
        username: String,
        phone: String,
        author: String,
        role: String,
        details: String,
        verse: String

    ) {
        viewModelScope.launch {
            val credentials = "$username:${globalTempKey.value}"
            val basicAuth =
                "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
            try {
                val postModel = PostModel(
                    username,
                    phone,
                    author,
                    role,
                    details,
                    verse
                )
                val result =
                    RetrofitClient.instance.createWeeklyPrayer(
                        postModel,
                        authHeader = basicAuth
                    );
                if (result.isSuccessful) {
                    Log.i(AUTHTAG, "weekly prayer: ${result.body()}")
                } else {
                    Log.i(AUTHTAG, "Error:weekly prayer: $result")
                }
            } catch (e: Exception) {
                Log.i(AUTHTAG, "Error:--> weekly prayer: $e")
            }
        }
    }


    private fun createTestimony(
        username: String,
        phone: String,
        author: String,
        role: String,
        details: String,
        verse: String

    ) {
        viewModelScope.launch {
            val credentials = "$username:${globalTempKey.value}"
            val basicAuth =
                "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
            try {
                val postModel = PostModel(
                    username,
                    phone,
                    author,
                    role,
                    details,
                    verse
                )
                val result =
                    RetrofitClient.instance.createTestimonial(
                        postModel,
                        authHeader = basicAuth
                    );
                if (result.isSuccessful) {
                    Log.i(AUTHTAG, "Testimony: ${result.body()}")
                } else {
                    Log.i(AUTHTAG, "Error:Testimony: $result")
                }
            } catch (e: Exception) {
                Log.i(AUTHTAG, "Error:--> Testimony: $e")
            }
        }
    }


    private fun createNatureTalent(
        username: String,
        phone: String,
        author: String,
        role: String,
        details: String,
        verse: String

    ) {
        viewModelScope.launch {
            val credentials = "$username:${globalTempKey.value}"
            val basicAuth =
                "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
            try {
                val postModel = PostModel(
                    username,
                    phone,
                    author,
                    role,
                    details,
                    verse
                )
                val result =
                    RetrofitClient.instance.createTalent(
                        postModel,
                        authHeader = basicAuth
                    );
                if (result.isSuccessful) {
                    Log.i(AUTHTAG, "nature talent: ${result.body()}")
                } else {
                    Log.i(AUTHTAG, "Error:nature talent: $result")
                }
            } catch (e: Exception) {
                Log.i(AUTHTAG, "Error:--> nature talent: $e")
            }
        }
    }


    private fun createEvent(
        username: String,
        phone: String,
        author: String,
        role: String,
        details: String,
        verse: String

    ) {
        viewModelScope.launch {
            val credentials = "$username:${globalTempKey.value}"
            val basicAuth =
                "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
            try {
                val postModel = PostModel(
                    username,
                    phone,
                    author,
                    role,
                    details,
                    verse
                )
                val result =
                    RetrofitClient.instance.createEventCalendar(
                        postModel,
                        authHeader = basicAuth
                    );
                if (result.isSuccessful) {
                    Log.i(AUTHTAG, "create event: ${result.body()}")
                } else {
                    Log.i(AUTHTAG, "Error:create event: $result")
                }
            } catch (e: Exception) {
                Log.i(AUTHTAG, "Error:--> create event: $e")
            }
        }
    }

    private fun createAnnouncement(
        username: String,
        phone: String,
        author: String,
        role: String,
        details: String,
        verse: String

    ) {
        viewModelScope.launch {
            val credentials = "$username:${globalTempKey.value}"
            val basicAuth =
                "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
            try {
                val postModel = PostModel(
                    username,
                    phone,
                    author,
                    role,
                    details,
                    verse
                )
                val result =
                    RetrofitClient.instance.createAnnouncement(
                        postModel,
                        authHeader = basicAuth
                    );
                if (result.isSuccessful) {
                    Log.i(AUTHTAG, "create announcement: ${result.body()}")
                } else {
                    Log.i(AUTHTAG, "Error:announcement: $result")
                }
            } catch (e: Exception) {
                Log.i(AUTHTAG, "Error:--> announcement: $e")
            }
        }
    }

    private fun createSundayDiary(
        username: String,
        phone: String,
        author: String,
        role: String,
        details: String,
        verse: String

    ) {
        viewModelScope.launch {
            val credentials = "$username:${globalTempKey.value}"
            val basicAuth =
                "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
            try {
                val postModel = PostModel(
                    username,
                    phone,
                    author,
                    role,
                    details,
                    verse
                )
                val result =
                    RetrofitClient.instance.createSundayDiary(
                        postModel,
                        authHeader = basicAuth
                    );
                if (result.isSuccessful) {
                    Log.i(AUTHTAG, "create sunday diary: ${result.body()}")
                } else {
                    Log.i(AUTHTAG, "Error:sunday diary: $result")
                }
            } catch (e: Exception) {
                Log.i(AUTHTAG, "Error:--> sunday diary: $e")
            }
        }
    }


    private suspend fun getAllDailyPrayers(): List<PostResponse>? {
        return try {
            val credentials = "${globalUserState.value?.username}:${globalTempKey.value}"
            val basicAuth = "Basic " + Base64.encodeToString(
                credentials.toByteArray(),
                Base64.NO_WRAP
            )

            RetrofitClient.instance
                .getAllDailyPrayers(basicAuth)
                .body()
        } catch (e: Exception) {
            Log.e("API", "getAllDailyPrayers failed", e)
            null
        }
    }


    private suspend fun getAllWeeklyPrayers(): List<PostResponse>? {
        return try {
            val credentials = "${globalUserState.value?.username}:${globalTempKey.value}"
            val basicAuth = "Basic " + Base64.encodeToString(
                credentials.toByteArray(),
                Base64.NO_WRAP
            )

            RetrofitClient.instance
                .getAllWeeklyPrayers(basicAuth)
                .body()
        } catch (e: Exception) {
            Log.e("API", "getAllWeeklyPrayers failed", e)
            null
        }
    }


    private suspend fun getAllNatureTalents(): List<PostResponse>? {
        return try {
            val credentials = "${globalUserState.value?.username}:${globalTempKey.value}"
            val basicAuth = "Basic " + Base64.encodeToString(
                credentials.toByteArray(),
                Base64.NO_WRAP
            )

            RetrofitClient.instance
                .getAllTalents(basicAuth)
                .body()
        } catch (e: Exception) {
            Log.e("API", "getAllNatureTalents failed", e)
            null
        }
    }


    private suspend fun getAllTestimonials(): List<PostResponse>? {
        return try {
            val credentials = "${globalUserState.value?.username}:${globalTempKey.value}"
            val basicAuth = "Basic " + Base64.encodeToString(
                credentials.toByteArray(),
                Base64.NO_WRAP
            )

            RetrofitClient.instance
                .getAllTestimonials(basicAuth)
                .body()
        } catch (e: Exception) {
            Log.e("API", "getAllTestimonials failed", e)
            null
        }
    }


    private suspend fun getEventsCalendar(): List<PostResponse>? {
        return try {
            val credentials = "${globalUserState.value?.username}:${globalTempKey.value}"
            val basicAuth = "Basic " + Base64.encodeToString(
                credentials.toByteArray(),
                Base64.NO_WRAP
            )

            RetrofitClient.instance
                .getAllEventsCalendar(basicAuth)
                .body()
        } catch (e: Exception) {
            Log.e("API", "getEventsCalendar failed", e)
            null
        }
    }


    private suspend fun getAllSundayDiaries(): List<PostResponse>? {
        return try {
            val credentials = "${globalUserState.value?.username}:${globalTempKey.value}"
            val basicAuth = "Basic " + Base64.encodeToString(
                credentials.toByteArray(),
                Base64.NO_WRAP
            )

            RetrofitClient.instance
                .getAllSundayDiaries(basicAuth)
                .body()
        } catch (e: Exception) {
            Log.e("API", "getAllSundayDiaries failed", e)
            null
        }
    }


    private suspend fun getAllAnnouncements(): List<PostResponse>? {
        return try {
            val credentials = "${globalUserState.value?.username}:${globalTempKey.value}"
            val basicAuth = "Basic " + Base64.encodeToString(
                credentials.toByteArray(),
                Base64.NO_WRAP
            )

            RetrofitClient.instance
                .getAllAnnouncements(basicAuth)
                .body()
        } catch (e: Exception) {
            Log.e("API", "getAllAnnouncements failed", e)
            null
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.logout()
        }
    }


    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers = _allUsers.asStateFlow()

    private val _dailyPrayers = MutableStateFlow<List<PostResponse>>(emptyList())
    val dailyPrayers = _dailyPrayers.asStateFlow()

    private val _weeklyPrayers = MutableStateFlow<List<PostResponse>>(emptyList())
    val weeklyPrayers = _weeklyPrayers.asStateFlow()

    private val _events = MutableStateFlow<List<PostResponse>>(emptyList())
    val events = _events.asStateFlow()

    private val _testimonials = MutableStateFlow<List<PostResponse>>(emptyList())
    val testimonials = _testimonials.asStateFlow()

    private val _natureTalents = MutableStateFlow<List<PostResponse>>(emptyList())
    val natureTalents = _natureTalents.asStateFlow()

    private val _announcements = MutableStateFlow<List<PostResponse>>(emptyList())
    val announcements = _announcements.asStateFlow()

    private val _sundayDiaries = MutableStateFlow<List<PostResponse>>(emptyList())
    val sundayDiaries = _sundayDiaries.asStateFlow()


    fun loadUsersData(isRefresh: Boolean = false){
        viewModelScope.launch {
            if (!authReady.value) return@launch

            if (isRefresh) {
                _isRefreshing.value = true
            } else {
                _isLoading.value = true
            }

            try {
                _allUsers.value = getAllUsers() ?: emptyList()

            }finally {
                _isLoading.value = false
                _isRefreshing.value = false
            }
        }
    }

    fun loadStarterScreenData(isRefresh: Boolean = false) {
        viewModelScope.launch {

            if (!authReady.value) return@launch

            if (isRefresh) {
                _isRefreshing.value = true
            } else {
                _isLoading.value = true
            }

            try {
                _dailyPrayers.value = getAllDailyPrayers() ?: emptyList()
                _weeklyPrayers.value = getAllWeeklyPrayers() ?: emptyList()
                _events.value = getEventsCalendar() ?: emptyList()
                _testimonials.value = getAllTestimonials() ?: emptyList()
                _natureTalents.value = getAllNatureTalents() ?: emptyList()
                _announcements.value = getAllAnnouncements() ?: emptyList()
                _sundayDiaries.value = getAllSundayDiaries() ?: emptyList()
            } finally {
                _isLoading.value = false
                _isRefreshing.value = false
            }
        }
    }

     fun createPost(
        category: String,
        username: String,
        phone: String,
        author: String,
        role: String,
        details: String,
        verse: String
    ): Boolean {
        return try {
            when {
                category.contains("Daily") -> createDailyPrayer(username, phone, author, role, details, verse)
                category.contains("Weekly") -> createWeeklyPrayer(username, phone, author, role, details, verse)
                category.contains("Event") -> createEvent(username, phone, author, role, details, verse)
                category.contains("Talent") -> createNatureTalent(username, phone, author, role, details, verse)
                category.contains("Test") -> createTestimony(username, phone, author, role, details, verse)
                category.contains("Sunday") -> createSundayDiary(username, phone, author, role, details, verse)
                category.contains("Announce") -> createAnnouncement(username, phone, author, role, details, verse)
                else -> throw IllegalArgumentException("Unknown category")
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }



    companion object {
        private const val AUTHTAG = "AuthTag"
    }
}
