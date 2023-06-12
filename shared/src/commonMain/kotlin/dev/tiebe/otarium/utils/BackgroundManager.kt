package dev.tiebe.otarium.utils

expect fun reloadTokensBackground(delay: Long = 0)

expect fun refreshGradesBackground(delay: Long = 0)

expect fun sendNotification(title: String, message: String)