package nl.tiebe.otarium.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import kotlin.math.min

/**
 * The tags to interpret. Add tags here and in [tagToStyle].
 */
private val tags2 = linkedMapOf(
    "<b>" to "</b>",
    "<i>" to "</i>",
    "<u>" to "</u>",
    "<strong>" to "</strong>",
    "<h1>" to "</h1>",
    "<h2>" to "</h2>",
    "<h3>" to "</h3>",
    "<h4>" to "</h4>",
    "<h5>" to "</h5>",
    "<p>" to "</p>",
    /*    "<blockquote>" to "</blockquote>",

        "<em>" to "</em>",
        "<p style=\"text-align: center\">" to "</p>",*/
)

private val tags = listOf(
    Tag("<p>", "</p>", spanStyle = SpanStyle()),

    Tag("<b>", "</b>", spanStyle = SpanStyle(fontWeight = FontWeight.Bold)),
    Tag("<i>", "</i>", spanStyle = SpanStyle(fontStyle = FontStyle.Italic)),
    Tag("<u>", "</u>", spanStyle = SpanStyle(textDecoration = TextDecoration.Underline)),
    Tag("<strong>", "</strong>", spanStyle = SpanStyle(fontWeight = FontWeight.Bold)),
    Tag("<em>", "</em>", spanStyle = SpanStyle(fontStyle = FontStyle.Italic)),
    Tag("<blockquote>", "</blockquote>", spanStyle = SpanStyle(background = Color.LightGray, color = Color.Black, fontStyle = FontStyle.Italic)),

    Tag("<p style=\"text-align: left\">", "<//p>", paragraphStyle = ParagraphStyle(textAlign = TextAlign.Start)),
    Tag("<p style=\"text-align: center\">", "<//p>", paragraphStyle = ParagraphStyle(textAlign = TextAlign.Center)),
    Tag("<p style=\"text-align: right\">", "<//p>", paragraphStyle = ParagraphStyle(textAlign = TextAlign.Right)),
    Tag("<p style=\"margin-left", "<//p>", regex = "<p style=\"margin-left: (.+?)rem\">", regexAction = { to, value -> to.pushStyle(ParagraphStyle(textIndent = TextIndent(firstLine = value.toInt() * 16.sp, restLine = value.toInt() * 16.sp))) }),

    Tag("<h1>", "</h1>", spanStyle = SpanStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold)),
    Tag("<h2>", "</h2>", spanStyle = SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)),
    Tag("<h3>", "</h3>", spanStyle = SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)),
    Tag("<h4>", "</h4>", spanStyle = SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)),
    Tag("<h5>", "</h5>", spanStyle = SpanStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)),

    Tag("<li>", "</li>", paragraphStyle = ParagraphStyle(textIndent = TextIndent(firstLine = 12.sp, restLine = 12.sp))),
)

/**
 * The main entry point. Call this on a String and use the result in a Text.
 */
fun String.parseHtml(): AnnotatedString {
    val specialCharacterReplace = this
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&nbsp;", " ")

    val newlineReplace = specialCharacterReplace
        .replace("<br>", "\n")
        .replace("<p>", "\n<p>")
        .replace("<span>", "")
        .replace("</span>", "")
        .replace("</blockquote>", "</blockquote>\n")
        .replace("<h1>", "<h1>\n")
        .replace("</h1>", "\n</h1>")
        .replace("</h2>", "\n</h2>")
        .replace("</h3>", "\n</h3>")
        .replace("</h4>", "\n</h4>")
        .replace("</h5>", "\n</h5>")
        .replace("<li>\n", "\n<li>")


    return buildAnnotatedString {
        recurse(newlineReplace, this)
    }
}

/**
 * Recurses through the given HTML String to convert it to an AnnotatedString.
 *
 * @param string the String to examine.
 * @param to the AnnotatedString to append to.
 */
