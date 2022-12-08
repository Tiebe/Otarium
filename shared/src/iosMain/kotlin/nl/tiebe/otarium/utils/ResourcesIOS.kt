package nl.tiebe.otarium.utils

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

actual fun getLocalizedString(string: StringResource): String {
    return StringDesc.Resource(string).localized()
}

actual fun Float.format(decimals: Int): String {
    val formatter = NSNumberFormatter()
    formatter.minimumFractionDigits = 0u
    formatter.maximumFractionDigits = 2u
    formatter.numberStyle = decimals //Decimal
    return formatter.stringFromNumber(NSNumber(double))!!
}