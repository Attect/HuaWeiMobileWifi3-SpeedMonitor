// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlin.system.exitProcess

@Composable
@Preview
fun App() {

    MaterialTheme {
        Row (modifier = Modifier.padding(8.dp)) {
            val fontSize = 12.sp
            Column {
                Text("上传速度",fontSize = fontSize)
                Text(SpeedData.currentUploadRate.toReadableSize()+"/s",fontSize = fontSize)
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text("下载速度",fontSize = fontSize)
                Text(SpeedData.currentDownloadRate.toReadableSize()+"/s",fontSize = fontSize)
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text("当前上传",fontSize = fontSize)
                Text(SpeedData.currentUpload.toReadableSize(),fontSize = fontSize)
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text("当前下载",fontSize = fontSize)
                Text(SpeedData.currentDownload.toReadableSize(),fontSize = fontSize)
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text("总计上传",fontSize = fontSize)
                Text(SpeedData.totalUpload.toReadableSize(),fontSize = fontSize)
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text("总计下载",fontSize = fontSize)
                Text(SpeedData.totalDownload.toReadableSize(),fontSize = fontSize)
            }
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

        Window(title = "华为随行WiFi3 网速监控", state = WindowState(width =440.dp, height = 90.dp), onCloseRequest = {
            exitProcess(0)
        }) {
            App()
        }
    }

}