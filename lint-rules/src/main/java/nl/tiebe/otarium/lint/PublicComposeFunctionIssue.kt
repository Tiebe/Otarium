package nl.tiebe.otarium.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod

@Suppress("UnstableApiUsage")
object PublicComposeFunctionIssue {
    /**
     * The fixed id of the issue
     */
    private const val ID = "nl.tiebe.otarium.lint.PublicComposeFunctionIssue"

    /**
     * The priority, a number from 1 to 10 with 10 being most important/severe
     */
    private const val PRIORITY = 10

    /**
     * Description short summary (typically 5-6 words or less), typically describing
     * the problem rather than the fix (e.g. "Missing minSdkVersion")
     */
    private const val DESCRIPTION = "Compose functions should be internal"

    /**
     * A full explanation of the issue, with suggestions for how to fix it
     */
    private const val EXPLANATION = """
       Compose functions should be internal in a Jetbrains Compose Multiplatform project. This code will not work on iOS.
    """

    /**
     * The associated category, if any @see [Category]
     */
    private val CATEGORY = Category.LINT

    /**
     * The default severity of the issue
     */
    private val SEVERITY = Severity.WARNING

    val ISSUE = Issue.create(
        ID,
        DESCRIPTION,
        EXPLANATION,
        CATEGORY,
        PRIORITY,
        SEVERITY,
        Implementation(RxIssueDetector::class.java, Scope.JAVA_FILE_SCOPE)
    )

    class RxIssueDetector : Detector(), Detector.UastScanner {
        override fun getApplicableUastTypes(): List<Class<out UElement>> =
            listOf(UMethod::class.java)

        override fun createUastHandler(context: JavaContext): UElementHandler =
            RxNodeVisitor(context)
    }
}