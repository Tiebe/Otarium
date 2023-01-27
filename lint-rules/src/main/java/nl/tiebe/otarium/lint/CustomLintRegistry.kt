package nl.tiebe.otarium.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API

class CustomLintRegistry : IssueRegistry() {
    override val issues = listOf(RxJavaUnconventionalMethodNamingIssue.ISSUE)

    override val api: Int = CURRENT_API

    override val minApi: Int = 6
}