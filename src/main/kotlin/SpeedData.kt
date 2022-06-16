import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
object SpeedData : CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job


    private const val CURRENT_CONNECT_TIME_TAG = "<CurrentConnectTime>"
    var currentConnectTime by mutableStateOf(0L)

    private const val CURRENT_UPLOAD_TAG = "<CurrentUpload>"
    var currentUpload by mutableStateOf(0L)

    private const val CURRENT_DOWNLOAD_TAG = "<CurrentDownload>"
    var currentDownload by mutableStateOf(0L)

    private const val CURRENT_DOWNLOAD_RATE_TAG = "<CurrentDownloadRate>"
    var currentDownloadRate by mutableStateOf(0L)

    private const val CURRENT_UPLOAD_RATE_TAG = "<CurrentUploadRate>"
    var currentUploadRate by mutableStateOf(0L)

    private const val TOTAL_UPLOAD_TAG = "<TotalUpload>"
    var totalUpload by mutableStateOf(0L)

    private const val TOTAL_DOWNLOAD_TAG = "<TotalDownload>"
    var totalDownload by mutableStateOf(0L)

    private const val TOTAL_CONNECT_TIME_TAG = "<TotalConnectTime>"
    var totalConnectTime by mutableStateOf(0L)

    private const val SHOW_TRAFFIC_TAG = "<showtraffic>"
    var showTraffic by mutableStateOf(true)

    fun startPullData() = launch {
        val client = HttpClient(CIO)
        while (true) {
            val response: HttpResponse = client.get("http://192.168.8.1/api/monitoring/traffic-statistics")
            updateFromString(response.bodyAsText())
            delay(1000L)
        }

    }

    private fun updateFromString(bodyContent: String) {
        if (bodyContent.isBlank()) return
        bodyContent.split("\n").forEach { originalLine ->
            if (originalLine.isBlank()) return@forEach
            when {
                originalLine.startsWith(CURRENT_CONNECT_TIME_TAG) -> {
                    originalLine.readLongFromLine()?.let { currentConnectTime = it }
                }
                originalLine.startsWith(CURRENT_UPLOAD_TAG) -> {
                    originalLine.readLongFromLine()?.let { currentUpload = it }
                }
                originalLine.startsWith(CURRENT_DOWNLOAD_TAG) -> {
                    originalLine.readLongFromLine()?.let { currentDownload = it }
                }
                originalLine.startsWith(CURRENT_DOWNLOAD_RATE_TAG) -> {
                    originalLine.readLongFromLine()?.let { currentDownloadRate = it }
                }
                originalLine.startsWith(CURRENT_UPLOAD_RATE_TAG) -> {
                    originalLine.readLongFromLine()?.let { currentUploadRate = it }
                }
                originalLine.startsWith(TOTAL_UPLOAD_TAG) -> {
                    originalLine.readLongFromLine()?.let { totalUpload = it }
                }
                originalLine.startsWith(TOTAL_DOWNLOAD_TAG) -> {
                    originalLine.readLongFromLine()?.let { totalDownload = it }
                }
                originalLine.startsWith(TOTAL_CONNECT_TIME_TAG) -> {
                    originalLine.readLongFromLine()?.let { totalConnectTime = it }
                }
                originalLine.startsWith(SHOW_TRAFFIC_TAG) -> {
                    originalLine.readLongFromLine()?.let { showTraffic = it == 1L }
                }
            }
        }
    }

    private fun String.readLongFromLine(): Long? {
        val startIndex = this.indexOfFirst { it == '>' }
        val endIndex = this.indexOfAny(charArrayOf('<'), 1)
        if (startIndex in 0 until endIndex) {
            return this.substring(startIndex + 1, endIndex).toLong()
        }
        return null
    }
}