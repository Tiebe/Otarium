package nl.tiebe.otarium.utils


import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

actual fun getLocalizedString(string: StringResource): String {
    return StringDesc.Resource(string).localized()
}