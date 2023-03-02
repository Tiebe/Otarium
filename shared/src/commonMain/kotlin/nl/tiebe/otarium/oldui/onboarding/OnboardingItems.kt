package nl.tiebe.otarium.oldui.onboarding

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
import nl.tiebe.otarium.oldui.utils.LabelledCheckBox
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
                    title = MR.strings.onboarding_title_4,
                    desc = MR.strings.onboarding_desc_4
                ) {
                    val checkedState = remember { mutableStateOf(true) }
                    val checkedState2 = remember { mutableStateOf(false) }

                    remember { Data.showAds = true }

                    LabelledCheckBox(checked = checkedState.value, onCheckedChange = {
                        checkedState.value = it
                        Data.showAds = it
                    }, label = getLocalizedString(MR.strings.show_ads_checkbox))

                    LabelledCheckBox(
                        checkable = checkedState.value,
                        checked = checkedState2.value,
                        onCheckedChange = {
                            checkedState2.value = it
                            Data.ageOfConsent = true
                        },
                        label = getLocalizedString(MR.strings.age_checkbox)
                    )

                    Spacer(Modifier.padding(30.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = getLocalizedString(MR.strings.age_requirement),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center,
                            letterSpacing = 1.sp,
                        )
                    }
                }, OnboardingItems(

                    title = MR.strings.onboarding_title_3,
                    desc = MR.strings.onboarding_desc_3
                )
            )
        }
    }
}