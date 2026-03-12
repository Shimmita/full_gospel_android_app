import java.util.Date

data class User(
    val userId: Long,
    val username: String,
    val phone: String,
    val first_name: String,
    val last_name: String,
    val role: String,
    val imagePath: String,
    val createdAt: Date,
    val updatedAt: Date,
    val enabled: Boolean,
)
