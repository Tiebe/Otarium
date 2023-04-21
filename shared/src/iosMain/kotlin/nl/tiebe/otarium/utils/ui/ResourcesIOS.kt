package nl.tiebe.otarium.utils.ui

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.UIKit.UIWindow

object IOS {
    lateinit var window: UIWindow
}

actual fun getLocalizedString(string: StringResource): String {
    return StringDesc.Resource(string).localized()
}

actual fun Float.format(decimals: Int): String {
    val formatter = NSNumberFormatter()
    formatter.minimumFractionDigits = decimals.toULong()
    formatter.maximumFractionDigits = decimals.toULong()
    formatter.numberStyle = NSNumberFormatterDecimalStyle
    return formatter.stringFromNumber(NSNumber(this))!!
}