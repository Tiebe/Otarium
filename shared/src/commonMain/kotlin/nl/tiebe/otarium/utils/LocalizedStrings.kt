package nl.tiebe.otarium.utils

import dev.icerock.moko.resources.StringResource
import nl.tiebe.magisterapi.utils.format

expect fun getLocalizedString(string: StringResource): String

fun Float.format(digits: Int) = "%.${digits}f".format(this)