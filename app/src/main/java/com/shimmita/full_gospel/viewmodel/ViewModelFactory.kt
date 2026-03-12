import androidx.lifecycle.ViewModelProvider
import com.shimmita.full_gospel.auth.DataStoreManager

class ViewModelFactory(
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(com.shimmita.full_gospel.viewmodel.ViewModel::class.java)) {
            return com.shimmita.full_gospel.viewmodel.ViewModel(dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
