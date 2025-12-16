import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import com.griffith.viewmodels.JournalViewModel

//a custom text component that converts GeoPoint coordinates to human-readable locations
@Composable
fun AddressText(lat: Double, lon: Double, viewModel: JournalViewModel) {
    var address: String? by remember{mutableStateOf("Loading...")}
    //trigger fetch
    LaunchedEffect(lat, lon) {
       address = viewModel.fetchAddress(lat, lon)
    }
    Text(text = address ?: "N/A",
        maxLines = 2, // lines maximum
        overflow = TextOverflow.Ellipsis
    )
}
