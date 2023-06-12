package dev.tiebe.otarium.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.tiebe.otarium.MR
import dev.tiebe.otarium.utils.ui.getLocalizedString

@Composable
internal fun LoadingScreen() {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator()

        Spacer(Modifier.height(5.dp))

        Text(getLocalizedString(MR.strings.loading))
    }
}