private fun recurse(string: String, to: AnnotatedString.Builder) {
    //Find the opening tag that the given String starts with, if any.
    val startTag = tags.map { it.openingTag }.find { string.startsWith(it) }

    //Find the closing tag that the given String starts with, if any.
    val endTag = tags.map { it.closingTag }.find { string.startsWith(it) }

    when {
        tags.any {
            if (string.startsWith(it.closingTag)) {
                if (it.paragraphStyle != null) to.pop()
                if (it.spanStyle != null) to.pop()
                recurse(string.removeRange(0, endTag!!.length), to)
                return@any true
            } else return@any false
        } -> {}

        tags.any {
            return@any if (it.regex == null && string.startsWith(it.openingTag)) {
                if (it.spanStyle != null) to.pushStyle(it.spanStyle)
                if (it.paragraphStyle != null) to.pushStyle(it.paragraphStyle)
                recurse(string.removeRange(0, startTag!!.length), to)
                true
            } else false
        } -> {}

        tags.any {
            if (it.regex != null && string.startsWith(it.openingTag)) {
                val regex = Regex(it.regex)
                val match = regex.find(string, string.indexOf(it.openingTag))

                if (match != null) {
                    val group = match.groups[1]
                    if (group != null) {
                        it.regexAction?.let { it1 -> it1(to, group.value) }
                        recurse(string.removeRange(0, match.value.length), to)
                        true
                    } else false
                } else false
            } else false
        } -> {}

        //If the String doesn't start with an opening or closing tag, but does contain either,
        //find the lowest index (that isn't -1/not found) for either an opening or closing tag.
        //Append the text normally up until that lowest index, and then recurse starting from that index.
        tags.any { string.contains(it.openingTag) || string.contains(it.closingTag) } -> {
            val firstStart = tags.map { it.openingTag }.map { string.indexOf(it) }.filterNot { it == -1 }.minOrNull() ?: -1
            val firstEnd = tags.map { it.closingTag }.map { string.indexOf(it) }.filterNot { it == -1 }.minOrNull() ?: -1
            val first = when {
                firstStart == -1 -> firstEnd
                firstEnd == -1 -> firstStart
                else -> min(firstStart, firstEnd)
            }

            to.append(string.substring(0, first))

            recurse(string.removeRange(0, first), to)
        }
        //There weren't any supported tags found in the text. Just append it all normally.
        else -> {
            to.append(string)
        }
    }
}

/**
 * Get a [SpanStyle] for a given (opening) tag.
 * Add your own tag styling here by adding its opening tag to
 * the when clause and then instantiating the appropriate [SpanStyle].
 *
 * @return a [SpanStyle] for the given tag.
 */
private fun tagToStyle(tag: String): TextStyle {
    return when (tag) {
        "<b>" -> {
            TextStyle(fontWeight = FontWeight.Bold)
        }
        "<i>" -> {
            TextStyle(fontStyle = FontStyle.Italic)
        }
        "<u>" -> {
            TextStyle(textDecoration = TextDecoration.Underline)
        }
        "<strong>" -> {
            TextStyle(fontWeight = FontWeight.Bold)
        }
        "<h1>" -> {
            TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Bold, lineHeight = 60.sp)
        }
        "<h2>" -> {
            TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold, lineHeight = 60.sp)
        }
        "<h3>" -> {
            TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 60.sp)
        }
        "<h4>" -> {
            TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, lineHeight = 60.sp)
        }
        "<h5>" -> {
            TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, lineHeight = 60.sp)
        }
/*        "<blockquote>" -> {
            TextStyle(background = Color.Gray, color = Color.White)
        }
        "<em>" -> {
            TextStyle(fontStyle = FontStyle.Italic)
        }
        "<p style=\"text-align: center\">" -> {
            TextStyle(textAlign = TextAlign.Center)
        }*/
        "<p>" -> {
            TextStyle()
        }
        //This should only throw if you add a tag to the [tags] Map and forget to add it
        //to this function.
        else -> throw IllegalArgumentException("Tag $tag is not valid.")
    }
}

data class Tag(
    val openingTag: String,
    val closingTag: String,
    val spanStyle: SpanStyle? = null,
    val paragraphStyle: ParagraphStyle? = null,
    val regex: String? = null,
    var regexAction: ((AnnotatedString.Builder, String) -> Unit)? = null,
)