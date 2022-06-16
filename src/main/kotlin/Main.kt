// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlin.system.exitProcess

@Composable
@Preview
fun App() {

    MaterialTheme {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("当前上传速度:${SpeedData.currentUploadRate.toReadableSize()}/s")
            Text("当前下载速度:${SpeedData.currentDownloadRate.toReadableSize()}/s")
            Text("")
            Text("当前上传:${SpeedData.currentUpload.toReadableSize()}")
            Text("当前下载:${SpeedData.currentDownload.toReadableSize()}")
        }
    }
}

val dataUnit = arrayOf(
    "Byte", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB"
)

fun Long.toReadableSize(): String {
    var currentValue = this.toDouble()
    var unitIndex = 0
    while (currentValue > 1536) {
        currentValue /= 1024.0
        unitIndex++
    }
    if (unitIndex < dataUnit.size) {
        val unit = dataUnit[unitIndex]
        return "%.2f".format(currentValue) + unit
    }
    return toString() + dataUnit[0]
}

fun main() {

    SpeedData.startPullData()

    application {

        Window(title = "华为随行WiFi3 网速监控", state = WindowState(width = 280.dp, height = 180.dp), onCloseRequest = {
            exitProcess(0)
        }) {
            App()
        }
    }

}