package nl.tiebe.otarium.logic.data.wrapper.elo

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

interface LearningResource : Parcelable {
    val id: Int

}