package nl.tiebe.otarium.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.JavaContext
import com.intellij.psi.PsiClassType
import nl.tiebe.otarium.lint.PublicComposeFunctionIssue.ISSUE
import org.jetbrains.uast.UMethod

class RxNodeVisitor(private val context: JavaContext) : UElementHandler() {
    override fun visitMethod(node: UMethod) {
        if (node.name.length > 1) {
            reportIssue(node)
        }
    }

    private fun UMethod.returnClassName(): String =
        (this.returnType as? PsiClassType)?.className ?: ""

    private fun reportIssue(node: UMethod) {
        context.report(
            issue = ISSUE,
            scopeClass = node,
            location = context.getNameLocation(node),
            message = """
                [Single] returning functions should be named with suffix Once. 
                Example: removeAccountOnce()
            """
        )
    }
}