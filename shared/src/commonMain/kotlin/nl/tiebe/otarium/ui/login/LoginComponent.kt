package nl.tiebe.otarium.ui.login

import com.arkivanov.decompose.ComponentContext

interface LoginComponent {
}

class DefaultLoginComponent(val componentContext: ComponentContext): LoginComponent, ComponentContext by componentContext {
}