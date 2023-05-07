package nl.tiebe.otarium.utils.ui

import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.StringResource

expect fun getLocalizedString(string: StringResource): String

expect fun Float.format(decimals: Int): String

expect fun getText(file: FileResource): String