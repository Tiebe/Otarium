package nl.tiebe.otarium.utils

import dev.icerock.moko.resources.StringResource

expect fun getLocalizedString(string: StringResource): String

expect fun Float.format(decimals: Int): String