package nl.tiebe.otarium.ui.onboarding

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.StringResource
import nl.tiebe.otarium.Data
import nl.tiebe.otarium.MR
import nl.tiebe.otarium.ui.utils.LabelledCheckBox
import nl.tiebe.otarium.utils.ui.getLocalizedString

internal class OnboardingItems(
    val title: StringResource,
    val desc: StringResource,
    val extraItems: @Composable () -> Unit = {}
) {
    companion object{
        fun getData(): List<OnboardingItems>{
            return listOf(
                OnboardingItems(
                    title = MR.strings.onboarding_title_1,
                    desc = MR.strings.onboarding_desc_1
                ), OnboardingItems(
                    title = MR.strings.onboarding_title_2,
                    desc = MR.strings.onboarding_desc_2
                ), OnboardingItems(
                    title = MR.strings.onboarding_title_3,
                    desc = MR.strings.onboarding_desc_3
                )
            )
        }
    }
}