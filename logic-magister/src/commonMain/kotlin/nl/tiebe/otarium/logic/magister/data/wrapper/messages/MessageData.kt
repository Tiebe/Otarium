package nl.tiebe.otarium.logic.magister.data.wrapper.messages

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

interface MessageData : Parcelable {
    val id: Int

}