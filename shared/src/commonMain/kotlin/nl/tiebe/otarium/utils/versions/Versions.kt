package nl.tiebe.otarium.utils.versions

import nl.tiebe.otarium.settings

fun runVersionCheck(oldVersion: Int) {
    if (oldVersion <= 36) {
        settings.clear()
    }
}