package nl.tiebe.otarium.utils.ui

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

actual fun getLocalizedString(string: StringResource): String {
    return StringDesc.Resource(string).localized()
}

actual fun Float.format(decimals: Int): String {
    val df = java.text.DecimalFormat()
    df.isGroupingUsed = true
    df.maximumFractionDigits = decimals
    df.isDecimalSeparatorAlwaysShown = true
    val formatted = df.format(this).replace(".", ",")

    if (formatted.split(",")[1].length < decimals) {
        return formatted + "0".repeat(decimals - formatted.split(",")[1].length)
    }

    return formatted
}