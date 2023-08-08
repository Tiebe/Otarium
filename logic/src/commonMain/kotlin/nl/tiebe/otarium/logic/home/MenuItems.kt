package nl.tiebe.otarium.logic.home

import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.icerock.moko.resources.StringResource

@Parcelize
sealed class MenuItem(val resourceId: StringResource, val icon: @Composable () -> Unit, val iconSelected: @Composable () -> Unit): Parcelable