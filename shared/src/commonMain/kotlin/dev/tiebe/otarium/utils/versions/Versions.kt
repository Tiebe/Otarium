package dev.tiebe.otarium.utils.versions

import dev.tiebe.otarium.Data
import dev.tiebe.otarium.settings
import dev.tiebe.otarium.setupNotifications
import dev.tiebe.otarium.utils.versions.v21.migrateFromV21

fun runVersionCheck(oldVersion: Int) {
    if (oldVersion <= 14) {
        settings.remove("agenda")
    }

    if (oldVersion <= 17) {
        settings.clear()
    }

    if (oldVersion <= 20) {
        setupNotifications()
    }

    if (oldVersion <= 21) {
        migrateFromV21()
    }

    if (oldVersion <= 33) {
        try {
            Data.selectedAccount.accountId
        } catch (_: RuntimeException) {
            settings.clear()
        }
    }
}