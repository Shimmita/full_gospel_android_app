import com.shimmita.full_gospel.model.PostModel
import com.shimmita.full_gospel.model.PostResponse
import com.shimmita.full_gospel.model.UserLogin
import com.shimmita.full_gospel.model.UserRegister
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    //fetch users
    @GET("users/all")
    suspend fun getUsers(
        @Header("Authorization") authHeader: String
    ): Response<List<User>>

    //user login
    @POST("users/login")
    suspend fun loginUser(@Body user: UserLogin): Response<User>

    //user register
    @POST("users/register")
    suspend fun registerUser(@Body user: UserRegister): Response<User>

    @GET("users/specific/{email}")
    suspend fun getUserByEmail(@Path("email") email: String,@Header("Authorization") authHeader: String): Response<User>

    @PUT("users/update/{email}")
    suspend fun updateUser(@Path("email") email: String, @Body user: User,@Header("Authorization") authHeader: String): Response<User>

    // Daily prayers
    @POST("daily/create")
    suspend fun createDailyPrayer(@Body prayer: PostModel, @Header("Authorization") authHeader: String): Response<PostResponse>

    @GET("daily/all")
    suspend fun getAllDailyPrayers(@Header("Authorization") authHeader: String):Response<List<PostResponse>>

    @GET("daily/{id}")
    suspend fun getDailyPrayer(@Path("id") id: Long, @Header("Authorization") authHeader: String): Response<PostResponse>

    @PUT("daily/{id}")
    suspend fun updateDailyPrayer(@Path("id") id: Long, @Body prayer: PostModel, @Header("Authorization") authHeader: String): Response<PostResponse>

    @DELETE("daily/{id}")
    suspend fun deleteDailyPrayer(@Path("id") id: Long,@Header("Authorization") authHeader: String): Response<PostResponse>


    //Weekly prayer
    @POST("weekly/create")
    suspend fun createWeeklyPrayer(@Body prayer: PostModel, @Header("Authorization") authHeader: String): Response<PostResponse>

    @GET("weekly/all")
    suspend fun getAllWeeklyPrayers(@Header("Authorization") authHeader: String): Response<List<PostResponse>>

    //events
    @POST("events/create")
    suspend fun createEventCalendar(@Body prayer: PostModel, @Header("Authorization") authHeader: String): Response<PostResponse>

    @GET("events/all")
    suspend fun getAllEventsCalendar(@Header("Authorization") authHeader: String): Response<List<PostResponse>>

    //testimonial
    @POST("testimonials/create")
    suspend fun createTestimonial(@Body prayer: PostModel, @Header("Authorization") authHeader: String): Response<PostResponse>

    @GET("testimonials/all")
    suspend fun getAllTestimonials(@Header("Authorization") authHeader: String): Response<List<PostResponse>>

    //sunday diaries
    @POST("sunday/create")
    suspend fun createSundayDiary(@Body prayer: PostModel, @Header("Authorization") authHeader: String): Response<PostResponse>

    @GET("sunday/all")
    suspend fun getAllSundayDiaries(@Header("Authorization") authHeader: String): Response<List<PostResponse>>

    //nature talent
    @POST("talent/create")
    suspend fun createTalent(@Body prayer: PostModel, @Header("Authorization") authHeader: String): Response<PostResponse>

    @GET("talent/all")
    suspend fun getAllTalents(@Header("Authorization") authHeader: String): Response<List<PostResponse>>

    //announcement
    @POST("announce/create")
    suspend fun createAnnouncement(@Body prayer: PostModel, @Header("Authorization") authHeader: String): Response<PostResponse>

    @GET("announce/all")
    suspend fun getAllAnnouncements(@Header("Authorization") authHeader: String): Response<List<PostResponse>>

}
