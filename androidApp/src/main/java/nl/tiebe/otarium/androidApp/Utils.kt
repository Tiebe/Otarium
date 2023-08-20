package nl.tiebe.otarium.androidApp

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.datetime.*
import nl.tiebe.otarium.R
import java.io.File

fun setupNotifications(context: Context) {
    createNotificationChannel(context)
    askNotificationPermission(context)

    askBatteryOptimizationPermission(context)
}

fun askNotificationPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            nl.tiebe.otarium.androidApp.ui.utils.Android.requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

fun createNotificationChannel(context: Context) {
    val name = context.getString(R.string.grades_channel)
    val descriptionText = context.getString(R.string.grades_channel_description)
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel("grades", name, importance).apply {
        description = descriptionText
    }
    channel.enableVibration(true)

    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}
@SuppressLint("BatteryLife")
fun askBatteryOptimizationPermission(context: Context) {
    context.startActivity(
        Intent(
            Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            Uri.parse("package:" + context.packageName)
        )
    )
}

fun closeApp(context: Context) {
    val i = Intent(Intent.ACTION_MAIN)
    i.addCategory(Intent.CATEGORY_HOME)
    context.startActivity(i)
}

fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = android.content.ClipData.newPlainText("Accounts", text)
    clipboard.setPrimaryClip(clip)
}

fun getClipboardText(context: Context): String {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = clipboard.primaryClip
    return clip?.getItemAt(0)?.text.toString()
}

fun openUrl(context: Context, url: String) {
    var checkedUrl = url

    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        checkedUrl = "http://$url"
    }

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(checkedUrl))
    context.startActivity(intent)
}

fun writeFile(context: Context, id: String, fileName: String, data: ByteArray) {
    val directory = File(context.cacheDir, id)
    if (!directory.exists()) {
        directory.mkdir()
    }

    val file = File(directory, fileName)
    file.writeBytes(data)
}

fun openFileFromCache(context: Context, id: String, fileName: String) {
    val file = File(File(context.cacheDir, id), fileName)
    val intent = Intent(Intent.ACTION_VIEW)

    val fileUri = FileProvider.getUriForFile(context, context.applicationContext.packageName, file)

    intent.setData(fileUri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(Intent.createChooser(intent, fileName))
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun dynamicColorsPossible(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}

fun convertImageByteArrayToBitmap(imageData: ByteArray): ImageBitmap {
    val image = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
    return image.asImageBitmap()
}

@OptIn(ExperimentalFoundationApi::class)
fun getStartOfWeekFromDay(page: Int, initialPage: Int, now: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Amsterdam"))): LocalDate {
    val initialPageDate = now.date.minus(now.dayOfWeek.ordinal, DateTimeUnit.DAY)

    return initialPageDate.plus(7*(page.floorDiv(7) - initialPage.floorDiv(7)), DateTimeUnit.DAY)
}