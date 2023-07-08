package nl.tiebe.otarium.ui.home.messages.composing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nl.tiebe.otarium.logic.root.home.children.messages.children.composing.MessageComposeComponent

@Composable
fun MessageComposeScreen(component: MessageComposeComponent) {
    Column(Modifier.fillMaxSize()) {
        ToInputField(component)
/*        CcInputField(component)
        BccInputField(component)
        SubjectInputField(component)
        BodyInputField(component)
        SendButton(component)*/
    }
}


@Composable
fun ToInputField(component: MessageComposeComponent) {

}