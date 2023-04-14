package nl.tiebe.otarium.utils.versions

import nl.tiebe.otarium.settings
import nl.tiebe.otarium.setupNotifications
import nl.tiebe.otarium.utils.versions.v21.migrateFromV21

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
}