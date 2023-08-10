package nl.tiebe.otarium.logic.data.wrapper.messages

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

interface Attachment : Parcelable {
    val id: Int

}