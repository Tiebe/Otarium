package nl.tiebe.otarium.androidApp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import nl.tiebe.otarium.ui.onboarding.DefaultOnboardingComponent
import nl.tiebe.otarium.ui.onboarding.OnboardingComponent
import nl.tiebe.otarium.ui.onboarding.OnboardingScreen
import nl.tiebe.otarium.ui.theme.OtariumTheme
import org.junit.Rule
import org.junit.Test

class OnboardingTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun onboardingTest() {
        test {
            composeTestRule.onNodeWithText("Otarium").assertExists()
        }

    }

    private fun createComponent(): OnboardingComponent {
        val lifecycle = LifecycleRegistry()

        val component = DefaultOnboardingComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                navigateRootComponent = {}
            )

        lifecycle.resume()

        return component
    }

    private fun test(
        block: suspend CoroutineScope.(OnboardingComponent) -> Unit
    ) {
        runBlocking {
            val component = createComponent()

            composeTestRule.setContent {
                OtariumTheme {
                    OnboardingScreen(component)
                }
            }

            composeTestRule.awaitIdle()

            block(component)
        }
    }

}